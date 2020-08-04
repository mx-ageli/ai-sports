package com.mx.ai.sports.system.converter;

import com.mx.ai.sports.system.dto.UserSimple;
import com.mx.ai.sports.system.entity.User;
import org.mapstruct.Mapper;


/**
 * UserVO转换类
 *
 * @author Mengjiaxin
 * @date 2019-09-05 15:17
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserConverter {

    /**
     * 转换为用户简单对象
     *
     * @param user
     * @return
     */
    UserSimple domain2Simple(User user);

}
