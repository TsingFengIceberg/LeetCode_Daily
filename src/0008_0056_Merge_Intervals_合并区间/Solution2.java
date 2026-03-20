import java.util.Arrays;

public class Solution2 {
    /**
     * 优化版：合并区间 (去除 ArrayList 开销，优化排序)
     */
    public int[][] merge(int[][] intervals) {
        // 边界防御
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }

        // 优化点 1：题目约束 starti 最小为 0，最大为 10^4。
        // 所以 a[0] - b[0] 绝对不会发生 Integer 溢出。直接相减比调用 Integer.compare 快！
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // 优化点 2：抛弃 ArrayList，直接使用一个等长的二维数组作为结果容器
        // 最坏情况下，没有区间重叠，结果集大小等于原数组大小
        int[][] res = new int[intervals.length][2];
        
        // idx 作为结果数组 res 的指针，指向最后一个成功放入的区间
        int idx = -1;

        for (int[] interval : intervals) {
            // 如果结果集为空 (idx == -1)，或者当前区间的起点 > 最后一个区间的终点 (无重叠)
            if (idx == -1 || interval[0] > res[idx][1]) {
                // 指针后移，存入新区间
                // 注意：这里直接存入一维数组的引用，极其高效，连 new int[] 都省了！
                res[++idx] = interval; 
            } else {
                // 发生重叠，原地更新最后一个区间的终点
                res[idx][1] = Math.max(res[idx][1], interval[1]);
            }
        }

        // 优化点 3：利用 Arrays.copyOf 只截取数组中有数据的部分返回
        // 长度是 idx + 1
        return Arrays.copyOf(res, idx + 1);
    }
}