/**
 * @author zh4ngyj
 * @date 2025/8/3
 * @description
 */
public class VolatileModifier {

    private static volatile int volatileValue; // ✅ 正确拼写
    private static int generalValue;

    class A extends Thread {
        @Override
        public void run() {
            while (true) {
                if (VolatileModifier.volatileValue != 0) {
                    System.out.println("volatileValue: " + VolatileModifier.volatileValue);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileModifier vm = new VolatileModifier();
        VolatileModifier.A thread = vm.new A();
        thread.start();

        Thread.sleep(1000); // 让子线程先进入循环
        volatileValue = 42; // 修改静态变量
    }
}
