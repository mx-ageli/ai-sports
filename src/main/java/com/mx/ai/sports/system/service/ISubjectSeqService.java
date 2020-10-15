package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.SubjectSeq;

import java.util.List;

/**
 * 主课程序号
 *
 * @author Mengjiaxin
 * @date 2020/10/14 5:23 下午
 */
public interface ISubjectSeqService extends IService<SubjectSeq> {

    List<SubjectSeq> batchSubjectSeq(List<SubjectSeq> subjectSeqs);

}
