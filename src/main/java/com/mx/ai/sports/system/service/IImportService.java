package com.mx.ai.sports.system.service;

import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.system.dto.ImportStudentDto;
import com.mx.ai.sports.system.entity.Subject;
import com.mx.ai.sports.system.entity.TeacherRegister;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 导入excel服务
 *
 * @author Mengjiaxin
 * @date 2020/10/14 5:28 下午
 */
public interface IImportService {


    /**
     * 将导入的教师数据转换为教师注册表数据
     *
     * @param file
     * @return
     * @throws AiSportsException
     * @throws IOException
     */
    List<TeacherRegister> getImportTeacherByFile(MultipartFile file) throws AiSportsException, IOException;

    /**
     * 导入学生基础信息-解析为学生导入模型
     *
     * @param file
     * @return
     * @throws AiSportsException
     * @throws IOException
     */
    List<ImportStudentDto> getImportStudentDtoByFile(MultipartFile file) throws AiSportsException, IOException;

    /**
     * 1、初始化主课程数据
     * 2、初始化所有的主课程序号
     * 3、初始化所有的老师
     * 4、初始化所有的班级
     * 5、初始化所有的班级和老师的关系
     * 6、初始化所有的临时学生基础信息
     *
     * @param importTeacherDtos
     * @return
     */
    Boolean batchImportStudentToDb(List<ImportStudentDto> importTeacherDtos) throws AiSportsException;
}
