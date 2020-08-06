package com.mx.ai.sports.system.converter;

import com.mx.ai.sports.system.vo.ClassesVo;
import com.mx.ai.sports.system.vo.UserSimple;
import com.mx.ai.sports.system.vo.UserVo;
import com.mx.ai.sports.system.entity.Classes;
import com.mx.ai.sports.system.entity.User;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * ClassesVO转换类
 *
 * @author Mengjiaxin
 * @date 2019-09-05 15:17
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClassesConverter {


    /**
     * 转换为班级对象
     *
     * @param classes
     * @return
     */
    ClassesVo domain2Vo(Classes classes);

    /**
     * 转换为班级对象
     *
     * @param classes
     * @return
     */
    List<ClassesVo> domain2Vos(List<Classes> classes);



}
