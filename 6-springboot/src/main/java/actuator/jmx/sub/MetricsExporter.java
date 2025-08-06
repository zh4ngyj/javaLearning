package actuator.jmx.sub;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 17:48
 * @des: 指标导出器 - 将JMX指标转换为Prometheus格式
 */
public class MetricsExporter {
    private final MBeanServer server;
    private final Map<String, Object> lastValues = new ConcurrentHashMap<>();

    public MetricsExporter() {
        this.server = ManagementFactory.getPlatformMBeanServer();
    }

    /**
     * 导出Prometheus格式的指标
     */
    public String exportMetrics() throws Exception {
        StringBuilder metrics = new StringBuilder();

        // JVM内存指标
        ObjectName memoryName = new ObjectName("java.lang:type=Memory");
        CompositeData heapMemory = (CompositeData) server.getAttribute(memoryName, "HeapMemoryUsage");

        long heapUsed = (Long) heapMemory.get("used");
        long heapMax = (Long) heapMemory.get("max");

        metrics.append("# HELP jvm_memory_heap_used_bytes Used heap memory\n");
        metrics.append("# TYPE jvm_memory_heap_used_bytes gauge\n");
        metrics.append(String.format("jvm_memory_heap_used_bytes %d\n", heapUsed));

        metrics.append("# HELP jvm_memory_heap_max_bytes Maximum heap memory\n");
        metrics.append("# TYPE jvm_memory_heap_max_bytes gauge\n");
        metrics.append(String.format("jvm_memory_heap_max_bytes %d\n", heapMax));

        // 检查内存使用率阈值
        double heapUsageRatio = (double) heapUsed / heapMax;
        if (heapUsageRatio > 0.9) {
            triggerAlert("MEMORY_HIGH", String.format("堆内存使用率过高: %.2f%%", heapUsageRatio * 100));
        }

        // 线程指标
        ObjectName threadName = new ObjectName("java.lang:type=Threading");
        int threadCount = (Integer) server.getAttribute(threadName, "ThreadCount");

        metrics.append("# HELP jvm_threads_current Current thread count\n");
        metrics.append("# TYPE jvm_threads_current gauge\n");
        metrics.append(String.format("jvm_threads_current %d\n", threadCount));

        // 自定义业务指标 (假设已注册BusinessMetrics MBean)
        try {
            ObjectName businessName = new ObjectName("com.example:type=BusinessMetrics");
            Long orderCount = (Long) server.getAttribute(businessName, "OrderProcessCount");
            Double avgResponseTime = (Double) server.getAttribute(businessName, "AverageResponseTime");

            metrics.append("# HELP business_orders_processed_total Total processed orders\n");
            metrics.append("# TYPE business_orders_processed_total counter\n");
            metrics.append(String.format("business_orders_processed_total %d\n", orderCount));

            metrics.append("# HELP business_response_time_avg_seconds Average response time\n");
            metrics.append("# TYPE business_response_time_avg_seconds gauge\n");
            metrics.append(String.format("business_response_time_avg_seconds %.3f\n", avgResponseTime / 1000.0));

            // 检查响应时间阈值
            if (avgResponseTime > 5000) { // 5秒
                triggerAlert("RESPONSE_TIME_HIGH", String.format("平均响应时间过高: %.2fms", avgResponseTime));
            }
        } catch (Exception e) {
            // BusinessMetrics MBean可能未注册，忽略错误
        }

        return metrics.toString();
    }

    private void triggerAlert(String alertType, String message) {
        System.err.println("🚨 ALERT [" + alertType + "]: " + message);
        // 这里可以集成实际的告警系统，如发送邮件、短信、钉钉通知等
    }

    /**
     * 启动指标收集服务 (模拟Prometheus抓取)
     */
    public void startMetricsServer() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                String metrics = exportMetrics();
                // 在实际应用中，这里会启动HTTP服务器暴露/metrics端点
                System.out.println("=== Metrics Export ===");
                System.out.println(metrics);
            } catch (Exception e) {
                System.err.println("指标导出失败: " + e.getMessage());
            }
        }, 0, 15, TimeUnit.SECONDS);
    }
}
