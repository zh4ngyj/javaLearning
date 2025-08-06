package actuator.jmx.sub;

import java.lang.management.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 17:32
 * @des: 1.JVM状态监控器 - 监控内存、线程、GC等核心指标
 */
public class JVMMonitor {
    private final MemoryMXBean memoryBean;
    private final ThreadMXBean threadBean;
    private final List<GarbageCollectorMXBean> gcBeans;
    private final ScheduledExecutorService scheduler;

    public JVMMonitor() {
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.threadBean = ManagementFactory.getThreadMXBean();
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startMonitoring() {
        scheduler.scheduleAtFixedRate(this::printSystemStatus, 0, 10, TimeUnit.SECONDS);
    }

    private void printSystemStatus() {
        System.out.println("=== JVM状态监控 ===");

        // 监控内存使用情况
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();

        System.out.printf("堆内存: 已用=%dMB, 最大=%dMB, 使用率=%.2f%%\n",
                heapUsage.getUsed() / 1024 / 1024,
                heapUsage.getMax() / 1024 / 1024,
                (double) heapUsage.getUsed() / heapUsage.getMax() * 100);

        System.out.printf("非堆内存: 已用=%dMB, 最大=%dMB\n",
                nonHeapUsage.getUsed() / 1024 / 1024,
                nonHeapUsage.getMax() / 1024 / 1024);

        // 监控线程状态
        System.out.printf("线程总数: %d, 活跃线程: %d, 守护线程: %d\n",
                threadBean.getThreadCount(),
                threadBean.getThreadCount() - threadBean.getDaemonThreadCount(),
                threadBean.getDaemonThreadCount());

        // 监控GC情况
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.printf("GC[%s]: 次数=%d, 总耗时=%dms\n",
                    gcBean.getName(), gcBean.getCollectionCount(), gcBean.getCollectionTime());
        }

        System.out.println();
    }

    public void stopMonitoring() {
        scheduler.shutdown();
    }
}
