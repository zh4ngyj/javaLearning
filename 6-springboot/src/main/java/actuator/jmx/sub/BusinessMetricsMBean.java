package actuator.jmx.sub;


public interface BusinessMetricsMBean {
    // 只读属性
    long getOrderProcessCount();
    double getAverageResponseTime();
    int getActiveConnectionCount();
    double getCacheHitRatio();

    // 可写属性
    void setMaxConnectionCount(int count);
    int getMaxConnectionCount();

    // 操作方法
    void resetCounters();
    String getDetailedStats();
}

