package actuator.endpoint;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 16:21
 * @des: 自定义Tomcat线程池监控端点
 */
@Component
@Endpoint(id = "tomcatThreadPool")
public class TomcatThreadPoolEndpoint {

    private final ServletWebServerApplicationContext applicationContext;

    // 注入 Spring Web 应用上下文，用于获取 Tomcat 容器
    public TomcatThreadPoolEndpoint(ServletWebServerApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @ReadOperation
    public Map<String, Object> getThreadPoolInfo() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取 Tomcat 服务器示例
            TomcatWebServer tomcatWebServer = (TomcatWebServer) applicationContext.getWebServer();
            // 获取 Tomcat 连接器
            Connector connector = tomcatWebServer.getTomcat().getConnector();
            // 从连接器获取线程池
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) connector.getProtocolHandler().getExecutor();

            // 获取指标
            result.put("status", "UP");
            result.put("threadPoolName", threadPoolExecutor.getThreadFactory().getClass().getSimpleName());
            // 核心线程数（默认 10）
            result.put("corePoolSize", threadPoolExecutor.getCorePoolSize());
            // 最大线程数（默认 200）
            result.put("maximumPoolSize", threadPoolExecutor.getMaximumPoolSize());
            // 当前活跃线程数
            result.put("activeCount", threadPoolExecutor.getActiveCount());
            // 当前线程池大小
            result.put("poolSize", threadPoolExecutor.getPoolSize());
            // 队列中等待的任务数
            result.put("queuedTasks", threadPoolExecutor.getQueue().size());
            // 队列剩余容量
            result.put("queueCapacity", threadPoolExecutor.getQueue().remainingCapacity());
            // 已完成任务数
            result.put("completedTasks", threadPoolExecutor.getCompletedTaskCount());
            // 历史最大线程数（峰值）
            result.put("largestPoolSize", threadPoolExecutor.getLargestPoolSize());
        } catch (Exception e) {
            // 处理非 Tomcat 容器或获取失败的情况（如使用 Jetty/Undertow 时）
            result.put("status", "DOWN");
            result.put("error", "获取 Tomcat 线程池信息失败：" + e.getMessage());
            result.put("tip", "请确认应用使用 Tomcat 作为 Web 容器");
        }
        return result;
    }

}
