package actuator.jmx.sub;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 17:43
 * @des:
 */
public interface ConfigManagerMBean {
    // 日志级别管理
    String getLogLevel();
    void setLogLevel(String level);

    // 线程池配置
    int getThreadPoolSize();
    void setThreadPoolSize(int size);

    // 功能开关
    boolean isRecommendationEnabled();
    void setRecommendationEnabled(boolean enabled);

    // 操作方法
    void reloadConfiguration();
    String[] getAvailableLogLevels();
}

