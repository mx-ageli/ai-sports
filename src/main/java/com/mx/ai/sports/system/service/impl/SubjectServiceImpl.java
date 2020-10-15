package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.Subject;
import com.mx.ai.sports.system.mapper.SubjectMapper;
import com.mx.ai.sports.system.service.ISubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mengjiaxin
 * @date 2020/9/8 6:50 下午
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Subject> batchSubject(List<Subject> subjects) {
        // 先把老的数据查询出来
        List<Subject> oldSubjects = this.list();
        // 老数据的主课程名称
        List<String> subjectNames = oldSubjects.stream().map(Subject::getName).distinct().collect(Collectors.toList());
        // 先将老数据排除，不保存
        subjects = subjects.stream().filter(e -> !subjectNames.contains(e.getName())).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(subjects)) {
            saveBatch(subjects);

            oldSubjects.addAll(subjects);
        }

        return oldSubjects;
    }

}
