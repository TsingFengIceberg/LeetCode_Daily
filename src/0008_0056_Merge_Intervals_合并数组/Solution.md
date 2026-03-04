# 📘 LeetCode 56. 合并区间 (Merge Intervals) - 面试复盘笔记

## 1. 题目核心痛点与解决策略
* **直觉陷阱**：面对乱序的区间，如果直接暴力两两比较，时间复杂度将达到极高的 $O(n^2)$。
* **破局核心（贪心+排序）**：必须先让区间按照某种规律“排队”。当所有区间按**起点（start）升序排序**后，我们只需要从左到右扫一遍，每次只需比较“当前区间的起点”与“结果集中最后一个区间的终点”。

## 2. 核心状态转移逻辑
假设结果集中最后一个区间为 `lastMerged`，当前遍历到的区间为 `current`：
* **重叠判定**：如果 `lastMerged[1] >= current[0]`，说明发生重叠。
* **合并操作**：将最后一个区间的终点向右延伸，即 `lastMerged[1] = Math.max(lastMerged[1], current[1])`。
* **无重叠操作**：直接将 `current` 作为一个全新的独立区间加入结果集。

## 3. Java 工程化演进与代码实现
在刷题过程中，我们经历了三次性能迭代，深刻体会了 JVM 的底层特性：

### 🐌 版本一：面向对象常规写法 (ArrayList + Integer.compare)
* **特点**：易读性极强，适合初学者。但动态数组扩容、装箱拆箱以及方法调用带来了极高的开销。
* **代码**：
```java
public int[][] mergeV1(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
    List<int[]> merged = new ArrayList<>();
    for (int[] interval : intervals) {
        if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
            merged.add(new int[]{interval[0], interval[1]});
        } else {
            merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], interval[1]);
        }
    }
    return merged.toArray(new int[merged.size()][]);
}

```

### 🚀 版本二：原生数组原地优化版 (LeetCode 综合最优，击败 91.58% 内存)

* **特点**：抛弃 ArrayList，直接预估最大容量使用二维数组 `res`，并通过指针对原数组进行原地操作。Lambda 表达式中直接使用减法 `a[0] - b[0]` 避免了 `Integer.compare` 的调用开销。
* **代码**：

```java
public int[][] mergeV2(int[][] intervals) {
    if (intervals == null || intervals.length <= 1) return intervals;
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    
    int[][] res = new int[intervals.length][2];
    int idx = -1;
    for (int[] interval : intervals) {
        if (idx == -1 || interval[0] > res[idx][1]) {
            res[++idx] = interval; 
        } else {
            res[idx][1] = Math.max(res[idx][1], interval[1]);
        }
    }
    return Arrays.copyOf(res, idx + 1);
}

```

### 🏎️ 版本三：基本数据类型降维打击 (应对百万级海量数据)

* **特点**：将二维对象数组拆解为两个一维的基本类型 `int[] starts` 和 `int[] ends`。利用基本类型数组触发 JVM 底层高度优化的双轴快排（Dual-Pivot Quicksort），彻底消除对象指针引用的 Cache Miss。但在 LeetCode 小样本测试中，因数组实例化开销反而表现一般。
* **代码**：

```java
public int[][] mergeV3(int[][] intervals) {
    int n = intervals.length;
    int[] starts = new int[n];
    int[] ends = new int[n];
    for (int i = 0; i < n; i++) {
        starts[i] = intervals[i][0];
        ends[i] = intervals[i][1];
    }
    Arrays.sort(starts);
    Arrays.sort(ends);
    
    int[][] res = new int[n][2];
    int idx = 0;
    for (int i = 0, j = 0; i < n; i++) {
        if (i == n - 1 || ends[i] < starts[i + 1]) {
            res[idx][0] = starts[j];
            res[idx][1] = ends[i];
            idx++;
            j = i + 1; 
        }
    }
    return Arrays.copyOf(res, idx);
}

```

## 4. 大厂真实业务映射 (Follow-ups)

* **视频有效播放时长计算（字节/快手）**：直接套用 LC 56，合并用户反复拖拽进度条产生的重叠播放区间，最后累加所有不重叠区间的 `end - start`。
* **会议室预定问题（阿里/飞书 - LC 253）**：利用版本三的一维数组拆分思路（扫描线算法），将所有起点和终点分别排序，遇到起点并发量 +1，遇到终点并发量 -1，求并发量的最大峰值。
* **骑手/司机派单冲突检测（美团/滴滴 - LC 57/732）**：在流式数据中动态插入区间。若并发极高，需放弃数组，改用 `TreeMap` (红黑树) 维护时间轴进行 $O(\log n)$ 级别的冲突判定。

