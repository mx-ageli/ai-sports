package com.mx.ai.sports;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * AI体育项目入口
 *
 * @author Mengjiaxin
 * @date 2020/7/14 3:49 下午
 */
@SpringBootApplication
@EnableAsync
@EnableSwagger2
@EnableTransactionManagement
@MapperScan("com.mx.ai.sports.*.mapper")
public class AiSportsApplication {

    public static void main(String[] args) {

        new SpringApplicationBuilder(AiSportsApplication.class).run(args);
    }
}
