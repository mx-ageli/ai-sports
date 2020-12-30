package com.mx.ai.sports.app.controller;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.github.crab2died.sheet.wrapper.NoTemplateSheetWrapper;
import com.google.common.base.Stopwatch;
import com.mx.ai.sports.app.api.ExportApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.oss.OssUploadUtil;
import com.mx.ai.sports.common.utils.FileUtil;
import com.mx.ai.sports.common.utils.ZipMultiFile;
import com.mx.ai.sports.course.dto.*;
import com.mx.ai.sports.course.query.ExportAllQuery;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.service.IRecordStudentService;
import com.mx.ai.sports.system.dto.ImportStudentDto;
import com.mx.ai.sports.system.dto.SubjectTeacherDto;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.entity.Term;
import com.mx.ai.sports.system.service.IImportService;
import com.mx.ai.sports.system.service.ISubjectService;
import com.mx.ai.sports.system.service.ITeacherRegisterService;
import com.mx.ai.sports.system.service.ITermService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private IRecordStudentService recordStudentService;

    @Autowired
    private ITermService termService;


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

    @Override
    public AiSportsResponse<String> exportStudentRecord(@NotNull @RequestParam("termId") Long termId) {
        Term term = termService.getById(termId);
        if (term == null) {
            return new AiSportsResponse<String>().fail().message("没有查询到学期信息！");
        }

        // 先查询到所有的课程以及序号和老师
        List<SubjectTeacherDto> subjectTeacherList = subjectService.findSubjectTeacherDto();
        // 根据课程Id进行分组
        Map<Long, List<SubjectTeacherDto>> subjectTeacherMap = subjectTeacherList.stream().collect(Collectors.groupingBy(SubjectTeacherDto::getSubjectId));

        String allFileName = "AI健身记录(" + term.getTermName() + ")";

        subjectTeacherMap.forEach((subjectId, subjectSeqList) -> {
            // 根据课程Id去查询课程下面的学生所有合格的健身记录
            List<StudentRecordTotalDto> studentRecordTotalList = recordStudentService.findStudentRecordDetailBySubjectId(subjectId);
            // 按照课程序号去分组
            Map<Long, List<StudentRecordTotalDto>> studentRecordTotalMap = studentRecordTotalList.stream().collect(Collectors.groupingBy(StudentRecordTotalDto::getSubjectSeqId));

            // subjectSeqList 对应一个课程下的很多个序号， 其中一个序号就对应一个sheet页
            for (SubjectTeacherDto dto : subjectSeqList) {
                // 一个课程序号下面的所有的学生健身记录，这里也就对应到一个sheet的数据
                List<StudentRecordTotalDto> seqStudentRecordList = studentRecordTotalMap.get(dto.getSubjectSeqId());
                if (CollectionUtils.isEmpty(seqStudentRecordList)) {
                    continue;
                }
                log.info("课程：{}-{}, 对应有{}条数据 ", dto.getSubjectName(), dto.getSeq(), seqStudentRecordList.size());
                // 每一个学生对应的多次成绩
                Map<Long, List<StudentRecordTotalDto>> seqStudentRecordMap = seqStudentRecordList.stream().collect(Collectors.groupingBy(StudentRecordTotalDto::getUserId));
                // 表头内容
                Map<String, String> titleData = new HashMap<>(4);
                titleData.put("number", dto.getNumber());
                titleData.put("subjectName", dto.getSubjectName());
                titleData.put("seq", dto.getSeq());
                titleData.put("teacherName", dto.getFullName());
                titleData.put("termName", term.getTermName());

                List<ExportStudentRecordTotalDto> totalDtoList = new ArrayList<>();
                seqStudentRecordMap.forEach((userId, studentRecordList) -> {
                    ExportStudentRecordTotalDto totalDto = new ExportStudentRecordTotalDto();

                    totalDto.setClassesName(studentRecordList.get(0).getClassesName());
                    totalDto.setScore(studentRecordList.stream().filter(e -> e.getCourseId() != null).count());
                    // 如果学生的及格次数大于10，就直接赋值为10分
                    if(totalDto.getScore() > 10){
                        totalDto.setScore(10);
                    }
                    totalDto.setFullName(studentRecordList.get(0).getFullName());
                    totalDto.setSno(studentRecordList.get(0).getSno());

                    int size = studentRecordList.size();
                    if (size >= 1) {
                        totalDto.setName1(studentRecordList.get(0).getCourseName());
                    }
                    if (size >= 2) {
                        totalDto.setName2(studentRecordList.get(1).getCourseName());
                    }
                    if (size >= 3) {
                        totalDto.setName3(studentRecordList.get(2).getCourseName());
                    }
                    if (size >= 4) {
                        totalDto.setName4(studentRecordList.get(3).getCourseName());
                    }
                    if (size >= 5) {
                        totalDto.setName5(studentRecordList.get(4).getCourseName());
                    }
                    if (size >= 6) {
                        totalDto.setName6(studentRecordList.get(5).getCourseName());
                    }
                    if (size >= 7) {
                        totalDto.setName7(studentRecordList.get(6).getCourseName());
                    }
                    if (size >= 8) {
                        totalDto.setName8(studentRecordList.get(7).getCourseName());
                    }
                    if (size >= 9) {
                        totalDto.setName9(studentRecordList.get(8).getCourseName());
                    }
                    if (size >= 10) {
                        totalDto.setName10(studentRecordList.get(9).getCourseName());
                    }

                    totalDtoList.add(totalDto);
                });

                // 基于模板导出Excel
                try {
                    String templateFileName = "ai_student_record_template.xlsx";

                    FileUtil.writeToLocal(templateFileName, (new DefaultResourceLoader()).getResource("classpath:" + templateFileName).getInputStream());
                    // 最后输出的文件名称
                    String fileName = allFileName + "/" + dto.getSubjectName() + "/" + dto.getSubjectName() + "-" + dto.getSeq() + "-" + dto.getFullName() + ".xlsx";

                    cn.hutool.core.io.FileUtil.mkdir(new File(allFileName + "/" + dto.getSubjectName()));

                    ExcelUtils.getInstance().exportObjects2Excel(templateFileName, 0, totalDtoList, titleData, ExportStudentRecordTotalDto.class, false, fileName);

                } catch (Excel4JException | IOException e) {
                    e.printStackTrace();
                }
            }

        });
        String zipFileName = allFileName + ".zip";
        ZipMultiFile.zipMultiFile(allFileName, zipFileName, true);
        // 删除文件夹
        FileUtil.delete(allFileName);

        return new AiSportsResponse<String>().success().data(ossUploadUtil.uploadToOss(new File(zipFileName)));
    }

    @Override
    public AiSportsResponse<String> exportStudentScore(@NotNull Long termId) {
        Term term = termService.getById(termId);
        if (term == null) {
            return new AiSportsResponse<String>().fail().message("没有查询到学期信息！");
        }

        // 先查询到所有的课程以及序号和老师
        List<SubjectTeacherDto> subjectTeacherList = subjectService.findSubjectTeacherDto();
        // 根据课程Id进行分组
        Map<Long, List<SubjectTeacherDto>> subjectTeacherMap = subjectTeacherList.stream().collect(Collectors.groupingBy(SubjectTeacherDto::getSubjectId));

        String allFileName = "平时成绩表(" + term.getTermName() + ")";

        subjectTeacherMap.forEach((subjectId, subjectSeqList) -> {
            // 根据课程Id去查询课程下面的学生所有合格的健身记录
            List<StudentRecordTotalDto> studentRecordTotalList = recordStudentService.findStudentRecordDetailBySubjectId(subjectId);
            // 按照课程序号去分组
            Map<Long, List<StudentRecordTotalDto>> studentRecordTotalMap = studentRecordTotalList.stream().collect(Collectors.groupingBy(StudentRecordTotalDto::getSubjectSeqId));

            // subjectSeqList 对应一个课程下的很多个序号， 其中一个序号就对应一个sheet页
            for (SubjectTeacherDto dto : subjectSeqList) {
                // 一个课程序号下面的所有的学生健身记录，这里也就对应到一个sheet的数据
                List<StudentRecordTotalDto> seqStudentRecordList = studentRecordTotalMap.get(dto.getSubjectSeqId());
                if (CollectionUtils.isEmpty(seqStudentRecordList)) {
                    continue;
                }
                log.info("课程：{}-{}, 对应有{}条数据 ", dto.getSubjectName(), dto.getSeq(), seqStudentRecordList.size());
                // 每一个学生对应的多次成绩
                Map<Long, List<StudentRecordTotalDto>> seqStudentRecordMap = seqStudentRecordList.stream().collect(Collectors.groupingBy(StudentRecordTotalDto::getUserId));
                // 表头内容
                Map<String, String> titleData = new HashMap<>(4);
                titleData.put("number", dto.getNumber());
                titleData.put("subjectName", dto.getSubjectName());
                titleData.put("seq", dto.getSeq());
                titleData.put("teacherName", dto.getFullName());
                titleData.put("termName", term.getTermName());

                List<ExportStudentScoreTotalDto> totalDtoList = new ArrayList<>();
                seqStudentRecordMap.forEach((userId, studentRecordList) -> {
                    ExportStudentScoreTotalDto totalDto = new ExportStudentScoreTotalDto();

                    totalDto.setClassesName(studentRecordList.get(0).getClassesName());
                    long score = studentRecordList.stream().filter(e -> e.getCourseId() != null).count();
                    totalDto.setAiScore(score * 0.5f);
                    totalDto.setSignedScore(score * 0.5f);
                    // 如果学生的及格次数大于10，就直接赋值为10分
                    if(score > 10){
                        totalDto.setAiScore(5f);
                        totalDto.setSignedScore(5f);
                    }
                    totalDto.setFullName(studentRecordList.get(0).getFullName());
                    totalDto.setSno(studentRecordList.get(0).getSno());
                    totalDto.setScore(totalDto.getAiScore() + totalDto.getSignedScore());

                    totalDtoList.add(totalDto);
                });

                // 基于模板导出Excel
                try {
                    String templateFileName = "ai_student_score_template.xlsx";

                    FileUtil.writeToLocal(templateFileName, (new DefaultResourceLoader()).getResource("classpath:" + templateFileName).getInputStream());
                    // 最后输出的文件名称
                    String fileName = allFileName + "/" + dto.getSubjectName() + "/" + dto.getSubjectName() + "-" + dto.getSeq() + "-" + dto.getFullName() + ".xlsx";

                    cn.hutool.core.io.FileUtil.mkdir(new File(allFileName + "/" + dto.getSubjectName()));

                    ExcelUtils.getInstance().exportObjects2Excel(templateFileName, 0, totalDtoList, titleData, ExportStudentScoreTotalDto.class, false, fileName);

                } catch (Excel4JException | IOException e) {
                    e.printStackTrace();
                }
            }

        });
        String zipFileName = allFileName + ".zip";
        ZipMultiFile.zipMultiFile(allFileName, zipFileName, true);
        // 删除文件夹
        FileUtil.delete(allFileName);

        return new AiSportsResponse<String>().success().data(ossUploadUtil.uploadToOss(new File(zipFileName)));
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
