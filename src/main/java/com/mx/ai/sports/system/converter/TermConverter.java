package com.mx.ai.sports.system.converter;

import com.mx.ai.sports.system.entity.Term;
import com.mx.ai.sports.system.vo.TermVo;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * TermVO转换类
 *
 * @author Mengjiaxin
 * @date 2019-09-05 15:17
 */
@Mapper(componentModel = "spring", uses = {})
public interface TermConverter {

    /**
     * 学期
     *
     * @param termList
     * @return
     */
    List<TermVo> domain2Vos(List<Term> termList);


}
