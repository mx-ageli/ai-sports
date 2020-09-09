package com.mx.ai.sports.system.converter;

import com.mx.ai.sports.system.entity.Replace;
import com.mx.ai.sports.system.vo.ReplaceVo;
import org.mapstruct.Mapper;



/**
 * ReplaceVO转换类
 *
 * @author Mengjiaxin
 * @date 2019-09-05 15:17
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReplaceConverter {


    /**
     *
     * @param replace
     * @return
     */
    ReplaceVo domain2Vo(Replace replace);



}
