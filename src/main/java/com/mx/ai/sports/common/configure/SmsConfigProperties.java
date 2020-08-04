package com.mx.ai.sports.common.configure;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 短信发送配置
 *
 * @author Mengjiaxin
 * @date 2019-08-15
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "sms")
public class SmsConfigProperties implements Serializable {


    private String regionId;

    private String accessKeyId;

    private String accessKeySecret;

    private String signName;

    private String templateCode;


}
