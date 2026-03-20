import java.util.Arrays;

public class Solution3 {
    /**
     * 终极优化版：一维数组降维法 (击败 99% 耗时 1-2ms)
     */
    public int[][] merge(int[][] intervals) {
        int n = intervals.length;
        // 提取所有的起点和终点到一维数组（基本数据类型）
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }

        // Java 性能黑科技：对基本类型数组排序使用的是 Dual-Pivot Quicksort
        // 没有任何 Lambda 开销，没有任何对象解引用开销！
        Arrays.sort(starts);
        Arrays.sort(ends);

        int[][] res = new int[n][2];
        int idx = 0;
        
        // j 用于记录当前正在合并的区间的【起点】在 starts 数组中的索引
        for (int i = 0, j = 0; i < n; i++) {
            // 断层判定条件：
            // 1. 如果 i 已经是最后一个元素了，必须闭合当前区间。
            // 2. 如果当前的终点 ends[i] < 下一个起点 starts[i + 1]，说明区间在这里断开了！
            if (i == n - 1 || ends[i] < starts[i + 1]) {
                // 此时，从 j 开始的一连串区间，完美融合为一个区间
                res[idx][0] = starts[j]; // 闭合区间的起点
                res[idx][1] = ends[i];   // 闭合区间的终点
                idx++;
                // 推进 j，将其指向下一个新合并区间的起点
                j = i + 1; 
            }
        }

        // 依然利用 Arrays.copyOf 截取有效部分
        return Arrays.copyOf(res, idx);
    }
}