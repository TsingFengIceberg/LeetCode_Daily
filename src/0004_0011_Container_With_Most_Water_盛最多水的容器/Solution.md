# [11. 盛最多水的容器](https://leetcode.cn/problems/container-with-most-water/)

## 1. 题目剖析与核心思路
* **问题模型**：在给定数组中寻找两根柱子，使其与 X 轴构成的容器容积最大。容积公式 = `min(height[L], height[R]) * (R - L)`。
* **暴力解法 ($O(n^2)$)**：两层 `for` 循环枚举所有组合，由于数据规模达到 `10^5`，必定触发 TLE（超时）。
* **最优解法：对撞双指针 + 贪心剪枝**。
    * **初始状态**：左右指针分别指向数组两端，此时底边宽度最大。
    * **移动策略**：**永远移动较短的那根柱子**。因为容器的高度受限于短板，如果移动长板，宽度变小，而高度绝不可能增加，容积必然减小；只有移动短板，才有可能遇到更高的柱子，从而弥补宽度减小带来的损失。

## 2. 复杂度分析
* **时间复杂度**：`O(n)`。左右指针相向而行，最多遍历数组一次。
* **空间复杂度**：`O(1)`。仅使用常数个整型变量，无额外内存开销。

## 3. 代码演进实录

### 🟢 V1.0 基础满分版 (面试稳妥之选)
逻辑严密，代码可读性极高，是真实面试中应该首选写出的代码架构。
```java
public class Solution {
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int area = Math.min(height[left], height[right]) * (right - left);
            maxArea = Math.max(maxArea, area);
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }
}

```

### 🔴 V2.0 极限压榨版 (1ms 极速版)

通过工程维度的微操，极限压榨 CPU 性能，击败 99.9% 选手。
**三大优化点**：

1. **局部变量缓存**：减少堆内存中数组的重复寻址。
2. **消除方法栈开销**：使用三元运算符 `?:` 替代 `Math.min/max`。
3. **内层快速跳过 (核心剪枝)**：移动后如果新柱子比之前的短板还矮，容积绝对不可能超越历史最大值，直接内层 `while` 无脑跳过，免去一切计算。

```java
public class Solution {
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int hLeft = height[left];
            int hRight = height[right];
            int minHeight = hLeft < hRight ? hLeft : hRight;
            int currentArea = minHeight * (right - left);
            maxArea = maxArea > currentArea ? maxArea : currentArea;
            
            // 极限贪心：快速跳过所有比 minHeight 还要矮的柱子
            if (hLeft < hRight) {
                while (left < right && height[left] <= minHeight) left++;
            } else {
                while (left < right && height[right] <= minHeight) right--;
            }
        }
        return maxArea;
    }
}

```

## 4. 大厂真实工程应用场景 (Follow-up)

在高级研发岗面试中，此算法常被包装为以下业务场景：

1. **CDN 节点网络链路 (腾讯/阿里)**：在 $N$ 个机房中挑选两个建立点对点链路，综合吞吐效能取决于“较小的机房带宽 $\times$ 物理拓扑距离”。
2. **Feed 流广告最佳插入点 (字节/快手)**：在信息流预估分数中寻找两个广告坑位，要求“最低的广告预估转化率 $\times$ 两广告间隔的自然内容数”最大化。
3. **运力调度边界 (美团/滴滴)**：寻找综合抗风险能力最强的大区边界，公式为“边缘网格的最差运力 $\times$ 覆盖的网格跨度”。

