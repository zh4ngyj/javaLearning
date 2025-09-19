package com.example.greeting;

import org.springframework.stereotype.Component;

/**
 * @author: zh4ngyj
 * @date: 2025/8/14 18:05
 * @des: 问候服务健康检查指示器
 * 提供问候服务的健康状态检查，展示如何集成Spring Boot Actuator
 */
@Component
public class GreetingHealthIndicator {

    private final GreetingService greetingService;

    public GreetingHealthIndicator(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    /**
     * 检查问候服务是否健康
     * @return 健康状态信息
     */
    public String checkHealth() {
        try {
            // 测试问候服务是否正常工作
            String testMessage = greetingService.greet("HealthCheck");
            
            if (testMessage != null && !testMessage.isEmpty()) {
                return "UP - Greeting service is running normally. Test message: " + testMessage;
            } else {
                return "DOWN - Service returned empty response";
            }
        } catch (Exception e) {
            return "DOWN - Error: " + e.getMessage();
        }
    }

    /**
     * 获取服务状态
     * @return 服务状态
     */
    public boolean isHealthy() {
        try {
            String testMessage = greetingService.greet("HealthCheck");
            return testMessage != null && !testMessage.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
