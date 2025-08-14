package com.example.greeting;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zh4ngyj
 * @date: 2025/8/14 18:05
 * @des: 自动配置类
 * 自动配置类。
 * 当这个 starter 在 classpath 中时，Spring Boot 会加载这个类。
 */
@Configuration
@EnableConfigurationProperties(GreetingProperties.class) // 启用 GreetingProperties
public class GreetingAutoConfiguration {

    private final GreetingProperties properties;

    public GreetingAutoConfiguration(GreetingProperties properties) {
        this.properties = properties;
    }

    /**
     * 创建 GreetingService 的 Bean。
     * @ConditionalOnMissingBean 确保只有当容器中不存在 GreetingService 类型的 Bean 时，才会创建此 Bean。
     * 这允许用户通过自己定义一个 GreetingService Bean 来覆盖我们的默认实现。
     * @return GreetingService 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public GreetingService greetingService() {
        return new GreetingService(properties);
    }
}
