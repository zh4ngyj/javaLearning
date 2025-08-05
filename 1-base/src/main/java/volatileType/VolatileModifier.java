package volatileType;

/**
 * @author zh4ngyj
 * @date 2025/8/3
 * @description
 */
public class VolatileModifier {
    private static volatile int volatileValue;
    private static int generalValue;

    class A extends Thread {
        @Override
        public void run() {
            System.out.println("进入线程A");
            while (true) {
                if (VolatileModifier.volatileValue != 0) {
                    System.out.printf(
                            "volatileValue: %s%n",
                            VolatileModifier.volatileValue
                    );
                    break;
                }
            }
            System.out.println("退出线程A");
        }
    }

    class B extends Thread {
        @Override
        public void run() {
            System.out.println("进入线程B");
            while (true) {
                if (VolatileModifier.generalValue != 0) {
                    System.out.printf(
                            "generalValue: %s%n",
                            VolatileModifier.generalValue
                    );
                    break;
                }

            }
            System.out.println("退出线程B");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileModifier vm = new VolatileModifier();
        VolatileModifier.A threadA = vm.new A();
        VolatileModifier.B threadB = vm.new B();
        threadA.start();
        threadB.start();

        // 让子线程先进入循环
        Thread.sleep(1000);

        // 线程A会退出
        volatileValue = 42;

        // 线程B不会推出
        generalValue = 42;

        /*
output:
进入线程A
进入线程B
volatileValue: 42	 generalValue: 42
退出线程A

扩展：
synchronized 如何实现可见性？
JVM 在进入和退出 synchronized 时的具体动作：
1.进入 synchronized（加锁）时：
-清空当前线程的工作内存中被锁对象关联变量的缓存值（避免读取旧值）。
-从主内存重新读取变量。
2.退出 synchronized（解锁）时：
-将当前线程对共享变量的修改刷新到主内存。

可见性关注“你看没看到我写的数据”；
有序性关注“我写的顺序你看到是一样的吗”。
         */
    }
}
