大厂面试官收到！恭喜你顺利拿下这道经典面试题，并且斩获了 **100.00%** 的时间击败率，这是非常漂亮的大厂手撕战绩！

为你整理了这道题的详细复盘笔记，以及更新后的刷题进度表。请将以下内容补充到你的个人知识库中，反复消化。

---

## 📘 LeetCode 53. 最大子数组和 - 面试复盘笔记

### 1. 题目核心痛点与思维误区

* **直觉陷阱**：初学者常以为“加上当前的数字让总和变大就保留，变小就舍弃（另起炉灶）”。
* **反例**：对于 `[5, -2, 10]`，遇到 `-2` 时总和变小，如果舍弃，就会完美错过后面的 `10`，导致最终和为 `10` 而不是正确的 `13`。

### 2. 解法一：动态规划（Kadane 算法）

这是本题的最优解，也是面试中要求第一时间写出的代码。

* **核心心法（“资产”与“负债”）**：
* 判断是否“另起炉灶”的标准，**取决于前面的累加和（`currentSum`）是正数还是负数**。
* 如果前面的累加和是**正数（资产）**：哪怕当前数字是负数，加上前方的资产也会比单干要大，必须“抱大腿”。
* 如果前面的累加和是**负数（负债）**：此时果断从当前元素“另起炉灶”，因为加上负债只会让自己变得更小。


* **时间与空间复杂度**：
* **Time Complexity**: $O(n)$，只需遍历一次数组。
* **Space Complexity**: $O(1)$，仅需常数级别的变量存储状态。



**核心 Java 代码实现**：

```java
public class Solution1 {
    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int maxSum = nums[0];
        int currentSum = 0;
        
        for (int i = 0; i < nums.length; i++) {
            // 如果前面的和是负债（<0），则另起炉灶；否则抱大腿累加
            if (currentSum < 0) {
                currentSum = nums[i];
            } else {
                currentSum += nums[i];
            }
            // 挑战全局最大值
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
        }
        return maxSum;
    }
}

```

### 3. 解法二：分治法（大厂高频 Follow-up）

面试官要求写分治法，**往往是为了考察你对“区间合并（线段树）”的理解**，用于应对海量数据的频繁修改与查询场景。

* **核心心法（“建桥”逻辑）**：
* 将数组从中间 `mid` 劈开，最大子数组要么完全在左边，要么完全在右边，要么**横跨中间**。
* **横跨中间的计算方式绝对不能是左右最大值直接相加**，而必须像建桥一样，从桥墩（`mid`）分别向左、向右强行延伸，寻找“左半桥极限”和“右半桥极限”，最后拼接。


* **时间与空间复杂度**：
* **Time Complexity**: $O(n \log n)$。
* **Space Complexity**: $O(\log n)$，主要是递归调用栈的深度。



**核心 Java 代码实现**：

```java
public class Solution2 {
    public int maxSubArray(int[] nums) {
        return maxSubArrayHelper(nums, 0, nums.length - 1);
    }

    private int maxSubArrayHelper(int[] nums, int left, int right) {
        if (left == right) return nums[left];

        int mid = left + (right - left) / 2;
        int leftMax = maxSubArrayHelper(nums, left, mid);
        int rightMax = maxSubArrayHelper(nums, mid + 1, right);
        int crossMax = crossSum(nums, left, right, mid);

        return Math.max(Math.max(leftMax, rightMax), crossMax);
    }

    private int crossSum(int[] nums, int left, int right, int mid) {
        // 构建左半桥 (必须以 mid 结尾)
        int leftSubSum = Integer.MIN_VALUE;
        int currentSum = 0;
        for (int i = mid; i >= left; i--) {
            currentSum += nums[i];
            leftSubSum = Math.max(leftSubSum, currentSum);
        }

        // 构建右半桥 (必须以 mid+1 开头)
        int rightSubSum = Integer.MIN_VALUE;
        currentSum = 0;
        for (int i = mid + 1; i <= right; i++) {
            currentSum += nums[i];
            rightSubSum = Math.max(rightSubSum, currentSum);
        }

        return leftSubSum + rightSubSum;
    }
}

```

### 4. 大厂面试实战变形考点总结

* **变形 1：返回起止下标**。不仅要算出最大和，还需要额外维护 `start`, `end`, `tempStart` 指针来记录区间。
* **变形 2：高频单点修改与区间查询**。引入线段树（Segment Tree），利用分治法中“跨界相加”的思想维护每个区间的状态，将单次操作降维至 $O(\log n)$。
* **相关必刷变种题**：LeetCode 152. 乘积最大子数组、LeetCode 918. 环形子数组的最大和。
