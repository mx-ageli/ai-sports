package com.mx.ai.sports.common.entity;

/**
 * 项目的通用的常量
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
     * request 头信息
     */
    public static final String AUTH_HEADER = "Authorization";

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

    /**
     * 不需要token验证的路径
     */
    public static final String[] AUTH_PATH = {"api/user/v/"};

    /**
     * 多张图片的分隔符
     */
    public final static String IMG_SPLIT = ",";

    /**
     * json标识
     */
    public final static String JSON = "json";


    /**
     * 默认学校Id
     */
    public static final Long DEFAULT_SCHOOL_ID = 1L;
    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "/avatar/default.jpg";


}
