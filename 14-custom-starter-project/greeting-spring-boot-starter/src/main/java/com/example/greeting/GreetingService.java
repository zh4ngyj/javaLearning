package com.example.greeting;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: zh4ngyj
 * @date: 2025/8/14 18:05
 * @des: 核心服务类
 * 核心服务类，提供生成问候语的功能。
 * 集成了指标收集和错误处理功能。
 */
public class GreetingService {

    private final GreetingProperties properties;
    
    @Autowired(required = false)
    private GreetingMetrics metrics;

    public GreetingService(GreetingProperties properties) {
        this.properties = properties;
    }

    /**
     * 根据配置的前缀和后缀生成问候语。
     * @param name 要问候的人名
     * @return 完整的问候语
     */
    public String greet(String name) {
        try {
            // 记录请求指标
            if (metrics != null) {
                metrics.recordGreetingRequest();
            }
            
            // 参数验证
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            
            // 生成问候语
            String greeting = properties.getPrefix() + name + properties.getSuffix();
            
            return greeting;
        } catch (Exception e) {
            // 记录错误指标
            if (metrics != null) {
                metrics.recordGreetingError();
            }
            throw e;
        }
    }

    /**
     * 获取服务状态信息
     * @return 服务状态
     */
    public String getServiceStatus() {
        return String.format("GreetingService is running with prefix='%s', suffix='%s', language='%s'", 
                properties.getPrefix(), properties.getSuffix(), properties.getLanguage());
    }
}
