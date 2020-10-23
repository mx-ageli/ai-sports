package com.mx.ai.sports.common.entity;

import java.time.LocalTime;

/**
 * 项目的通用的常量
 *
 * @author Mengjiaxin
 * @date 2020/7/14 4:00 下午
 */
public class AiSportsConstant {

    /**
     * 排序规则：降序
     */
    public static final String ORDER_DESC = "desc";
    /**
     * 排序规则：升序
     */
    public static final String ORDER_ASC = "asc";

    /**
     * 验证码 Session Key
     */
    public static final String CODE_PREFIX = "sports_captcha_";

    /**
     * 允许下载的文件类型，根据需求自己添加（小写）
     */
    public static final String[] VALID_FILE_TYPE = {"xlsx", "zip"};

    /**
     * 上传的图片类型
     */
    public static final String[] IMAGE_FILE_TYPE = {"png", "jpg", "jpeg"};

    /**
     * 星期范围
     */
    public static final String[] WEEK = {"1", "2", "3", "4", "5", "6", "7"};


    /**
     * request 头信息
     */
    public static final String AUTH_HEADER = "Authorization";

    /**
     * 设置Id
     */
    public static final String DEVICE_ID = "deviceId";

    /**
     * 设备类型1iOS 2Android
     */
    public static final String DEVICE_TYPE = "deviceType";

    /**
     * 秘钥
     */
    public static final String SECRET = "defaultSecret";

    /**
     * token的过期时间 一天:86400 默认设置为60天
     * 86400 * 60L * 1000
     */
    public static final Long EXPIRATION = 86400 * 60L * 1000;

    /**
     * 短信验证码标识
     */
    public final static String CODE_DATE_OUT = "CODE_DATE_OUT";

    /**
     * 重复获取短信验证码的间隔时间
     */
    public final static Integer CODE_DATE_OUT_VALUE = 60;

    /**
     * 短信验证码的过期时间 默认为五分钟
     */
    public final static Integer CODE_EXPIRE_TIME = 5 * 60;
//    public final static Integer CODE_EXPIRE_TIME = 60 * 60 * 24 * 7;

    /**
     * 不需要token验证的路径
     */
    public static final String[] AUTH_PATH = {"api/user/v/", "api/monitor/v/", "api/replace/v/"};

    /**
     * 逗号分隔符
     */
    public final static String SPLIT = ",";

    /**
     * json标识
     */
    public final static String JSON = "json";

    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "/avatar/default.jpg";

    /**
     * 项目logo
     */
    public static final String DEFAULT_LOGO = "/avatar/logo.png";

    /**
     * 空格
     */
    public static final String SPACE = " ";

    /**
     * 默认的学校Id
     */
    public static final Long SCHOOL_ID = 1L;

    /**
     * 课程的开始预约时间
     */
    public static final String ENTRY_START_TIME = "08:00";

    /**
     * 课程的结束预约时间
     */
    public static final String ENTRY_END_TIME = "16:00";

}
