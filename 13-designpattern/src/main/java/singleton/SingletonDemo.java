package singleton;// 单例模式：确保一个类只有一个实例
// https://www.cnblogs.com/sun-10387834/p/18950201
// 饿汉式、懒汉式、同步方法、双检锁式、静态内部类式、枚举式
// 安全问题：反射和序列化攻击

import java.io.*;
import java.lang.reflect.Constructor;

/**
 * @author: zh4ngyj
 * @date: 2025/7/1 14:16
 * @des:
 */
public class SingletonDemo implements Serializable {

    private Object var;

    // 饿汉式
//    private static final SingletonDemo instance = new SingletonDemo();
//    public static SingletonDemo getInstance() {
//        return instance;
//    }

    // 懒汉式
//    private static SingletonDemo instance;
//    public static SingletonDemo getInstance() {
//        if (instance == null) {
//            instance = new SingletonDemo();
//        }
//        return instance;
//    }

    // 同步方法
//    private static SingletonDemo instance;
//    public static synchronized SingletonDemo getInstance() {
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
//    private enum SingletonEnum {
//        INSTANCE;
//        private SingletonDemo instance;
//        SingletonEnum() {
//            instance = new SingletonDemo();
//        }
//    }
//    public static SingletonDemo getInstance() {
//        return SingletonEnum.INSTANCE.instance;
//    }

    /**
     * 反射
     */
//    public static void testReflection() throws Exception {
//        Class clazz= Class.forName("singleton.SingletonDemo");
//        Constructor constructor=clazz.getDeclaredConstructor(null);
//        constructor.setAccessible(true);
//
//        SingletonDemo singletonDemo1=(SingletonDemo) constructor.newInstance();
//        SingletonDemo singletonDemo2=(SingletonDemo) constructor.newInstance();
//        //返回为false
//        System.out.println(singletonDemo1==singletonDemo2);
//    }

    /**
     * 序列化和反序列化
     * @throws Exception
     */
//    public static void testSingleSerialization(SingletonDemo singletonDemo) throws Exception {
//        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("D://a.txt"));
//        oos.writeObject(singletonDemo);
//        oos.close();
//        ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream("D://a.txt"));
//        System.out.println(singletonDemo == objectInputStream.readObject());
//    }

    public static void main(String[] args) throws Exception {
//        testReflection();
//        testSingleSerialization(SingletonDemo.getInstance());
    }
}
