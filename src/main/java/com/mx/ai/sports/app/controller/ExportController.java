package com.mx.ai.sports.app.controller;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.sheet.wrapper.NoTemplateSheetWrapper;
import com.google.common.base.Stopwatch;
import com.mx.ai.sports.app.api.ExportApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.oss.OssUploadUtil;
import com.mx.ai.sports.course.dto.ExportRecordStudentDto;
import com.mx.ai.sports.course.dto.ExportRecordTotalDto;
import com.mx.ai.sports.course.query.ExportAllQuery;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.system.dto.ImportStudentDto;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.service.IImportService;
import com.mx.ai.sports.system.service.ISubjectService;
import com.mx.ai.sports.system.service.ITeacherRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 导出相关接口
 *
 * @author Mengjiaxin
 * @date 2020/9/18 10:42 上午
 */
@Slf4j
@RestController("ExportApi")
public class ExportController extends BaseRestController implements ExportApi {

    @Autowired
    private ICourseService courseService;

    @Autowired
    private OssUploadUtil ossUploadUtil;

    @Autowired
    private ITeacherRegisterService teacherRegisterService;

    @Autowired
    private IImportService importService;

    @Autowired
    private ISubjectService subjectService;


    @Override
    @TeacherRole
    @Log("导出时间段内的课程统计数据")
    public AiSportsResponse<String> exportAll(@RequestBody @Valid ExportAllQuery query) {

        try {
            List<ExportRecordTotalDto> exportRecordTotalVos = courseService.findExportRecordTotal(query.getStartTime(), query.getEndTime());

            Map<String, List<ExportRecordStudentDto>> exportRecordStudentMap = courseService.findExportRecordStudent(query.getStartTime(), query.getEndTime());

            String fileName = generateFileName(query.getStartTime(), query.getEndTime());

            String ossUrl = generateExcel(fileName, exportRecordTotalVos, exportRecordStudentMap);

            return new AiSportsResponse<String>().success().data(ossUrl);
        } catch (Exception e) {
            log.error("导出失败，异常：{}", e.getMessage());
            e.printStackTrace();
            return new AiSportsResponse<String>().fail().message("导出失败!" + e.getMessage());
        }
    }

    @Override
    public AiSportsResponse<Boolean> importTeacher(@NotNull MultipartFile file) {

        try {
            // 将导入的教师数据转换为教师注册表数据
            List<TeacherRegister> teacherRegisters = importService.getImportTeacherByFile(file);
            // 查询将全部的已注册老师查询出来
            List<TeacherRegister> oldTeacherRegisters = teacherRegisterService.list();

            if (!CollectionUtils.isEmpty(oldTeacherRegisters)) {
                List<String> oldUserNames = oldTeacherRegisters.stream().map(TeacherRegister::getUsername).distinct().collect(Collectors.toList());
                for (TeacherRegister tr : teacherRegisters) {
                    if (oldUserNames.contains(tr.getUsername())) {
                        return new AiSportsResponse<Boolean>().fail().message(tr.getFullName() + " " + tr.getUsername() + "，已经导入过，请删除这个用户再导入数据！");
                    }
                }
            }
            // 批量的注册老师账号
            teacherRegisterService.batchRegister(teacherRegisters);

        } catch (AiSportsException | IOException e) {
            e.printStackTrace();
        }

        return new AiSportsResponse<Boolean>().success().data(true);
    }

    @Override
    public AiSportsResponse<Boolean> importStudent(@NotNull MultipartFile file) {

        try {
            Stopwatch allStopwatch = Stopwatch.createStarted();
            log.info("开始导入学生基础信息-初始化主课、班级、学生信息...");
            List<ImportStudentDto> importTeacherDtos = importService.getImportStudentDtoByFile(file);
            // 1、初始化主课程数据
            // 2、初始化所有的主课程序号
            // 3、初始化所有的老师
            // 4、初始化所有的班级
            // 5、初始化所有的班级和老师的关系
            // 6、初始化所有的临时学生基础信息
            importService.batchImportStudentToDb(importTeacherDtos);

            log.info("导入学生基础信息任务完成，总耗时：{}", allStopwatch.stop());

        } catch (AiSportsException | IOException e) {
            e.printStackTrace();
            return new AiSportsResponse<Boolean>().fail().message("异常信息：" + e.getMessage());
        }
        return new AiSportsResponse<Boolean>().success().data(true);
    }

    /**
     * 构建文件名称
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 文件名称
     */
    private String generateFileName(Date startTime, Date endTime) {
        String suffix = ".xlsx";
        String prefix = "课程统计数据";

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startTimeStr = format.format(startTime);
        String endTimeStr = format.format(endTime);

        return prefix + "(" + startTimeStr + "至" + endTimeStr + ")" + suffix;
    }

    /**
     * 构建excel文件
     *
     * @param fileName               文件名称
     * @param exportRecordTotalVos   课程总览数据
     * @param exportRecordStudentMap 课程学生数据
     * @return oss地址
     * @throws Exception
     */
    public String generateExcel(String fileName, List<ExportRecordTotalDto> exportRecordTotalVos,
                                Map<String, List<ExportRecordStudentDto>> exportRecordStudentMap) throws Exception {

        List<NoTemplateSheetWrapper> sheets = new ArrayList<>();
        sheets.add(new NoTemplateSheetWrapper(exportRecordTotalVos, ExportRecordTotalDto.class, true, "课程总览"));

        exportRecordStudentMap.forEach((courseName, list) -> {
            sheets.add(new NoTemplateSheetWrapper(list, ExportRecordStudentDto.class, true, courseName));
        });

        ExcelUtils.getInstance().noTemplateSheet2Excel(sheets, fileName);

        return ossUploadUtil.uploadToOss(new File(fileName));
    }
}
