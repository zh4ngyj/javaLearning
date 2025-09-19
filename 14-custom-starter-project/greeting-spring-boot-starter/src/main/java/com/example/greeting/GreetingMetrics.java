package com.example.greeting;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * @author: zh4ngyj
 * @date: 2025/8/14 18:05
 * @des: 问候服务指标收集器
 * 展示如何在Starter中集成Micrometer指标收集
 */
@Component
@ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
public class GreetingMetrics {

    private final Counter greetingCounter;
    private final Counter errorCounter;

    public GreetingMetrics(MeterRegistry meterRegistry) {
        this.greetingCounter = Counter.builder("greeting.requests.total")
                .description("Total number of greeting requests")
                .register(meterRegistry);
        
        this.errorCounter = Counter.builder("greeting.errors.total")
                .description("Total number of greeting errors")
                .register(meterRegistry);
    }

    /**
     * 记录问候请求
     */
    public void recordGreetingRequest() {
        greetingCounter.increment();
    }

    /**
     * 记录问候错误
     */
    public void recordGreetingError() {
        errorCounter.increment();
    }

    /**
     * 获取问候请求总数
     */
    public double getGreetingRequestCount() {
        return greetingCounter.count();
    }

    /**
     * 获取问候错误总数
     */
    public double getGreetingErrorCount() {
        return errorCounter.count();
    }
}
