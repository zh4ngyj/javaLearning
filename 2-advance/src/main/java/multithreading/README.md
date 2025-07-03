## ThreadLocal(TL)

> TL是Java中线程局部变量的实现，它为每个线程都创建一个变量副本，保证多线程场景下，变量的线程安全。
> InheritableTheadLocal(ITL)是JDK提供的TL的增强版
> TransmittableThreadLocal(TTL)是阿里开源的ITL增强版

### 带着问题学习

1. 线程局部变量是上面意思？
2. ThreadLocalMap扩容机制是什么？
3. ThreadLocal中的key是弱引用，发生GC会怎么样？
4. 有了ThreadLocal了，为什么还要InheritableThreadLocal？
5. 有了ITL了，为什么还要TransmittableThreadLocal?
6. TL、ITL、TTL应用场景分别有哪些？

### TL

每个Thread线程有一个自己的ThreadLocalMap，ThreadLocal对象作为键，value作为值，存储在ThreadLocalMap中。
实际上key并不是ThreadLocal本身，而是它的一个弱引用。
ThreadLocalMap有点类似HashMap的结构，只是HashMap是由数组+链表实现的，而ThreadLocalMap中只有数组，并没有链表结构。
ThreadLocalMap的Entry， 它的key是ThreadLocal<?> k ，继承自WeakReference， 也就是我们常说的弱引用类型[^1]。


```java
// ThreadLocal源码分析：

/**
 * 创建一个线程局部变量。变量的初始值通过调用 {@code Supplier} 的 {@code get} 方法确定。
 *
 * @param <S> 线程局部变量的值类型
 * @param supplier 用于确定初始值的 Supplier
 * @return 新的线程局部变量
 * @throws NullPointerException 如果指定的 supplier 为 null
 * @since 1.8
 *
 * 这是 Java 8 引入的静态工厂方法，用于创建带有初始值的 ThreadLocal。
 * 通过 Supplier 函数式接口实现懒加载：初始值在首次调用 get() 时才计算。
 * 传统做法需重写 initialValue()，此方法提供了更简洁的替代方案。
 */
public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
    return new SuppliedThreadLocal<>(supplier);
}

/**
 * 返回当前线程在此线程局部变量中的值。若当前线程未设置过值，则先初始化为 {@link #initialValue} 方法的返回值。
 *
 * @return 当前线程的线程局部变量值
 * 
 * 核心流程：
 * 获取当前线程的 ThreadLocalMap（本质是定制化的 HashMap）。
 * 以当前 ThreadLocal 实例为键查找值。
 * 若未找到值，调用 setInitialValue() 初始化（触发 initialValue() 或 Supplier）。
 * 线程隔离：每个线程有独立的存储空间，通过线程对象关联 ThreadLocalMap。
 */
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}

/**
 * 设置当前线程在此线程局部变量中的值为指定值。大多数子类无需重写此方法，仅依赖 {@link #initialValue} 即可。
 *
 * @param value 要存储到当前线程的线程局部变量副本的值
 * <p>              
 * 将当前 ThreadLocal 实例作为键，value 作为值存入当前线程的 ThreadLocalMap。
 * 若线程首次使用该 ThreadLocal，会创建其专属的 ThreadLocalMap。
 */
public void set(T value) {
    // 获取当前线程
    Thread t = Thread.currentThread();
    // 获取当前线程的TLM
    ThreadLocalMap map = getMap(t);
    //如果不为空
    if (map != null) {
        map.set(this, value);
    } else {
        createMap(t, value);
    }
}

/**
 * 移除当前线程在此线程局部变量中的值。后续调用 {@link #get} 时将重新初始化。
 * 注意：可能多次调用 {@code initialValue} 方法。
 *
 * @since 1.5
 * 
 * 作用：显式删除当前线程中该 ThreadLocal 的值，避免内存泄漏（尤其在线程池中）。
 * 删除后再次调用 get() 会重新初始化（调用 initialValue 或 Supplier）。
 */
public void remove() {
    ThreadLocalMap m = getMap(Thread.currentThread());
    if (m != null) {
        m.remove(this);
    }
}


```

### ITL

### TTL

### 应用场景
1. 线程上下文
2. 数据库连接
3. 分布式traceId传递

### 扩展知识

1. 黄金分割数散列、斐波那契数散列
~~~java
// 黄金分割数步长会使对象在哈希表中均匀分散，减少哈希冲突并提高散列均匀性

// 特性代码
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
~~~
2. 强引用、软引用、弱引用、虚引用
3. 高效取模方法
前提条件：table.length为2的幂
int i = key.threadLocalHashCode % table.length;
上面的取模运算等价与下面更高效的位运算(&)：
int i = key.threadLocalHashCode & (table.length - 1);

### 回答

### 脚注

[^1]: 强引用、软引用、弱引用、虚引用
