package multithreading.tl.base;

/**
 * @author: zh4ngyj
 * @date: 2025/7/3 16:51
 * @des: 黄金分割数|斐波那契数散列
 */
public class GoldenHashDemo {

    // 验证代码
    static final int HASH_INCREMENT = 0x61c88647;

    public static void main(String[] args) {
        // 初始容量（2的幂）
        int size = 16;
        for (int i = 0; i < size; i++) {
            int hash = i * HASH_INCREMENT;
            // 取模运算优化
            int index = hash & (size - 1);
            System.out.printf("ThreadLocal-%d → 索引: %d%n", i, index);
        }
    }
}
