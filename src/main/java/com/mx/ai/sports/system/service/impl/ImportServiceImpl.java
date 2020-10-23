package com.mx.ai.sports.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.crab2died.ExcelUtils;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.entity.RoleEnum;
import com.mx.ai.sports.common.entity.SexEnum;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.oss.AliyunOssConfig;
import com.mx.ai.sports.common.utils.FileUtil;
import com.mx.ai.sports.system.dto.ImportStudentDto;
import com.mx.ai.sports.system.entity.*;
import com.mx.ai.sports.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mx.ai.sports.common.entity.AiSportsConstant.DEFAULT_LOGO;

/**
 * @author Mengjiaxin
 * @date 2020/9/8 6:50 下午
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ImportServiceImpl implements IImportService {

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private ISubjectSeqService subjectSeqService;

    @Autowired
    private ISubjectTeacherService subjectTeacherService;

    @Autowired
    private IClassesService classesService;

    @Autowired
    private IClassesTeacherService classesTeacherService;

    @Autowired
    private ITempStudentService tempStudentService;

    @Autowired
    private AliyunOssConfig aliyunOssConfig;

    @Autowired
    private IUserService userService;

    private List<String> teacherHeaderList = Arrays.asList("任课教师", "手机号码");

    /**
     * 将导入的教师数据转换为教师注册表数据
     *
     * @param file
     * @return
     * @throws AiSportsException
     * @throws IOException
     */
    @Override
    public List<TeacherRegister> getImportTeacherByFile(MultipartFile file) throws AiSportsException, IOException {
        List<TeacherRegister> importTeacherRegister = new ArrayList<>();
        // 将excel文件解析为data
        List<List<String>> readExcelList = getExcelList(file);

        // 取出第一行的表头
        List<String> headers = readExcelList.get(0);
        log.info("开始解析excel的表头！上传的表头：{}", JSON.toJSONString(headers));
        // 表头对应出错
        for (int i = 0; i < headers.size(); i++) {
            if (!Objects.equals(headers.get(i), teacherHeaderList.get(i))) {
                throw new AiSportsException("导入失败!表头[" + headers.get(i) + "]对应错误！表头应该为：" + JSON.toJSONString(teacherHeaderList));
            }
        }
        // 先将第一行表头删除
        readExcelList.remove(0);

        log.info("开始解析excel内容！一共需要转换{}条", readExcelList.size());
        for (List<String> readExcel : readExcelList) {
            TeacherRegister teacherRegister = new TeacherRegister();
            teacherRegister.setFullName(readExcel.get(0).trim());
            teacherRegister.setUsername(readExcel.get(1).trim());
            teacherRegister.setIsRegister(false);
            teacherRegister.setSchoolId(AiSportsConstant.SCHOOL_ID);

            importTeacherRegister.add(teacherRegister);
        }
        log.info("excel内容解析完成！一共完成转换{}条", importTeacherRegister.size());

        return importTeacherRegister;
    }

    /**
     * 将excel文件解析为data
     *
     * @param file
     * @return
     * @throws AiSportsException
     * @throws IOException
     */
    private List<List<String>> getExcelList(MultipartFile file) throws AiSportsException, IOException {
        if (file == null) {
            throw new AiSportsException("导入失败!文件不能为空！");
        }

        String fileSize = FileUtil.getFileSizeStr(file.getSize());
        log.info("文件上传成功！文件名称：{}, 文件大小：{}。开始解析excel文件...", file.getName(), fileSize);

        List<List<String>> readExcelList = ExcelUtils.getInstance().readExcel2List(file.getInputStream());
        log.info("excel文件解析成功！数据行数：{}", readExcelList.size());

        if (CollectionUtils.isEmpty(readExcelList)) {
            throw new AiSportsException("导入失败!文件不能为空！");
        }
        return readExcelList;
    }


    private List<String> studentHeaderList = Arrays.asList("课程号", "课程名称", "课序号", "班级", "学号", "姓名", "性别", "任课教师");

    /**
     * 导入学生基础信息-解析为学生导入模型
     *
     * @param file
     * @return
     * @throws AiSportsException
     * @throws IOException
     */
    @Override
    public List<ImportStudentDto> getImportStudentDtoByFile(MultipartFile file) throws AiSportsException, IOException {
        List<ImportStudentDto> importStudentDtos = new ArrayList<>();

        List<List<String>> readExcelList = getExcelList(file);
        // 取出第一行的表头
        List<String> headers = readExcelList.get(0);

        log.info("开始解析excel的表头！上传的表头：{}", JSON.toJSONString(headers));
        // 表头对应出错
        for (int i = 0; i < headers.size(); i++) {
            if (!Objects.equals(headers.get(i), studentHeaderList.get(i))) {
                throw new AiSportsException("导入失败!表头[" + headers.get(i) + "]对应错误！表头应该为：" + JSON.toJSONString(studentHeaderList));
            }
        }
        // 先将第一行表头删除
        readExcelList.remove(0);

        log.info("开始解析excel内容！一共需要转换{}条", readExcelList.size());
        for (List<String> readExcel : readExcelList) {
            ImportStudentDto dto = new ImportStudentDto();
            dto.setSubjectNo(readExcel.get(0).trim());
            dto.setSubjectName(readExcel.get(1).trim());
            dto.setSubjectSeq(readExcel.get(2).trim().length() == 1 ? "0" + readExcel.get(2).trim() : readExcel.get(2).trim());
            dto.setClassesName(readExcel.get(3).trim());
            dto.setSno(readExcel.get(4).trim());
            dto.setFullName(readExcel.get(5).trim());
            dto.setSex(getSex(readExcel.get(6).trim()));
            dto.setTeacherName(readExcel.get(7).replace("*", "").trim());
            importStudentDtos.add(dto);
        }
        log.info("excel内容解析完成！一共完成转换{}条", importStudentDtos.size());

        return importStudentDtos;
    }

    private String getSex(String sexStr) {
        if (SexEnum.MALE.getReasonPhrase().equals(sexStr)) {
            return SexEnum.MALE.value();
        } else if (SexEnum.FEMALE.getReasonPhrase().equals(sexStr)) {
            return SexEnum.FEMALE.value();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchImportStudentToDb(List<ImportStudentDto> importTeacherDtos) throws AiSportsException {
        // 1、统计出所有的主课程
        List<Subject> subjects = importTeacherDtos.stream().map(e -> new Subject(e.getSubjectName(), e.getSubjectNo())).distinct().collect(Collectors.toList());
        log.info("excel中解析出的主课数量：{}", subjects.size());

        // 批量的保存主课程,返回现在已有的所有的主课程
        List<Subject> allSubjectList = subjectService.batchSubject(subjects);
        log.info("批量保存主课程，数据库中一共返回：{}条", allSubjectList.size());
        // 主课程名称对应的课程Id
        Map<String, Long> subjectMap = allSubjectList.stream().collect(Collectors.toMap(Subject::getName, Subject::getSubjectId));

        // 2、统计出所有的主课程序号
        List<SubjectSeq> subjectSeqs = importTeacherDtos.stream().map(e -> {
            SubjectSeq subjectSeq = new SubjectSeq();
            subjectSeq.setSubjectId(subjectMap.get(e.getSubjectName()));
            subjectSeq.setSeq(e.getSubjectSeq());
            return subjectSeq;
        }).distinct().collect(Collectors.toList());
        log.info("excel中解析出的主课序号数量：{}", subjectSeqs.size());
        // 批量的保存主课程序号,返回现在已有的所有的主课程序号
        List<SubjectSeq> allSubjectSeqs = subjectSeqService.batchSubjectSeq(subjectSeqs);
        log.info("批量保存主课序号，数据库中一共返回：{}条", allSubjectSeqs.size());

        Map<Long, Map<String, Long>> subjectSeqMap = allSubjectSeqs.stream().collect(Collectors.groupingBy(SubjectSeq::getSubjectId,
                Collectors.toMap(SubjectSeq::getSeq, SubjectSeq::getSubjectSeqId, (e1, e2) -> e1)));

        // 3、统计出所有的老师
        List<User> teachers = userService.findByRole(RoleEnum.TEACHER.value());
        Map<String, Long> teacherMap = teachers.stream().collect(Collectors.toMap(User::getFullName, User::getUserId));

        List<SubjectTeacher> subjectTeachers = importTeacherDtos.stream().map(e -> {
            Long subjectId = subjectMap.get(e.getSubjectName());

            SubjectTeacher subjectTeacher = new SubjectTeacher();
            subjectTeacher.setSubjectId(subjectId);
            subjectTeacher.setSubjectSeqId(subjectSeqMap.get(subjectId).get(e.getSubjectSeq()));
            subjectTeacher.setUserId(teacherMap.get(e.getTeacherName()));

            return subjectTeacher;
        }).distinct().collect(Collectors.toList());
        log.info("excel中解析出的老师对应主课程序号数量：{}", subjectTeachers.size());
        // 批量的保存老师对应主课程序号,返回现在已有的所有的老师对应主课程序号
        List<SubjectTeacher> allSubjectTeachers = subjectTeacherService.batchSubjectTeacher(subjectTeachers);
        log.info("批量保存主课序号老师对应，数据库中一共返回：{}条", allSubjectTeachers.size());

        // 4、统计出所有的班级
        List<Classes> classesList = importTeacherDtos.stream().map(e -> {
            Classes classes = new Classes();
            classes.setClassesName(e.getClassesName().trim());
            classes.setSchoolId(AiSportsConstant.SCHOOL_ID);
            classes.setAvatar(aliyunOssConfig.getAccessUrl() + DEFAULT_LOGO);

            return classes;
        }).distinct().collect(Collectors.toList());
        log.info("excel中解析出的班级数量：{}", classesList.size());
        // 批量的保存老师对应,返回现在已有的所有的老师对应主课程序号
        List<Classes> allClassesList = classesService.batchClasses(classesList);
        log.info("批量保存班级信息，数据库中一共返回：{}条", allClassesList.size());

        // 5、统计出所有的班级和老师的关系
        Map<String, Long> classesMap = allClassesList.stream().collect(Collectors.toMap(Classes::getClassesName, Classes::getClassesId));
        List<ClassesTeacher> classesTeachers = importTeacherDtos.stream().map(e -> {
            ClassesTeacher classesTeacher = new ClassesTeacher();
            classesTeacher.setClassesId(classesMap.get(e.getClassesName()));
            classesTeacher.setUserId(teacherMap.get(e.getTeacherName()));

            return classesTeacher;
        }).distinct().collect(Collectors.toList());
        log.info("excel中解析出的班级老师关系数量：{}", classesTeachers.size());
        // 批量的保存老师对应,返回现在已有的所有的老师对应主课程序号
        List<ClassesTeacher> allClassesTeachers = classesTeacherService.batchClassesTeacher(classesTeachers);
        log.info("批量保存班级老师关系，数据库中一共返回：{}条", allClassesTeachers.size());

        // 6、组装保存所有的临时学生基础信息
        List<TempStudent> tempStudents = importTeacherDtos.stream().map(e -> {
            TempStudent tempStudent = new TempStudent();
            tempStudent.setFullName(e.getFullName());
            tempStudent.setSex(e.getSex());
            tempStudent.setSno(e.getSno());
            tempStudent.setSchoolId(AiSportsConstant.SCHOOL_ID);
            tempStudent.setClassesId(classesMap.get(e.getClassesName()));
            Long subjectId = subjectMap.get(e.getSubjectName());
            tempStudent.setSubjectId(subjectId);
            tempStudent.setSubjectSeqId(subjectSeqMap.get(subjectId).get(e.getSubjectSeq()));
            tempStudent.setTeacherId(teacherMap.get(e.getTeacherName()));
            // 默认为未注册
            tempStudent.setIsRegister(false);
            return tempStudent;
        }).distinct().collect(Collectors.toList());
        log.info("excel中解析出的学生信息数量：{}", tempStudents.size());
        // 保存最终的学生数据
        Boolean success = tempStudentService.batchTempStudent(tempStudents);
        log.info("批量保存学生临时基础信息，一共：{}条", tempStudents.size());
        return success;
    }
}
