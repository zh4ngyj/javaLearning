package com.example.greeting;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: zh4ngyj
 * @date: 2025/8/14 18:05
 * @des: 配置属性类
 * 配置属性类，用于接收来自 application.properties 的配置。
 * 前缀 "greeting" 表示所有相关配置都应以 "greeting." 开头。
 */
@ConfigurationProperties(prefix = "greeting")
public class GreetingProperties {

    /**
     * 问候语的前缀，默认为 "Hello, "
     */
    private String prefix = "Hello, ";

    /**
     * 问候语的后缀，默认为 "!"
     */
    private String suffix = "!";

    // --- Getters and Setters ---
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
