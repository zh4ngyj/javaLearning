package com.example.app;

import com.example.greeting.GreetingService;
import com.example.greeting.GreetingProperties;
import com.example.greeting.GreetingHealthIndicator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 问候服务演示应用
 * 展示自定义Spring Boot Starter的各种功能
 */
@SpringBootApplication
public class GreetingApplication
{
    public static void main(String[] args) {
        SpringApplication.run(GreetingApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(GreetingService greetingService, GreetingProperties properties, GreetingHealthIndicator healthIndicator) {
        return args -> {
            System.out.println("=========================================");
            System.out.println("🎉 自定义Spring Boot Starter演示");
            System.out.println("=========================================");
            
            // 显示配置信息
            System.out.println("📋 当前配置:");
            System.out.println("   - 前缀: " + properties.getPrefix());
            System.out.println("   - 后缀: " + properties.getSuffix());
            System.out.println("   - 语言: " + properties.getLanguage());
            System.out.println("   - 启用状态: " + properties.isEnabled());
            System.out.println("   - Web端点: " + properties.isWebEnabled());
            System.out.println("   - 健康检查: " + properties.isHealthEnabled());
            System.out.println("   - 缓存: " + properties.isCacheEnabled());
            
            // 健康检查
            System.out.println("\n🏥 健康检查:");
            System.out.println("   - 状态: " + (healthIndicator.isHealthy() ? "健康" : "异常"));
            System.out.println("   - 详情: " + healthIndicator.checkHealth());
            
            // 测试问候服务
            System.out.println("\n💬 问候服务测试:");
            String message1 = greetingService.greet("SpringBoot 2.6.7");
            String message2 = greetingService.greet("自定义Starter");
            String message3 = greetingService.greet("开发者");
            
            System.out.println("   - " + message1);
            System.out.println("   - " + message2);
            System.out.println("   - " + message3);
            
            System.out.println("\n🌐 可用的端点:");
            System.out.println("   - GET /api/greeting/{name} - 获取问候语");
            System.out.println("   - GET /api/greeting - 获取默认问候语");
            System.out.println("   - GET /actuator/health - 健康检查");
            System.out.println("   - GET /actuator/info - 应用信息");
            
            System.out.println("\n🚀 应用启动完成！访问 http://localhost:8080 查看效果");
            System.out.println("=========================================");
        };
    }
}
