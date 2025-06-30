package tl.base;

/**
 * @author: zh4ngyj
 * @date: 2025/6/30 15:51
 * @des: threadlocal
 */
public class ThreadLocalTest {

    public static final ThreadLocal<String> tl = ThreadLocal.withInitial(() -> "init");

    public static void main(String[] args) {
        // 主线程
        tl.set("hello world");
        System.out.println(tl.get());

        // 子线程
        new Thread(() -> {
            System.out.println(tl.get());
        }).start();

        
    }
}
