package multithreading.tl.itl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: zh4ngyj
 * @date: 2025/6/30 16:45
 * @des: InheritableThreadLocal
 */
public class InheritableThreadLocalTest {

    public static final InheritableThreadLocal<String> itl = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        Executor threadPool = Executors.newSingleThreadExecutor();
        // 模拟第一次开启子任务
        itl.set("itl-value-1");
        printThreadIdAndContext();
        CompletableFuture.supplyAsync(() -> {
            printThreadIdAndContext();
            return "task1";
        }, threadPool).join();
        printThreadIdAndContext();
        System.out.println("----------------------");
        // 模拟第二次开启子任务
        itl.set("itl-value-2");
        printThreadIdAndContext();
        CompletableFuture.supplyAsync(() -> {
            printThreadIdAndContext();
            return "task2";
        }, threadPool).join();
        printThreadIdAndContext();
    }

    private static void printThreadIdAndContext() {
        System.out.printf("【%2d】：%s%n", Thread.currentThread().getId(), itl.get());
    }
}
