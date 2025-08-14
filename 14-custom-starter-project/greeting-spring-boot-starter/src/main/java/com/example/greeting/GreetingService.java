package com.example.greeting;

/**
 * @author: zh4ngyj
 * @date: 2025/8/14 18:05
 * @des: 核心服务类
 * 核心服务类，提供生成问候语的功能。
 */
public class GreetingService {

    private final GreetingProperties properties;

    public GreetingService(GreetingProperties properties) {
        this.properties = properties;
    }

    /**
     * 根据配置的-前缀和后缀生成问候语。
     * @param name 要问候的人名
     * @return 完整的问候语
     */
    public String greet(String name) {
        return properties.getPrefix() + name + properties.getSuffix();
    }
}
