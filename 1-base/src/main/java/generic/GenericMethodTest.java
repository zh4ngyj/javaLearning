package generic;

/**
 * @author: zh4ngyj
 * @date: 2025/6/30 16:35
 * @des: 泛型测试
 */
public class GenericMethodTest {

    public static <E> void printArray(E[] inputArray) {
        for (E element : inputArray) {
            System.out.printf("%s ", element);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] intArray = {1, 2, 3, 4, 5};
        Character[] charArray = {'H', 'E', 'L', 'L', 'O'};
        String[] stringArray = {"Good", "Bye"};

        System.out.println("整型数组：");
        printArray(intArray);
        System.out.println("字符数组：");
        printArray(charArray);
        System.out.println("字符串数组：");
        printArray(stringArray);
    }
}
