# [560. 和为 K 的子数组](https://leetcode.cn/problems/subarray-sum-equals-k/)

## 1. 题目剖析与核心思路演进
* **问题模型**：在可能包含负数的无序数组中，寻找连续子数组，使其和严格等于目标值 $k$。
* **思维误区（暴力解法的破绽）**：
  * 起初容易想到双层循环 $O(n^2)$。但由于**数组中存在负数**，遇到当前和 $> k$ 时**绝对不能提前 `break`**（因为后续加上负数可能再次等于 $k$）。暴力解法极易导致 TLE。
* **终极武器：前缀和 (Prefix Sum) + 哈希表**：
  * 
  * **核心公式**：`preSum[j] - preSum[i-1] = k` $\Rightarrow$ `preSum[i-1] = preSum[j] - k`。
  * **业务隐喻（查账本模型）**：不要用两层循环去往回“找”哪一段加起来等于 $k$。而是在从左到右累加当前总前缀和 `preSum` 时，直接去哈希表（账本）里查：**历史上出现过多少次和为 `preSum - k` 的前缀和？** 查到几次，就代表以当前元素为结尾的有效子数组有几个。

## 2. 大厂必考的边缘场景 (Corner Case)
* **幽灵边界 `map.put(0, 1)`**：
  * 在开始遍历前，必须向哈希表中预置 `<0, 1>`。
  * **原因**：如果从数组索引 $0$ 开始到当前索引 $i$ 的总和恰好等于 $k$。此时我们需要去哈希表中寻找 `preSum - k = 0` 的记录。预置此项，确保这类“完美包含开头的子数组”不会被漏算。

## 3. 复杂度分析
* **时间复杂度**：$O(N)$。单次遍历数组，哈希表查/存操作均摊 $O(1)$。
* **空间复杂度**：$O(N)$。最坏情况下所有前缀和都不相同，哈希表需存储 $N$ 个键值对。

## 4. 代码实录与架构反思

### 🟢 面试标准解法 (HashMap 方案 - 推荐)
工程中最均衡的解法，也是 LeetCode 评测环境下的最优解。
```java
import java.util.HashMap;

public class Solution {
    public int subarraySum(int[] nums, int k) {
        int count = 0, preSum = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        // 核心边界：初始化前缀和为 0 的次数为 1
        map.put(0, 1); 

        for (int i = 0; i < nums.length; i++) {
            preSum += nums[i]; // 1. 计算当前前缀和
            
            // 2. 查账本：历史上存在多少个 preSum - k
            if (map.containsKey(preSum - k)) {
                count += map.get(preSum - k);
            }
            
            // 3. 记账：将当前前缀和登记入册
            map.put(preSum, map.getOrDefault(preSum, 0) + 1);
        }
        return count;
    }
}

```

### 🔴 架构师压测思考：为什么不用基本类型数组替代 HashMap？

* **尝试**：曾尝试使用长度 `65536` 的 `int[]` 配合线性探测法（开放寻址）手写 Primitive Map，旨在消除 `Integer` 装箱拆箱带来的对象开销。
* **结果复盘**：在本地压测亿级长数组时性能碾压 `HashMap`；但在 LeetCode 评测机中反而变慢。
* **原因**：LeetCode 包含大量极短的测试用例。每次方法调用都初始化 $65536$ 长度的数组，**初始化开销（$O(Capacity)$）远超其带来的寻址优势（$O(N)$）**。这在真实的系统架构选型中，是一次极其生动的“**因地制宜**”性能调优反面案例。

