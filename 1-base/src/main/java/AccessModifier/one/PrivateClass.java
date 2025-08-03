package AccessModifier.one;

/**
 * @author zh4ngyj
 * @date 2025/8/3
 * @description
 */
public class PrivateClass {

    private String privateName;

    String defaultName;

    protected String protectedName;

    public String publicName;

    class PrivateClassInner {
        public void test() {
            // 访问父类的私有成员变量
            System.out.println(privateName);
            // 访问父类的默认成员变量
            System.out.println(defaultName);
            // 访问父类的受保护的成员变量
            System.out.println(protectedName);
            // 访问父类的公共成员变量
            System.out.println(publicName);
        }
    }
}

// 同包子类
class Son extends PrivateClass {
    public void test() {
        // 无法访问父类的私有成员变量
//        System.out.println(privateName);
        // 访问父类的默认成员变量
        System.out.println(defaultName);
        // 访问父类的受保护的成员变量
        System.out.println(protectedName);
        // 访问父类的公共成员变量
        System.out.println(publicName);
    }
}

// 同包非子类
class OtherClass {
    public static void main(String[] args) {
        // 创建对象
        PrivateClass privateClass = new PrivateClass();
        // 无法访问私有成员变量
//        System.out.println(privateClass.privateName);
        // 访问默认成员变量
        System.out.println(privateClass.defaultName);
        // 访问受保护的成员变量
        System.out.println(privateClass.protectedName);
        // 访问公共成员变量
        System.out.println(privateClass.publicName);
    }
}
