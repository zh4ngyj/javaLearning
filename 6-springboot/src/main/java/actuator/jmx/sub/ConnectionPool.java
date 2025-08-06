package actuator.jmx.sub;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 17:47
 * @des: 连接池管理MBean - 模拟Tomcat连接池管理
 */
public class ConnectionPool implements ConnectionPoolMBean {
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final AtomicInteger idleConnections = new AtomicInteger(5);
    private volatile int maxConnections = 100;
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final AtomicLong totalProcessingTime = new AtomicLong(0);
    private volatile int connectionTimeout = 30000; // 30秒
    private volatile boolean acceptingConnections = true;

    @Override
    public int getActiveConnectionCount() {
        return activeConnections.get();
    }

    @Override
    public int getIdleConnectionCount() {
        return idleConnections.get();
    }

    @Override
    public int getMaxConnectionCount() {
        return maxConnections;
    }

    @Override
    public long getTotalRequestCount() {
        return totalRequests.get();
    }

    @Override
    public long getErrorCount() {
        return errorCount.get();
    }

    @Override
    public double getAverageProcessingTime() {
        long total = totalRequests.get();
        return total > 0 ? (double) totalProcessingTime.get() / total : 0;
    }

    @Override
    public void setMaxConnections(int max) {
        if (max <= 0) {
            throw new IllegalArgumentException("最大连接数必须大于0");
        }
        this.maxConnections = max;
        System.out.println("最大连接数已调整为: " + max);
    }

    @Override
    public void setConnectionTimeout(int timeoutMs) {
        if (timeoutMs <= 0) {
            throw new IllegalArgumentException("超时时间必须大于0");
        }
        this.connectionTimeout = timeoutMs;
        System.out.println("连接超时时间已调整为: " + timeoutMs + "ms");
    }

    @Override
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public void resetStatistics() {
        totalRequests.set(0);
        errorCount.set(0);
        totalProcessingTime.set(0);
        System.out.println("连接池统计信息已重置");
    }

    @Override
    public String getPoolStatus() {
        return String.format(
                "连接池状态: 活跃=%d, 空闲=%d, 最大=%d, 总请求=%d, 错误=%d, 平均处理时间=%.2fms, 接受新连接=%s",
                getActiveConnectionCount(), getIdleConnectionCount(), getMaxConnectionCount(),
                getTotalRequestCount(), getErrorCount(), getAverageProcessingTime(),
                isAcceptingConnections() ? "是" : "否"
        );
    }

    @Override
    public void clearIdleConnections() {
        int cleared = idleConnections.getAndSet(0);
        System.out.println("已清理 " + cleared + " 个空闲连接");
    }

    @Override
    public void pauseAcceptingConnections() {
        this.acceptingConnections = false;
        System.out.println("已暂停接受新连接");
    }

    @Override
    public void resumeAcceptingConnections() {
        this.acceptingConnections = true;
        System.out.println("已恢复接受新连接");
    }

    @Override
    public boolean isAcceptingConnections() {
        return acceptingConnections;
    }

    // 模拟请求处理
    public void processRequest(long processingTime, boolean success) {
        totalRequests.incrementAndGet();
        totalProcessingTime.addAndGet(processingTime);
        if (!success) {
            errorCount.incrementAndGet();
        }

        // 模拟连接池状态变化
        if (activeConnections.get() < maxConnections) {
            activeConnections.incrementAndGet();
            // 模拟处理完成后释放连接
            new Thread(() -> {
                try {
                    Thread.sleep(processingTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    activeConnections.decrementAndGet();
                    idleConnections.incrementAndGet();
                }
            }).start();
        }
    }
}
