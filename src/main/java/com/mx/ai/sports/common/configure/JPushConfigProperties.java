package com.mx.ai.sports.common.configure;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 极光推送配置
 * @author Mengjiaxin
 * @date 2020/5/25 5:47 下午
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "jpush")
public class JPushConfigProperties implements Serializable {

    private static final long serialVersionUID = -3276550737021748435L;

    private String masterSecret;

    private String appKey;

    private String title;

    private String largeIcon;



}
