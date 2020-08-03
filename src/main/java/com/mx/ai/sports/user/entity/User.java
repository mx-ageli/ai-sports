package com.mx.ai.sports.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:47
 */
@Data
@TableName("sys_user")
@Excel("用户信息表")
public class User implements Serializable {

    private static final long serialVersionUID = -4352868070794165001L;

    /**
     * 用户状态：有效
     */
    public static final String STATUS_VALID = "1";
    /**
     * 用户状态：锁定
     */
    public static final String STATUS_LOCK = "0";
    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "/avatar/default.jpg";
    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";
    /**
     * 性别男
     */
    public static final String SEX_MALE = "0";
    /**
     * 性别女
     */
    public static final String SEX_FEMALE = "1";
    /**
     * 性别保密
     */
    public static final String SEX_UNKNOW = "2";
    /**
     * 黑色主题
     */
    public static final String THEME_BLACK = "black";
    /**
     * 白色主题
     */
    public static final String THEME_WHITE = "white";
    /**
     * TAB开启
     */
    public static final String TAB_OPEN = "1";
    /**
     * TAB关闭
     */
    public static final String TAB_CLOSE = "0";
    /**
     * 学生组
     */
    public static final Long DEPT_STUDENT = 3L;

    /**
     * 老师组
     */
    public static final Long DEPT_TEACHER = 2L;







    /**
     * 用户 ID
     */
    @TableId(value = "USER_ID", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    @Size(min = 1, max = 20, message = "{range}")
    @ExcelField(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    @TableField("PASSWORD")
    private String password;

    /**
     * 用户组 ID
     */
    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 邮箱
     */
    @TableField("EMAIL")
    @Size(max = 50, message = "{noMoreThan}")
    @Email(message = "{email}")
    @ExcelField(value = "邮箱")
    private String email;

    /**
     * 联系电话
     */
    @TableField("MOBILE")
    @ExcelField(value = "联系电话")
    private String mobile;

    /**
     * 状态 0锁定 1有效
     */
    @TableField("STATUS")
    @NotBlank(message = "{required}")
    @ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效")
    private String status;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private Date modifyTime;

    /**
     * 最近访问时间
     */
    @TableField("LAST_LOGIN_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 性别 0男 1女 2 保密
     */
    @TableField("SSEX")
    @NotBlank(message = "{required}")
    @ExcelField(value = "性别", writeConverterExp = "0=男,1=女,2=保密")
    private String sex;

    /**
     * 头像
     */
    @TableField("AVATAR")
    private String avatar;

    /**
     * 主题
     */
    @TableField("THEME")
    private String theme;

    /**
     * 是否开启 tab 0开启，1关闭
     */
    @TableField("IS_TAB")
    private String isTab;

    /**
     * 描述
     */
    @TableField("REMARK")
    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "个人描述")
    private String remark;

    /**
     * 姓名
     */
    @TableField("NICKNAME")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "姓名")
    private String nickname;

    /**
     * 设备Id
     */
    @TableField("DEVICE_ID")
    private String deviceId;

    /**
     * 用户组名称
     */
    @ExcelField(value = "用户组")
    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String createTimeFrom;
    @TableField(exist = false)
    private String createTimeTo;
    /**
     * 角色 ID
     */
    @NotBlank(message = "{required}")
    @TableField(exist = false)
    private String roleId;

    @ExcelField(value = "角色")
    @TableField(exist = false)
    private String roleName;

    public Long getId() {
        return userId;
    }


    /**
     * 身份证号码
     */
    @TableField("ID_NUMBER")
    private String idNumber;

    /**
     * 详细地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 省份Id
     */
    @TableField("PROVINCE_ID")
    private Long provinceId;


    /**
     * 城市Id
     */
    @TableField("CITY_ID")
    private Long cityId;

    /**
     * 区域Id
     */
    @TableField("REGION_ID")
    private Long regionId;

}
