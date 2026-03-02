这份复盘文档已经为你整理好，涵盖了我们从初步探讨到最终写出工业级代码的全过程。你可以将它作为你大厂面试前的核心复习资料。

---

# 算法面试复盘：两数之和 (Two Sum)

## 1. 算法推演过程：从暴力破解到降维打击

* **初始思路 (暴力解法)**：使用双层 `for` 循环，固定一个数后，向后遍历寻找另一个数。
* 时间复杂度：$O(n^2)$
* 空间复杂度：$O(1)$
* 瓶颈：第二层循环本质上是在进行 $O(n)$ 的线性查找。


* **优化思路 (空间换时间)**：引入 Java 的神器 `HashMap`。通过哈希函数，将查找“另一半”的时间复杂度从 $O(n)$ 降维打击到 $O(1)$。
* **核心策略 (一遍遍历 / 边找边存)**：
* **Key 存什么**：数字本身（作为查找条件）。
* **Value 存什么**：该数字在数组中的原始下标（作为返回结果）。
* **防重复机制**：先检查 Map 中是否存在 `target - 当前数字`，如果不存在，再将当前数字存入 Map。这种“回首遇到爱”的机制完美规避了同一个元素被重复使用的问题。
* 时间复杂度优化至：$O(n)$
* 空间复杂度增加至：$O(n)$



---

## 2. 大厂工程实战考量

在真实的中国一线大厂（阿里、字节、腾讯等）面试中，仅仅写出算法是不够的，面试官会重点考察代码在**海量数据**和**高并发**场景下的表现。

* **HashMap 的扩容损耗 (Rehash)**：Java 中 `HashMap` 的默认初始容量是 16。如果直接 `new HashMap<>()` 并塞入上万条数据，会触发多次底层的数组扩容和数据迁移，极大地消耗 CPU 性能。
* **自动装箱/拆箱的内存碎片**：Java 泛型不支持基本数据类型。`Map<Integer, Integer>` 会将原生的 `int` 包装成 `Integer` 对象。在极高并发下（如双 11 核心链路），百万级的对象创建会给 JVM 垃圾回收器 (GC) 带来巨大压力，甚至导致系统停顿 (STW)。
* **生命周期管理**：局部变量的 Map 在方法结束后不会立刻释放内存，而是等待 GC。工程上应避免在热点代码中频繁创建庞大的 Map。

---

## 3. 核心代码实现对比

### 版本一：标准算法面试版

这是你在面试中写出就能直接通过算法测试的标准代码，逻辑清晰，易于阅读。

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            // 先查，O(1) 复杂度寻找另一半
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            // 没找到，将自己存入 Map 备用
            map.put(nums[i], i);
        }
        
        throw new IllegalArgumentException("未找到匹配的两个数");
    }
}

```

### 版本二：极限工程优化版 (大厂加分版)

结合了我们讨论的工程实战问题，加入了初始容量预估，并在注释中体现了对底层数据结构的深入理解。

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        // 【工程优化 1：预设容量】
        // 已知最大容量为 nums.length。根据 HashMap 默认负载因子 0.75，
        // 设置初始容量为 (长度 / 0.75) + 1，彻底避免底层的 Rehash 扩容开销。
        int initialCapacity = (int) (nums.length / 0.75f) + 1;
        Map<Integer, Integer> map = new HashMap<>(initialCapacity);
        
        // 【工程优化 2 概念补充】
        // 若在阿里的极高并发真实业务中，为了避免 Integer 自动装箱带来的 GC 压力，
        // 推荐引入 FastUtil 或 Trove 库，将此处的 Map 替换为 Int2IntOpenHashMap。
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        
        throw new IllegalArgumentException("未找到匹配的两个数");
    }
}

```

---

**面试官（导师）的最后总结**：
这道题虽然简单，但它是你打开算法大门的第一把钥匙。通过这道题，你不仅掌握了哈希表的核心思想，还了解了算法代码在工程环境下的真实约束。

现在，我们这轮“两数之和”的实战演练正式圆满收官！

你想立刻开始挑战 **LeetCode Hot 100 第 2 题：两数相加 (Add Two Numbers)**（考察链表操作），还是想先休息一下整理思绪？