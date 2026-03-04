# 📘 LeetCode 198. 打家劫舍 (House Robber) - 面试复盘笔记

## 1. 题目核心痛点与解决策略
* **直觉陷阱**：单纯挑选奇数下标或偶数下标的房屋求和。这是完全错误的，因为最优解可能是“偷第 1、4、7 间房”，并不一定要隔一间偷一间，只要不**相邻**即可。
* **破局核心（动态规划）**：面对任意一间房屋，我们只有两个选择：“偷”或“不偷”。当前的最大收益，取决于历史做出的最优选择。



## 2. 核心状态转移逻辑
假设 `dp[i]` 表示走到第 `i` 间房屋时能获取的最高总金额：
* **选择 A（不偷当前房屋）**：收益等于走到上一间房屋时的最高收益，即 `dp[i-1]`。
* **选择 B（偷当前房屋）**：既然偷了当前房屋，就绝对不能偷上一间。收益等于走到上上间房屋的最高收益加上当前房屋的金额，即 `dp[i-2] + nums[i]`。
* **状态转移方程**：`dp[i] = Math.max(dp[i-1], dp[i-2] + nums[i])`

## 3. Java 工程演进与代码实现

### 🟢 Solution1：基础动态规划（O(n) 空间）
原汁原味的动态规划思路，利用 `sum` 数组记录所有历史状态。
```java
public class Solution1 {
    public int rob(int[] nums) {
        int[] sum = new int[nums.length]; 
        if (nums == null || nums.length == 0) { return 0; }
        else if (nums.length == 1) { return nums[0]; }
        else if (nums.length == 2) { return Math.max(nums[0], nums[1]); }
        else {
            sum[0] = nums[0];
            sum[1] = Math.max(nums[0], nums[1]);
            for (int i = 2; i < nums.length; i++) {
                // 核心转移：取“不偷当前房”和“偷当前房”的最大值
                sum[i] = Math.max(sum[i - 1], sum[i - 2] + nums[i]);
            }
            return sum[nums.length - 1];
        }
    }
}

```

### 🟡 Solution2：状态压缩版（O(1) 空间，最佳战绩：0ms / 击败 84.72% 内存）

由于当前状态只依赖前两个状态，抛弃 O(n) 数组，改用两个变量滚动更新，极大节省内存。

```java
public class Solution2 {
    public int rob(int[] nums) {
        if (nums == null || nums.length == 0) { return 0; }
        else if (nums.length == 1) { return nums[0]; }
        else if (nums.length == 2) { return Math.max(nums[0], nums[1]); }
        else {
            int sump1 = nums[0]; // 记录上上间的最高收益
            int sump2 = Math.max(nums[0], nums[1]); // 记录上一间的最高收益
            int temp;
            for (int i = 2; i < nums.length; i++) {
                temp = sump2;
                sump2 = Math.max(sump2, sump1 + nums[i]);
                sump1 = temp;
            }
            return sump2;
        }
    }
}

```

### 🚀 Solution3：极致优雅大厂版（O(1) 空间，消除 if-else）

通过虚拟两个金额为 0 的起始点，彻底干掉冗长的 Base Case 分支判断，代码极度精简。

```java
public class Solution3 {
    public int rob(int[] nums) {
        int prev1 = 0; // 虚拟上上间
        int prev2 = 0; // 虚拟上一间
        for (int num : nums) {
            int temp = Math.max(prev2, prev1 + num);
            prev1 = prev2;
            prev2 = temp;
        }
        return prev2;
    }
}

```

## 4. 大厂真实业务映射

* **带冲突约束的资源最优分配**：
* **字节/快手**：信息流广告插入策略（防止连续插入广告导致用户流失，求总 eCPM 最大化）。
* **阿里云/腾讯云**：机架服务器容灾升级调度（禁止同时升级物理相邻的服务器，防局部断电/过热）。
* **美团/滴滴**：高并发营销防刷策略（禁止在相邻的时段连续发放大额红包，预留风控清洗时间）。
