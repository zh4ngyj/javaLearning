package actuator.jmx.sub;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 17:46
 * @des:
 */
public interface ConnectionPoolMBean {
    // 监控指标
    int getActiveConnectionCount();
    int getIdleConnectionCount();
    int getMaxConnectionCount();
    long getTotalRequestCount();
    long getErrorCount();
    double getAverageProcessingTime();

    // 管理操作
    void setMaxConnections(int max);
    void setConnectionTimeout(int timeoutMs);
    int getConnectionTimeout();
    void resetStatistics();
    String getPoolStatus();

    // 高级操作
    void clearIdleConnections();
    void pauseAcceptingConnections();
    void resumeAcceptingConnections();
    boolean isAcceptingConnections();
}