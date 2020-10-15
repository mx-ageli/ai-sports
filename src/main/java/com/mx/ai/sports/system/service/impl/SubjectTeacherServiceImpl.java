package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.SubjectTeacher;
import com.mx.ai.sports.system.mapper.SubjectTeacherMapper;
import com.mx.ai.sports.system.service.ISubjectTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author Mengjiaxin
 * @date 2020/9/8 6:50 下午
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SubjectTeacherServiceImpl extends ServiceImpl<SubjectTeacherMapper, SubjectTeacher> implements ISubjectTeacherService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SubjectTeacher> batchSubjectTeacher(List<SubjectTeacher> subjectTeachers) {
        // 先把老的数据查询出来
        List<SubjectTeacher> oldSubjectTeachers = this.list();

        for (SubjectTeacher oldTeacher : oldSubjectTeachers) {
            subjectTeachers.removeIf(subjectTeacher -> Objects.equals(oldTeacher.getSubjectId(), subjectTeacher.getSubjectId())
                    && Objects.equals(oldTeacher.getSubjectSeqId(), subjectTeacher.getSubjectSeqId())
                    && Objects.equals(oldTeacher.getUserId(), subjectTeacher.getUserId()));
        }

        if (!CollectionUtils.isEmpty(subjectTeachers)) {
            saveBatch(subjectTeachers);

            oldSubjectTeachers.addAll(subjectTeachers);
        }

        return oldSubjectTeachers;
    }
}
