package actuator.jmx.sub;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 17:44
 * @des: 配置管理MBean - 支持动态修改应用配置
 */

public class ConfigManager implements ConfigManagerMBean {
    private volatile String logLevel = "INFO";
    private volatile int threadPoolSize = 10;
    private volatile boolean recommendationEnabled = true;
    private final ThreadPoolExecutor threadPool;

    public ConfigManager() {
        this.threadPool = new ThreadPoolExecutor(
                threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

    @Override
    public String getLogLevel() {
        return logLevel;
    }

    @Override
    public void setLogLevel(String level) {
        if (!Arrays.asList("DEBUG", "INFO", "WARN", "ERROR").contains(level.toUpperCase())) {
            throw new IllegalArgumentException("无效的日志级别: " + level);
        }
        this.logLevel = level.toUpperCase();
        System.out.println("日志级别已动态调整为: " + logLevel);
        // 这里可以集成具体的日志框架进行动态调整
    }

    @Override
    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    @Override
    public void setThreadPoolSize(int size) {
        if (size <= 0 || size > 200) {
            throw new IllegalArgumentException("线程池大小必须在1-200之间");
        }

        int oldSize = this.threadPoolSize;
        this.threadPoolSize = size;

        // 动态调整线程池大小
        threadPool.setCorePoolSize(size);
        threadPool.setMaximumPoolSize(size);

        System.out.printf("线程池大小已从 %d 调整为 %d\n", oldSize, size);
    }

    @Override
    public boolean isRecommendationEnabled() {
        return recommendationEnabled;
    }

    @Override
    public void setRecommendationEnabled(boolean enabled) {
        this.recommendationEnabled = enabled;
        System.out.println("推荐系统已" + (enabled ? "开启" : "关闭"));
    }

    @Override
    public void reloadConfiguration() {
        System.out.println("重新加载配置文件...");
        // 模拟配置重载逻辑
        try {
            Thread.sleep(1000); // 模拟加载时间
            System.out.println("配置重载完成");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String[] getAvailableLogLevels() {
        return new String[]{"DEBUG", "INFO", "WARN", "ERROR"};
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }
}
