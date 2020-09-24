package com.mx.ai.sports.app.controller;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.sheet.wrapper.NoTemplateSheetWrapper;
import com.mx.ai.sports.app.api.ExportApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.oss.OssUploadUtil;
import com.mx.ai.sports.course.dto.ExportRecordStudentDto;
import com.mx.ai.sports.course.dto.ExportRecordTotalDto;
import com.mx.ai.sports.course.query.ExportAllQuery;
import com.mx.ai.sports.course.service.ICourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
