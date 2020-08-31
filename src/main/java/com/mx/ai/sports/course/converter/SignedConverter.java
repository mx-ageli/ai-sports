package com.mx.ai.sports.course.converter;

import com.mx.ai.sports.course.entity.Signed;
import com.mx.ai.sports.course.vo.SignedVo;
import org.mapstruct.Mapper;


/**
 * SignedVo转换类
 *
 * @author Mengjiaxin
 * @date 2019-09-05 15:17
 */
@Mapper(componentModel = "spring", uses = {})
public interface SignedConverter {


    /**
     * 转换为打卡对象
     *
     * @param signed
     * @return
     */
    SignedVo domain2Vo(Signed signed);


}
