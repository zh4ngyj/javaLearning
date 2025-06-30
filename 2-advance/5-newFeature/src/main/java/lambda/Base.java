package lambda;

import java.util.Arrays;
import java.util.List;

/**
 * @author: zh4ngyj
 * @date: 2025/6/30 16:10
 * @des: lambda
 */
public class Base {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Tom");

        names.forEach(name -> System.out.println(name));
        names.forEach(System.out::println);
    }

}
