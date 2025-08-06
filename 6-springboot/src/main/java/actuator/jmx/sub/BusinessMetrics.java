package actuator.jmx.sub;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 17:40
 * @des: 自定义业务指标MBean - 监控业务相关指标
 */
public class BusinessMetrics implements BusinessMetricsMBean {
    private volatile long orderProcessCount = 0;
    private volatile long totalResponseTime = 0;
    private volatile int requestCount = 0;
    private volatile int activeConnections = 0;
    private volatile int maxConnections = 100;
    private volatile long cacheHits = 0;
    private volatile long cacheAccess = 0;

    // 业务方法 - 模拟订单处理
    public void processOrder(long responseTime) {
        orderProcessCount++;
        totalResponseTime += responseTime;
        requestCount++;
    }

    public void recordCacheAccess(boolean hit) {
        cacheAccess++;
        if (hit) cacheHits++;
    }

    // MBean接口实现
    @Override
    public long getOrderProcessCount() {
        return orderProcessCount;
    }

    @Override
    public double getAverageResponseTime() {
        return requestCount > 0 ? (double) totalResponseTime / requestCount : 0;
    }

    @Override
    public int getActiveConnectionCount() {
        return activeConnections;
    }

    @Override
    public double getCacheHitRatio() {
        return cacheAccess > 0 ? (double) cacheHits / cacheAccess * 100 : 0;
    }

    @Override
    public void setMaxConnectionCount(int count) {
        this.maxConnections = count;
        System.out.println("最大连接数已调整为: " + count);
    }

    @Override
    public int getMaxConnectionCount() {
        return maxConnections;
    }

    @Override
    public void resetCounters() {
        orderProcessCount = 0;
        totalResponseTime = 0;
        requestCount = 0;
        cacheHits = 0;
        cacheAccess = 0;
        System.out.println("所有计数器已重置");
    }

    @Override
    public String getDetailedStats() {
        return String.format(
                "详细统计: 订单处理=%d, 平均响应时间=%.2fms, 缓存命中率=%.2f%%, 活跃连接=%d/%d",
                orderProcessCount, getAverageResponseTime(), getCacheHitRatio(),
                activeConnections, maxConnections);
    }
}
