package com.mx.ai.sports.common.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * 项目基础配置
 * @author Mengjiaxin
 * @date 2019-08-20 16:23
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:ai-sports.properties"})
@ConfigurationProperties(prefix = "ai-sports")
public class AiSportsProperties {

    private ShiroProperties shiro = new ShiroProperties();
    private boolean openAopLog = true;
}
