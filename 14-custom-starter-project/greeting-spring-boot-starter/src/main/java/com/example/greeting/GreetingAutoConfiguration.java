package com.example.greeting;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * @author: zh4ngyj
 * @date: 2025/8/14 18:05
 * @des: 自动配置类
 * 自动配置类，展示各种条件注解的使用。
 * 当这个 starter 在 classpath 中时，Spring Boot 会加载这个类。
 */
@Configuration
@EnableConfigurationProperties(GreetingProperties.class)
@ConditionalOnProperty(prefix = "greeting", name = "enabled", havingValue = "true", matchIfMissing = true)
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

    /**
     * 创建健康检查指示器
     * 只有在启用健康检查且存在GreetingService时才创建
     */
    @Bean
    @ConditionalOnProperty(prefix = "greeting", name = "health-enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(GreetingHealthIndicator.class)
    public GreetingHealthIndicator greetingHealthIndicator(GreetingService greetingService) {
        return new GreetingHealthIndicator(greetingService);
    }

    /**
     * 创建Web控制器
     * 只有在Web应用且启用Web端点时才创建
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(prefix = "greeting", name = "web-enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(GreetingController.class)
    public GreetingController greetingController(GreetingService greetingService) {
        return new GreetingController(greetingService);
    }

    /**
     * 创建指标收集器
     * 只有在存在MeterRegistry时才创建
     */
    @Bean
    @ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
    @ConditionalOnMissingBean(GreetingMetrics.class)
    public GreetingMetrics greetingMetrics(MeterRegistry meterRegistry) {
        return new GreetingMetrics(meterRegistry);
    }
}
