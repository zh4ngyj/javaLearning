package staticType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * @author: zh4ngyj
 * @date: 2025/7/1 12:30
 * @des: static关键字：变量、方法、代码块、类、接口方法
 */
public class StaticKeywordDemo {

    // 实例变量
    private String b;

    // 静态变量
    private static int a = 1;

    private final static List<String> nameList;
    // 静态代码块
    static {
        nameList = Arrays.asList("Tom", "Jerry", "Mike");
    }

    // 静态方法
    public static LocalDate getNowDate() {
        return LocalDate.now();
    }

    // 静态内部类
    private static class Holder {
        static final StaticKeywordDemo INSTANCE = new StaticKeywordDemo();
    }

    public static StaticKeywordDemo getInstance() {
        return Holder.INSTANCE;
    }

    public static void main(String[] args) {
    }
}
