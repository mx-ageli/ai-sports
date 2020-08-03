package com.mx.ai.sports.user.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息简单对象
 *
 * @author Mengjiaxin
 * @date 2019/11/28 3:33 下午
 */
@Data
public class UserSimple implements Serializable {


    private static final long serialVersionUID = -5483101195340644647L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 账号
     */
    private String username;

    /**
     * 用户组
     */
    private Long deptId;

    /**
     * 姓名
     */
    private String nickname;

    /**
     * 角色
     */
    private String roleId;
}
