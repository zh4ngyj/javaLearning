package actuator.jmx.sub;

import javax.management.*;
import java.lang.management.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.*;

/**
 * @author: zh4ngyj
 * @date: 2025/8/6 17:49
 * @des: 诊断工具 - 用于问题排查和调试
 */
public class DiagnosticTool {
    private final ThreadMXBean threadBean;
    private final MemoryMXBean memoryBean;
    private final RuntimeMXBean runtimeBean;
    private final MBeanServer server;

    public DiagnosticTool() {
        this.threadBean = ManagementFactory.getThreadMXBean();
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.runtimeBean = ManagementFactory.getRuntimeMXBean();
        this.server = ManagementFactory.getPlatformMBeanServer();
    }

    /**
     * 检测死锁
     */
    public void detectDeadlocks() {
        System.out.println("=== 死锁检测 ===");
        long[] deadlockedThreads = threadBean.findDeadlockedThreads();

        if (deadlockedThreads == null) {
            System.out.println("未检测到死锁");
            return;
        }

        System.out.println("检测到死锁线程:");
        ThreadInfo[] threadInfos = threadBean.getThreadInfo(deadlockedThreads);

        for (ThreadInfo info : threadInfos) {
            if (info != null) {
                System.out.printf("线程: %s (ID=%d)\n", info.getThreadName(), info.getThreadId());
                System.out.printf("状态: %s\n", info.getThreadState());
                System.out.printf("锁信息: %s\n", info.getLockInfo());

                StackTraceElement[] stackTrace = info.getStackTrace();
                for (StackTraceElement element : stackTrace) {
                    System.out.printf("  at %s\n", element.toString());
                }
                System.out.println();
            }
        }
    }

    /**
     * 分析长时间阻塞的线程
     */
    public void analyzeBlockedThreads() {
        System.out.println("=== 阻塞线程分析 ===");

        // 获取所有线程ID
        long[] threadIds = threadBean.getAllThreadIds();
        ThreadInfo[] allThreads = threadBean.getThreadInfo(threadIds, Integer.MAX_VALUE);

        for (ThreadInfo info : allThreads) {
            if (info != null &&
                    (info.getThreadState() == Thread.State.BLOCKED ||
                            info.getThreadState() == Thread.State.WAITING ||
                            info.getThreadState() == Thread.State.TIMED_WAITING)) {

                System.out.printf("线程: %s (ID=%d)\n", info.getThreadName(), info.getThreadId());
                System.out.printf("状态: %s\n", info.getThreadState());
                System.out.printf("阻塞时间: %dms\n", info.getBlockedTime());
                System.out.printf("等待时间: %dms\n", info.getWaitedTime());

                if (info.getLockInfo() != null) {
                    System.out.printf("等待锁: %s\n", info.getLockInfo());
                    System.out.printf("锁持有者: %s\n", info.getLockOwnerName());
                }

                System.out.println("线程栈:");
                StackTraceElement[] stackTrace = info.getStackTrace();
                for (int i = 0; i < Math.min(stackTrace.length, 5); i++) {
                    System.out.printf("  at %s\n", stackTrace[i].toString());
                }
                System.out.println();
            }
        }
    }

    /**
     * 内存分析 - 分析各内存池使用情况
     */
    public void analyzeMemoryPools() {
        System.out.println("=== 内存池分析 ===");

        List<MemoryPoolMXBean> memoryPools = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean pool : memoryPools) {
            MemoryUsage usage = pool.getUsage();
            MemoryUsage peakUsage = pool.getPeakUsage();

            System.out.printf("内存池: %s\n", pool.getName());
            System.out.printf("类型: %s\n", pool.getType());

            if (usage.getMax() > 0) {
                System.out.printf("当前使用: %dMB / %dMB (%.2f%%)\n",
                        usage.getUsed() / 1024 / 1024,
                        usage.getMax() / 1024 / 1024,
                        (double) usage.getUsed() / usage.getMax() * 100);
            } else {
                System.out.printf("当前使用: %dMB / 无限制\n",
                        usage.getUsed() / 1024 / 1024);
            }

            System.out.printf("峰值使用: %dMB\n", peakUsage.getUsed() / 1024 / 1024);

            // 检查是否接近内存限制
            if (usage.getMax() > 0 && (double) usage.getUsed() / usage.getMax() > 0.8) {
                System.out.println("⚠️  警告: 内存使用率过高!");
            }
            System.out.println();
        }
    }

    /**
     * 强制进行垃圾回收并分析效果
     */
    public void performGCAnalysis() {
        System.out.println("=== GC分析 ===");

        // 记录GC前的状态
        MemoryUsage beforeHeap = memoryBean.getHeapMemoryUsage();
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        Map<String, Long> gcCountBefore = new HashMap<>();
        Map<String, Long> gcTimeBefore = new HashMap<>();

        for (GarbageCollectorMXBean gcBean : gcBeans) {
            gcCountBefore.put(gcBean.getName(), gcBean.getCollectionCount());
            gcTimeBefore.put(gcBean.getName(), gcBean.getCollectionTime());
        }

        // 强制GC
        System.out.println("执行强制GC...");
        long startTime = System.currentTimeMillis();
        System.gc();
        System.runFinalization();
        long endTime = System.currentTimeMillis();

        // 等待GC完成
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 分析GC效果
        MemoryUsage afterHeap = memoryBean.getHeapMemoryUsage();

        System.out.printf("GC前堆内存: %dMB\n", beforeHeap.getUsed() / 1024 / 1024);
        System.out.printf("GC后堆内存: %dMB\n", afterHeap.getUsed() / 1024 / 1024);
        System.out.printf("释放内存: %dMB\n",
                (beforeHeap.getUsed() - afterHeap.getUsed()) / 1024 / 1024);
        System.out.printf("GC耗时: %dms\n", endTime - startTime);

        for (GarbageCollectorMXBean gcBean : gcBeans) {
            long countAfter = gcBean.getCollectionCount();
            long timeAfter = gcBean.getCollectionTime();

            long countDiff = countAfter - gcCountBefore.get(gcBean.getName());
            long timeDiff = timeAfter - gcTimeBefore.get(gcBean.getName());

            if (countDiff > 0) {
                System.out.printf("%s: +%d次, +%dms\n",
                        gcBean.getName(), countDiff, timeDiff);
            }
        }
    }

    /**
     * 方法级别的性能监控 - 通过动态代理实现AOP
     */
    public void monitorMethodPerformance(String className, String methodName) {
        System.out.println("=== 方法性能监控 ===");
        System.out.printf("开始监控方法: %s.%s\n", className, methodName);

        // 这里可以集成字节码操作库（如ASM、Javassist）来动态注入监控代码
        // 为了演示，我们模拟方法调用统计

        Map<String, MethodStats> methodStats = new ConcurrentHashMap<>();
        String methodKey = className + "." + methodName;

        // 模拟方法调用数据
        MethodStats stats = methodStats.computeIfAbsent(methodKey, k -> new MethodStats());

        System.out.printf("方法 %s 统计信息:\n", methodKey);
        System.out.printf("  调用次数: %d\n", stats.getCallCount());
        System.out.printf("  总耗时: %dms\n", stats.getTotalTime());
        System.out.printf("  平均耗时: %.2fms\n", stats.getAverageTime());
        System.out.printf("  最大耗时: %dms\n", stats.getMaxTime());
        System.out.printf("  最小耗时: %dms\n", stats.getMinTime());
    }

    /**
     * 高级线程分析 - 包括线程栈采样和热点分析
     */
    public void performAdvancedThreadAnalysis() {
        System.out.println("=== 高级线程分析 ===");

        // 获取所有线程信息
        long[] threadIds = threadBean.getAllThreadIds();
        ThreadInfo[] allThreads = threadBean.getThreadInfo(threadIds, Integer.MAX_VALUE);

        // 统计线程状态分布
        Map<Thread.State, Integer> stateCount = new EnumMap<>(Thread.State.class);
        Map<String, Integer> threadGroupCount = new HashMap<>();

        for (ThreadInfo info : allThreads) {
            if (info != null) {
                stateCount.merge(info.getThreadState(), 1, Integer::sum);

                String threadName = info.getThreadName();
                String groupName = extractThreadGroupName(threadName);
                threadGroupCount.merge(groupName, 1, Integer::sum);
            }
        }

        System.out.println("线程状态分布:");
        stateCount.forEach((state, count) ->
                System.out.printf("  %s: %d\n", state, count));

        System.out.println("\n线程组分布:");
        threadGroupCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.printf("  %s: %d\n", entry.getKey(), entry.getValue()));

        // 查找高CPU使用率线程
        if (threadBean.isThreadCpuTimeSupported()) {
            System.out.println("\nCPU使用率最高的线程:");
            Arrays.stream(allThreads)
                    .filter(Objects::nonNull)
                    .sorted((a, b) -> Long.compare(
                            threadBean.getThreadCpuTime(b.getThreadId()),
                            threadBean.getThreadCpuTime(a.getThreadId())))
                    .limit(5)
                    .forEach(info -> {
                        long cpuTime = threadBean.getThreadCpuTime(info.getThreadId());
                        System.out.printf("  %s: %dms CPU时间\n",
                                info.getThreadName(), cpuTime / 1_000_000);
                    });
        }
    }

    private String extractThreadGroupName(String threadName) {
        // 提取线程组名称的简单逻辑
        if (threadName.contains("pool")) return "ThreadPool";
        if (threadName.startsWith("http")) return "HTTP";
        if (threadName.contains("GC")) return "GC";
        if (threadName.contains("Finalizer")) return "Finalizer";
        if (threadName.startsWith("main")) return "Main";
        return "Other";
    }

    /**
     * 内存泄漏检测 - 通过对象存活分析
     */
    public void detectMemoryLeaks() {
        System.out.println("=== 内存泄漏检测 ===");

        // 执行多次GC以清理临时对象
        System.out.println("执行清理GC...");
        for (int i = 0; i < 3; i++) {
            System.gc();
            System.runFinalization();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // 记录GC后的内存状态
        MemoryUsage afterGC = memoryBean.getHeapMemoryUsage();
        long baselineMemory = afterGC.getUsed();

        System.out.printf("基线内存使用量: %dMB\n", baselineMemory / 1024 / 1024);

        // 分析内存池趋势
        List<MemoryPoolMXBean> memoryPools = ManagementFactory.getMemoryPoolMXBeans();

        for (MemoryPoolMXBean pool : memoryPools) {
            if (pool.getType() == MemoryType.HEAP) {
                MemoryUsage usage = pool.getUsage();
                MemoryUsage peakUsage = pool.getPeakUsage();

                // 计算内存增长趋势
                if (usage.getMax() > 0) {
                    double utilizationRatio = (double) usage.getUsed() / usage.getMax();
                    double peakRatio = (double) peakUsage.getUsed() / usage.getMax();

                    System.out.printf("\n内存池: %s\n", pool.getName());
                    System.out.printf("  当前使用率: %.2f%%\n", utilizationRatio * 100);
                    System.out.printf("  峰值使用率: %.2f%%\n", peakRatio * 100);

                    if (utilizationRatio > 0.8) {
                        System.out.println("  ⚠️  可能存在内存泄漏风险!");

                        // 建议处理措施
                        System.out.println("  建议措施:");
                        System.out.println("    - 检查长期持有的对象引用");
                        System.out.println("    - 检查缓存是否正确释放");
                        System.out.println("    - 使用内存分析工具进行详细分析");
                    }
                } else {
                    System.out.printf("\n内存池: %s (无限制)\n", pool.getName());
                    System.out.printf("  当前使用: %dMB\n", usage.getUsed() / 1024 / 1024);
                    System.out.printf("  峰值使用: %dMB\n", peakUsage.getUsed() / 1024 / 1024);
                }

                // 重置峰值统计以便下次分析
                pool.resetPeakUsage();
            }
        }
    }

    /**
     * JVM参数和环境信息分析
     */
    public void analyzeJVMEnvironment() {
        System.out.println("=== JVM环境分析 ===");

        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();

        System.out.println("JVM基本信息:");
        System.out.printf("  JVM名称: %s\n", runtime.getVmName());
        System.out.printf("  JVM版本: %s\n", runtime.getVmVersion());
        System.out.printf("  JVM供应商: %s\n", runtime.getVmVendor());
        System.out.printf("  启动时间: %s\n", new Date(runtime.getStartTime()));
        System.out.printf("  运行时长: %d分钟\n", runtime.getUptime() / 1000 / 60);

        System.out.println("\n系统信息:");
        System.out.printf("  操作系统: %s %s\n", os.getName(), os.getVersion());
        System.out.printf("  系统架构: %s\n", os.getArch());
        System.out.printf("  可用处理器: %d\n", os.getAvailableProcessors());

        try {
            if (os instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOs =
                        (com.sun.management.OperatingSystemMXBean) os;
                System.out.printf("  物理内存: %dGB\n",
                        sunOs.getTotalPhysicalMemorySize() / 1024 / 1024 / 1024);
                System.out.printf("  可用内存: %dGB\n",
                        sunOs.getFreePhysicalMemorySize() / 1024 / 1024 / 1024);

                double systemCpu = sunOs.getSystemCpuLoad();
                double processCpu = sunOs.getProcessCpuLoad();

                if (systemCpu >= 0) {
                    System.out.printf("  系统CPU使用率: %.2f%%\n", systemCpu * 100);
                }
                if (processCpu >= 0) {
                    System.out.printf("  进程CPU使用率: %.2f%%\n", processCpu * 100);
                }
            }
        } catch (Exception e) {
            System.out.println("  无法获取详细系统信息: " + e.getMessage());
        }

        System.out.println("\nJVM启动参数:");
        List<String> arguments = runtime.getInputArguments();
        for (String arg : arguments) {
            System.out.printf("  %s\n", arg);
        }

        System.out.println("\n类加载信息:");
        ClassLoadingMXBean classLoading = ManagementFactory.getClassLoadingMXBean();
        System.out.printf("  已加载类数: %d\n", classLoading.getLoadedClassCount());
        System.out.printf("  总加载类数: %d\n", classLoading.getTotalLoadedClassCount());
        System.out.printf("  已卸载类数: %d\n", classLoading.getUnloadedClassCount());
    }

    /**
     * 生成诊断报告
     */
    public String generateDiagnosticReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== JMX诊断报告 ===\n");
        report.append("生成时间: ").append(new Date()).append("\n\n");

        try {
            // 内存状态摘要
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            report.append("内存状态摘要:\n");
            if (heapUsage.getMax() > 0) {
                report.append(String.format("  堆内存使用率: %.2f%%\n",
                        (double) heapUsage.getUsed() / heapUsage.getMax() * 100));
            } else {
                report.append(String.format("  堆内存使用: %dMB (无限制)\n",
                        heapUsage.getUsed() / 1024 / 1024));
            }

            // 线程状态摘要
            report.append(String.format("  活跃线程数: %d\n", threadBean.getThreadCount()));
            report.append(String.format("  守护线程数: %d\n", threadBean.getDaemonThreadCount()));

            // GC状态摘要
            List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
            long totalGCTime = gcBeans.stream().mapToLong(GarbageCollectorMXBean::getCollectionTime).sum();
            report.append(String.format("  总GC时间: %dms\n", totalGCTime));

            // 检查潜在问题
            report.append("\n潜在问题检查:\n");

            if (heapUsage.getMax() > 0 && (double) heapUsage.getUsed() / heapUsage.getMax() > 0.9) {
                report.append("  ❌ 堆内存使用率过高 (>90%)\n");
            } else {
                report.append("  ✅ 堆内存使用正常\n");
            }

            if (threadBean.findDeadlockedThreads() != null) {
                report.append("  ❌ 检测到线程死锁\n");
            } else {
                report.append("  ✅ 未检测到死锁\n");
            }

            if (totalGCTime > runtimeBean.getUptime() * 0.1) {
                report.append("  ❌ GC时间占比过高 (>10%)\n");
            } else {
                report.append("  ✅ GC性能正常\n");
            }

        } catch (Exception e) {
            report.append("报告生成过程中出现错误: ").append(e.getMessage()).append("\n");
        }

        return report.toString();
    }

    /**
     * 执行完整的系统诊断
     */
    public void performFullDiagnostic() {
        System.out.println("开始执行完整系统诊断...\n");

        analyzeJVMEnvironment();
        System.out.println();

        detectDeadlocks();
        System.out.println();

        performAdvancedThreadAnalysis();
        System.out.println();

        analyzeMemoryPools();
        System.out.println();

        detectMemoryLeaks();
        System.out.println();

        performGCAnalysis();
        System.out.println();

        // 生成诊断报告
        String report = generateDiagnosticReport();
        System.out.println(report);

        System.out.println("系统诊断完成");
    }

    // 内部类：方法统计信息
    private static class MethodStats {
        private final AtomicLong callCount = new AtomicLong(0);
        private final AtomicLong totalTime = new AtomicLong(0);
        private final AtomicLong maxTime = new AtomicLong(0);
        private final AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);

        public void recordCall(long executionTime) {
            callCount.incrementAndGet();
            totalTime.addAndGet(executionTime);
            maxTime.updateAndGet(current -> Math.max(current, executionTime));
            minTime.updateAndGet(current -> Math.min(current, executionTime));
        }

        public long getCallCount() {
            return callCount.get();
        }

        public long getTotalTime() {
            return totalTime.get();
        }

        public double getAverageTime() {
            long count = callCount.get();
            return count > 0 ? (double) totalTime.get() / count : 0;
        }

        public long getMaxTime() {
            return maxTime.get();
        }

        public long getMinTime() {
            long min = minTime.get();
            return min == Long.MAX_VALUE ? 0 : min;
        }
    }

    // 主方法用于测试
    public static void main(String[] args) {
        DiagnosticTool tool = new DiagnosticTool();
        tool.performFullDiagnostic();
    }
}