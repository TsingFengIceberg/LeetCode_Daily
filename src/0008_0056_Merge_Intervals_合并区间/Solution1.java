// 引入必要的工具类
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution1 {
    public int[][] merge(int[][] intervals) {
        // 按照每个区间的起点 intervals[i][0] 进行升序排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        /* * 面试官工程化提示：
        * 很多初学者会写成 (a, b) -> a[0] - b[0]，
        * 虽然在这道题的数据范围（0 到 10^4）下不会出错，
        * 但如果数值接近 Integer.MAX_VALUE 或 MIN_VALUE，减法会导致越界溢出！
        * 使用 Integer.compare() 是大厂极度推荐的防御性编程规范。
        */

        List<int[]> merged = new ArrayList<>();

        for (int[] interval : intervals) {
            // 提取当前正在遍历的区间的起点和终点
            int currentStart = interval[0];
            int currentEnd = interval[1];

            // 如果 merged 是空的，说明是第一个区间，直接放进去
            if (merged.isEmpty()) {
                merged.add(new int[]{currentStart, currentEnd});
            } else {
                // 核心难点：获取我们结果集中“最后一个区间”的引用
                int[] lastMergedInterval = merged.get(merged.size() - 1);
                int lastEnd = lastMergedInterval[1]; // 取出最后一个区间的终点
                
                // --------------------------------------------------
                // 请在此处填补你的“合并逻辑”：
                // 比较 lastEnd 和 currentStart，判断是否重叠。
                // 如果重叠了，lastMergedInterval[1] 应该更新为什么？
                // 如果没重叠，应该把 interval 怎么处理？
                if (lastEnd >= currentStart) {
                    lastMergedInterval[1] = Math.max(lastEnd, currentEnd);
                } else {
                    merged.add(new int[]{currentStart, currentEnd});
                }
                // --------------------------------------------------
            }
        }

        // 最后，将 List 转换为二维数组返回
        return merged.toArray(new int[merged.size()][]);
    }
}
