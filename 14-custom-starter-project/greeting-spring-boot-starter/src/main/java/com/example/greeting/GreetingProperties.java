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

    /**
     * 是否启用问候服务，默认为 true
     */
    private boolean enabled = true;

    /**
     * 是否启用Web端点，默认为 true
     */
    private boolean webEnabled = true;

    /**
     * 是否启用健康检查，默认为 true
     */
    private boolean healthEnabled = true;

    /**
     * 问候语的语言，默认为 "en"
     */
    private String language = "en";

    /**
     * 是否启用缓存，默认为 false
     */
    private boolean cacheEnabled = false;

    /**
     * 缓存过期时间（秒），默认为 300
     */
    private int cacheExpireSeconds = 300;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isWebEnabled() {
        return webEnabled;
    }

    public void setWebEnabled(boolean webEnabled) {
        this.webEnabled = webEnabled;
    }

    public boolean isHealthEnabled() {
        return healthEnabled;
    }

    public void setHealthEnabled(boolean healthEnabled) {
        this.healthEnabled = healthEnabled;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public int getCacheExpireSeconds() {
        return cacheExpireSeconds;
    }

    public void setCacheExpireSeconds(int cacheExpireSeconds) {
        this.cacheExpireSeconds = cacheExpireSeconds;
    }
}
