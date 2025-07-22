package multithreading.tl.base.example;

/**
 * @author: zh4ngyj
 * @date: 2025/7/7 17:15
 * @des:序列号
 */
public class SerialNum {

    // The next serial number to be assigned
    private static int nextSerialNum = 0;

    private static ThreadLocal serialNum = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new Integer(nextSerialNum++);
        }
    };

    public static int get() {
        return ((Integer) (serialNum.get())).intValue();
    }

    public static void main(String[] args) {
        System.out.println(SerialNum.get());

        new Thread(()->{
            System.out.println(SerialNum.get());
        }).start();
        new Thread(()->{
            System.out.println(SerialNum.get());
        }).start();
        new Thread(()->{
            System.out.println(SerialNum.get());
        }).start();
    }
}
