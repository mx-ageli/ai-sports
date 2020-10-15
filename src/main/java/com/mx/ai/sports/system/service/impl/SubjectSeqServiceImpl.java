package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.SubjectSeq;
import com.mx.ai.sports.system.mapper.SubjectSeqMapper;
import com.mx.ai.sports.system.service.ISubjectSeqService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class SubjectSeqServiceImpl extends ServiceImpl<SubjectSeqMapper, SubjectSeq> implements ISubjectSeqService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SubjectSeq> batchSubjectSeq(List<SubjectSeq> subjectSeqs) {
        // 先把老的数据查询出来
        List<SubjectSeq> oldSubjectSeqs = this.list();

        for (SubjectSeq oldSubject : oldSubjectSeqs) {
            subjectSeqs.removeIf(subjectSeq -> StringUtils.equals(oldSubject.getSeq(), subjectSeq.getSeq()) && Objects.equals(oldSubject.getSubjectId(), subjectSeq.getSubjectId()));
        }

        if (!CollectionUtils.isEmpty(subjectSeqs)) {
            saveBatch(subjectSeqs);

            oldSubjectSeqs.addAll(subjectSeqs);
        }

        return oldSubjectSeqs;
    }
}
