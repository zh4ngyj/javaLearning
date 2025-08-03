package AccessModifier.two;

import AccessModifier.one.PrivateClass;

/**
 * @author zh4ngyj
 * @date 2025/8/3
 * @description
 */
// 其他包的非子类
public class OtherPackClass {

    public static void main(String[] args) {
        PrivateClass privateClass = new PrivateClass();
//        System.out.println(privateClass.privateName);
//        System.out.println(privateClass.defaultName);
//        System.out.println(privateClass.protectedName);
        System.out.println(privateClass.publicName);
    }
}

// 其他包的子类
class OtherPackSonClass extends PrivateClass{

    public static void main(String[] args) {
        OtherPackSonClass otherPackSonClass = new OtherPackSonClass();
        // 无法访问其他包的私有成员变量
//        System.out.println(otherPackSonClass.privateName);
        // 无法访问其他包的默认成员变量
//        System.out.println(otherPackSonClass.defaultName);
        // 可以访问其他包的受保护的成员变量
        System.out.println(otherPackSonClass.protectedName);
        // 可以访问其他包的公共成员变量
        System.out.println(otherPackSonClass.publicName);
    }

}