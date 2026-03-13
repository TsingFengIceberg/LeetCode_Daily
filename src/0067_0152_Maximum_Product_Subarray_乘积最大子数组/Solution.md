# [0152. 乘积最大子数组 (Maximum Product Subarray)]

## 🚨 走过的弯路与致命错误复盘
在最初没有看提示的情况下，我直接套用了【53. 最大子数组和】的单维 DP 模板，写出了如下逻辑：
> `if (dp[i - 1] * nums[i] > nums[i]) { dp[i] = dp[i - 1] * nums[i]; } else { dp[i] = nums[i]; }`

这暴露了两个在真实大厂面试中极易踩中的致命陷阱：
1. **加法思维误用（丢弃负数陷阱）**：
   在测试用例 `[-2, 3, -4]` 中，当遍历到 `i = 1`（当前值为 `3`）时，我的代码将 `-2 * 3 = -6` 与 `3` 比较，理所当然地丢弃了 `-6`，记录当前最大值为 `3`。
   **大错特错！** 在乘法的世界里，负数有“负负得正”的魔法。极小的负数遇到下一个负数会瞬间翻盘变成极大的正数。丢弃了前面的负数累积，导致面对下一个 `-4` 时，只能得出 `-12` 和 `-4` 的比较，最终得出了错误答案 `3`，完美错过了全量乘积 `24`。
2. **全局最值初始化遗漏**：
   代码开头写了 `int max = Integer.MIN_VALUE;`，然后直接从 `i = 1` 开始循环并比对 `max = Math.max(max, dp[i])`。
   这导致如果数组是 `[2, -1]`，全局 `max` 从头到尾都没有和 `dp[0]`（即 `2`）比较过，最终错误返回了 `-1`。全局变量的初始化必须与初始状态（如 `nums[0]`）强绑定！

## 💡 核心破局：双状态 DP 与穷举的数学之美
为了应对“极小负数可能翻盘”的特性，必须同时维护两个状态：
* `max_dp`：以当前元素结尾的最大乘积。
* `min_dp`：以当前元素结尾的最小乘积（最极端的负数）。

我在手撕状态转移时，用极其严密的 4 个 `if-else` 分支穷举了所有（正正、负负、正负、0负）的组合情况。这在数学上其实完美等价于从三个候选人中挑最值：
1. `max_dp[i - 1] * nums[i]`
2. `min_dp[i - 1] * nums[i]`
3. `nums[i]` (抛弃历史，自立门户)

## ⚙️ 工业界真实工程实例：量化风控与 Flink 状态压缩流计算
这道题绝非毫无意义的算法八股，它的两种解法（$O(N)$ 空间和 $O(1)$ 空间）在工业界有着直接的真实映射：
1. **量化交易与风险敞口对冲**：
   在金融量化系统实时产生的高频波动率中，往往包含正向收益（做多）和负向乘数（做空）。双状态 DP 中的 `cur_max` 追踪的是“当前做多的极限收益”，而 `cur_min` 追踪的则是“当前做空的最大风险敞口”。遇到市场暴跌（极小的负数乘数）时，`cur_min` 乘以负数瞬间翻红变成巨大正收益，这就是经典的“风险对冲与逆向套利”算法原型。
2. **Flink 无界数据流与 OOM 拯救（$O(1)$ 状态压缩的统治力）**：
   在字节/快手的大数据实时流计算（Stream Processing）中，数据是通过 Kafka 源源不断涌入的。如果使用基础的 $O(N)$ 状态数组，面对每秒几十万条的无界数据流，JVM 内存会在一分钟内 OOM 崩溃。
   我们手写的利用 `prev_max` 和 `prev_min` 交替滚动的 $O(1)$ 极限压缩代码，正是 Apache Flink 中 `ValueState` 状态编程的底层密码——用几个常数级的状态机变量，承接亿级的高并发吞吐计算，做到真正的“零内存暴涨”。

## 💻 最终 Java 代码实现 (包含基础版与极限状态压缩版)

### 版本 1：基础 DP 版本 (空间复杂度 $O(N)$)
```java
class Solution1 {
    // 【Code Review: 基础 DP 版本】
    // 时间复杂度: O(N)，只需遍历一次数组。
    // 空间复杂度: O(N)，使用了两个长度为 N 的状态数组，没有进行状态压缩。
    public int maxProduct(int[] nums) {
        int max = Integer.MIN_VALUE;
        int max_dp [] = new int [nums.length];
        int min_dp [] = new int [nums.length];
        
        // 【边界处理】只有一个元素时直接返回
        if (nums.length == 1) return nums[0];
        
        // 【状态初始化】将首个元素作为初始状态，并同步更新全局 max，防止漏判
        max_dp[0] = nums[0];
        min_dp[0] = nums[0];
        max = Math.max(max, max_dp[0]);
        
        for (int i = 1; i < nums.length; i++) {
            // 【状态转移逻辑】利用穷举法实现了严密的 3-way Max/Min 判断逻辑
            if (max_dp[i - 1] * nums[i] > nums[i]) {//此时max_dp[i - 1]和nums[i]（正正）或（负负）或（0负），那么存在min_dp[i]和nums[i]为（负负得正）的情况。
                max_dp[i] = Math.max(max_dp[i - 1] * nums[i], min_dp[i - 1] * nums[i]);
            }
            else {//此时max_dp[i - 1]和nums[i]（正负）或（负正）或（0正)或（nums[i]为0），那么依旧存在min_dp[i-1]和nums[i]为（负负得正）的情况。
                max_dp[i] = Math.max(nums[i], min_dp[i - 1] * nums[i]);
            }
            if (min_dp[i - 1] * nums[i] < nums[i]) {//此时min_dp[i - 1]和nums[i]（正负）或（负正）或（0正），那么存在max_dp[i-1]和nums[i]为（正负）的情况。
                min_dp[i] = Math.min(min_dp[i - 1] * nums[i], max_dp[i - 1] * nums[i]);
            }
            else {//此时min_dp[i - 1]和nums[i]（正正）或（负负）或（0负）或（nums[i]为0），那么存在max_dp[i-1]和nums[i]为（正负）的情况。
                min_dp[i] = Math.min(nums[i], max_dp[i - 1] * nums[i]);
            }
            
            // 【全局最值收集】每一轮独立比对，抓取历史峰值
            max = Math.max(max, max_dp[i]);
        }
        return max;
    }
}
```

### 版本 2：极限状态压缩版本 (空间复杂度 $O(1)$)
```java
class Solution2 {
    // 【Code Review: 极限状态压缩版本 (滚动变量)】
    // 时间复杂度: O(N)
    // 空间复杂度: O(1)，完美去除了 DP 数组，只利用了 4 个常数级别的指针变量！
    public int maxProduct(int[] nums) {
        int max = Integer.MIN_VALUE;
        
        // 【状态压缩核心】彻底抛弃 max_dp 和 min_dp 数组，采用双指针交替滚动
        int prev_max = nums[0];
        int prev_min = nums[0];
        int cur_max = nums[0];
        int cur_min = nums[0];
        
        if (nums.length == 1) return nums[0];
        max = Math.max(max, prev_max);
        
        for (int i = 1; i < nums.length; i++) {
            // 【状态转移逻辑】完全等价于 Solution1，只需将数组查询替换为 prev 变量
            if (prev_max * nums[i] > nums[i]) {//此时prev_max和nums[i]（正正）或（负负）或（0负），那么存在min_dp[i]和nums[i]为（负负得正）的情况。
                cur_max = Math.max(prev_max * nums[i], prev_min * nums[i]);
            }
            else {//此时prev_max和nums[i]（正负）或（负正）或（0正)或（nums[i]为0），那么依旧存在min_dp[i-1]和nums[i]为（负负得正）的情况。
                cur_max = Math.max(nums[i], prev_min * nums[i]);
            }
            if (prev_min * nums[i] < nums[i]) {//此时prev_min和nums[i]（正负）或（负正）或（0正），那么存在max_dp[i-1]和nums[i]为（正负）的情况。
                cur_min = Math.min(prev_min * nums[i], prev_max * nums[i]);
            }
            else {//此时prev_min和nums[i]（正正）或（负负）或（0负）或（nums[i]为0），那么存在max_dp[i-1]和nums[i]为（正负）的情况。
                cur_min = Math.min(nums[i], prev_max * nums[i]);
            }
            
            max = Math.max(max, cur_max);
            
            // 【关键防污染操作！】必须在当前所有状态计算完毕后，统一向后拨动时间齿轮。
            // 绝不允许在计算 cur_min 时使用刚刚被更新过的 cur_max！
            prev_max = cur_max;
            prev_min = cur_min;
        }
        return max;
    }
}
