// 单例模式：确保一个类只有一个实例
// https://www.cnblogs.com/sun-10387834/p/18950201
// 饿汉式、懒汉式、同步方法、双检锁式、静态内部类式、枚举式
// 安全问题：反射和序列化攻击

/**
 * @author: zh4ngyj
 * @date: 2025/7/1 14:16
 * @des:
 */
public class SingletonDemo {

    private Object var;

    // 饿汉式
//    private static final SingletonDemo instance = new SingletonDemo();

    // 懒汉式
//    private static SingletonDemo instance;
//    public SingletonDemo getInstance() {
//        if (instance == null) {
//            instance = new SingletonDemo();
//        }
//        return instance;
//    }

    // 同步方法
//    private static SingletonDemo instance;
//    public synchronized SingletonDemo getInstance() {
//        if (instance == null) {
//            instance = new SingletonDemo();
//        }
//        return instance;
//    }

    // 双检锁式
//    private static SingletonDemo instance;
//    public static synchronized SingletonDemo getInstance() {
//        if (instance == null) {
//            synchronized (SingletonDemo.class) {
//                if (instance == null) {
//                    instance = new SingletonDemo();
//                }
//            }
//        }
//        return instance;
//    }

    // 静态内部类式
//    private static class SingletonHolder {
//        private static final SingletonDemo INSTANCE = new SingletonDemo();
//    }
//    public static SingletonDemo getInstance() {
//        return SingletonHolder.INSTANCE;
//    }

    // 枚举式
//    public enum SingletonEnum {
//        INSTANCE;
//        private SingletonDemo instance;
//        SingletonEnum() {
//            instance = new SingletonDemo();
//        }
//    }
//    public static SingletonDemo getInstance() {
//        return SingletonEnum.INSTANCE.instance;
//    }
}
