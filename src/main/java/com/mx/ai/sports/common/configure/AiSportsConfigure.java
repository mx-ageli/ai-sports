package com.mx.ai.sports.common.configure;

//import com.alicp.jetcache.autoconfigure.JedisPoolFactory;
//import com.alicp.jetcache.autoconfigure.RedisAutoConfiguration;
import com.mx.ai.sports.common.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * aiSports 配置类
 * @author Mengjiaxin
 * @date 2019-08-20 16:12
 */
@Configuration
public class AiSportsConfigure {

    @Bean("ekbAsyncThreadPool")
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("Ekb-Async-Thread");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * XssFilter Bean
     */
    @Bean
    @SuppressWarnings("all")
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

//    @Bean(name = "defaultPool")
//    @DependsOn(RedisAutoConfiguration.AUTO_INIT_BEAN_NAME)//jetcache2.2+
//    public JedisPoolFactory defaultPool() {
//        return new JedisPoolFactory("remote.default", JedisPool.class);
//    }
}
