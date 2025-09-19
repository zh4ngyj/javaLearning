package com.example.greeting;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zh4ngyj
 * @date: 2025/8/14 18:05
 * @des: 问候服务Web控制器
 * 提供REST API接口，展示如何在Starter中集成Web功能
 */
@RestController
@RequestMapping("/api/greeting")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    /**
     * 获取问候语
     * @param name 要问候的人名
     * @return 问候语响应
     */
    @GetMapping("/{name}")
    public GreetingResponse greet(@PathVariable String name) {
        String message = greetingService.greet(name);
        return new GreetingResponse(message, name);
    }

    /**
     * 获取默认问候语
     * @return 默认问候语响应
     */
    @GetMapping
    public GreetingResponse greetDefault() {
        String message = greetingService.greet("World");
        return new GreetingResponse(message, "World");
    }

    /**
     * 问候语响应类
     */
    public static class GreetingResponse {
        private final String message;
        private final String name;
        private final long timestamp;

        public GreetingResponse(String message, String name) {
            this.message = message;
            this.name = name;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() {
            return message;
        }

        public String getName() {
            return name;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
