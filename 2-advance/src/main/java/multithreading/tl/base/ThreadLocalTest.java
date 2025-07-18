package multithreading.tl.base;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: zh4ngyj
 * @date: 2025/6/30 15:51
 * @des: threadlocal
 */
public class ThreadLocalTest {

    public static final ThreadLocal<String> tl = ThreadLocal.withInitial(() -> "init1");

    public static void main(String[] args) {
        // 主线程
        String mainName = Thread.currentThread().getName();
        tl.set("init2");
        System.out.printf("%s : %s\n", mainName, tl.get());
        tl.remove();
        System.out.printf("%s : %s\n", mainName, tl.get());

        // 子线程
        Executor threadPool = Executors.newSingleThreadExecutor();
        threadPool.execute(() -> {
            String subName = Thread.currentThread().getName();
            System.out.println(subName + " : " + tl.get());
            tl.set("sub thread");
            System.out.println(subName + " : " + tl.get());
        });
    }
}
