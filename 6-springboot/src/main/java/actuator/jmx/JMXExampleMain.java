/*
1. 模拟业务活动 (simulateBusinessActivity)

订单处理模拟：每2秒模拟一次订单处理，包含随机响应时间和成功率
配置动态调整：10秒后自动演示配置的动态修改
高负载场景：20秒后启动50个并发线程模拟高负载情况

2. 程序化MBean访问 (demonstrateProgrammaticAccess)

展示如何通过代码直接访问MBean属性和操作
包含读取属性、修改配置、调用MBean方法的完整示例

DiagnosticTool类的完整诊断功能：
1. 方法性能监控 (monitorMethodPerformance)

提供AOP风格的方法级别性能监控框架
统计方法调用次数、总耗时、平均/最大/最小耗时
为集成字节码操作库（如ASM、Javassist）预留接口

2. 高级线程分析 (performAdvancedThreadAnalysis)

线程状态分布统计：分析各种线程状态的数量分布
线程组分类：自动识别线程类型（线程池、HTTP、GC等）
CPU使用率排序：找出CPU使用率最高的线程

3. 内存泄漏检测 (detectMemoryLeaks)

多轮GC清理：执行多次垃圾回收以清理临时对象
基线内存建立：建立内存使用基线用于对比
内存趋势分析：分析各内存池的使用率和增长趋势
风险预警：当内存使用率超过80%时发出预警

4. JVM环境分析 (analyzeJVMEnvironment)

JVM基本信息：版本、供应商、启动时间、运行时长
系统硬件信息：CPU核心数、物理内存、系统架构
性能指标：系统和进程的CPU使用率
启动参数：完整的JVM启动参数列表
类加载统计：已加载、总加载、已卸载的类数量

5. 诊断报告生成 (generateDiagnosticReport)

生成结构化的诊断报告
包含内存、线程、GC状态摘要
自动检测潜在问题并给出建议
支持导出为文本格式便于保存和分析

6. 内部工具类

MethodStats类：线程安全的方法统计信息收集器
线程组识别：智能识别线程的归属和用途
 */
package actuator.jmx;

import actuator.jmx.sub.*;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 17:51
 * @des: 主类：演示所有功能
 */
public class JMXExampleMain {
    public static void main(String[] args) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        // 1. 注册自定义MBean
        BusinessMetrics businessMetrics = new BusinessMetrics();
        ObjectName businessName = new ObjectName("com.example:type=BusinessMetrics");
        server.registerMBean(businessMetrics, businessName);

        ConfigManager configManager = new ConfigManager();
        ObjectName configName = new ObjectName("com.example:type=ConfigManager");
        server.registerMBean(configManager, configName);

        ConnectionPool connectionPool = new ConnectionPool();
        ObjectName poolName = new ObjectName("com.example:type=ConnectionPool");
        server.registerMBean(connectionPool, poolName);

        System.out.println("MBean已注册，可通过JConsole连接进行管理");
        System.out.println("连接参数: " + ManagementFactory.getRuntimeMXBean().getName());

        // 2. 启动JVM监控
        JVMMonitor jvmMonitor = new JVMMonitor();
        jvmMonitor.startMonitoring();

        // 3. 启动指标导出服务
        MetricsExporter exporter = new MetricsExporter();
        exporter.startMetricsServer();

        // 4. 模拟业务活动 - 将 configManager 作为参数传递
        simulateBusinessActivity(businessMetrics, connectionPool, configManager);

        // 4.5 演示程序化访问MBean
        Thread.sleep(5000);
        demonstrateProgrammaticAccess();

        // 5. 执行诊断
        DiagnosticTool diagnostic = new DiagnosticTool();

        // 等待一段时间后执行完整诊断
        Thread.sleep(30000);
        diagnostic.performFullDiagnostic();

        // 保持程序运行以便通过JMX客户端连接
        System.out.println("\n程序将持续运行，可通过以下方式连接:");
        System.out.println("1. 使用JConsole: 连接到本地进程");
        System.out.println("2. 使用代码方式: 通过MBeanServer访问");
        System.out.println("按Ctrl+C退出\n");

        // 保持主线程运行
        Thread.currentThread().join();
    }

    // 修改方法签名，添加 configManager 参数
    private static void simulateBusinessActivity(BusinessMetrics businessMetrics,
                                                 ConnectionPool connectionPool,
                                                 ConfigManager configManager) {
        // 启动后台线程模拟业务活动
        ScheduledExecutorService simulator = Executors.newScheduledThreadPool(3);

        // 模拟订单处理
        simulator.scheduleAtFixedRate(() -> {
            long responseTime = 50 + (long)(Math.random() * 200); // 50-250ms
            boolean success = Math.random() > 0.05; // 95%成功率
            businessMetrics.processOrder(responseTime);
            connectionPool.processRequest(responseTime, success);

            // 模拟缓存访问
            boolean cacheHit = Math.random() > 0.3; // 70%命中率
            businessMetrics.recordCacheAccess(cacheHit);
        }, 1, 2, TimeUnit.SECONDS);

        // 模拟配置变更 - 现在可以访问 configManager 了
        simulator.schedule(() -> {
            System.out.println("模拟动态配置调整...");
            try {
                configManager.setLogLevel("DEBUG");
                Thread.sleep(5000);
                configManager.setThreadPoolSize(15);
                Thread.sleep(5000);
                configManager.setRecommendationEnabled(false);
            } catch (Exception e) {
                System.err.println("配置调整失败: " + e.getMessage());
            }
        }, 10, TimeUnit.SECONDS);

        // 模拟高负载场景
        simulator.schedule(() -> {
            System.out.println("模拟高负载场景...");
            for (int i = 0; i < 50; i++) {
                final int taskId = i;
                new Thread(() -> {
                    try {
                        // 模拟CPU密集型任务
                        long start = System.currentTimeMillis();
                        while (System.currentTimeMillis() - start < 1000) {
                            Math.sqrt(Math.random());
                        }
                        businessMetrics.processOrder(1000);
                    } catch (Exception e) {
                        System.err.println("高负载任务失败: " + e.getMessage());
                    }
                }, "HighLoad-" + taskId).start();
            }
        }, 20, TimeUnit.SECONDS);
    }

    /**
     * 演示通过代码方式访问MBean
     */
    private static void demonstrateProgrammaticAccess() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        System.out.println("=== 通过代码访问MBean演示 ===");

        // 访问自定义业务指标
        ObjectName businessName = new ObjectName("com.example:type=BusinessMetrics");
        if (server.isRegistered(businessName)) {
            Long orderCount = (Long) server.getAttribute(businessName, "OrderProcessCount");
            Double avgTime = (Double) server.getAttribute(businessName, "AverageResponseTime");

            System.out.printf("订单处理数量: %d\n", orderCount);
            System.out.printf("平均响应时间: %.2fms\n", avgTime);

            // 调用MBean操作
            String detailedStats = (String) server.invoke(businessName, "getDetailedStats", null, null);
            System.out.println("详细统计: " + detailedStats);
        }

        // 访问配置管理
        ObjectName configName = new ObjectName("com.example:type=ConfigManager");
        if (server.isRegistered(configName)) {
            String logLevel = (String) server.getAttribute(configName, "LogLevel");
            Integer threadPoolSize = (Integer) server.getAttribute(configName, "ThreadPoolSize");

            System.out.printf("当前日志级别: %s\n", logLevel);
            System.out.printf("线程池大小: %d\n", threadPoolSize);

            // 动态修改配置
            server.setAttribute(configName, new Attribute("LogLevel", "WARN"));
            System.out.println("日志级别已通过代码修改为: WARN");
        }

        // 访问连接池状态
        ObjectName poolName = new ObjectName("com.example:type=ConnectionPool");
        if (server.isRegistered(poolName)) {
            String poolStatus = (String) server.invoke(poolName, "getPoolStatus", null, null);
            System.out.println("连接池状态: " + poolStatus);
        }
    }
}