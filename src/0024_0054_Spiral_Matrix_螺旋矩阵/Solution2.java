import java.util.List;
import java.util.ArrayList;

class Solution2 {
    public List<Integer> spiralOrder(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        
        // 【核心技巧】：方向数组。通过索引 0~3 分别代表右、下、左、上。
        // 例如：dirs[0] 和 dirs[1] 组合 (0, 1) 代表向右走；dirs[1] 和 dirs[2] 组合 (1, 0) 代表向下走
        int[] dirs = {0, 1, 0, -1, 0};
        
        // i, j 是当前坐标；k 是当前方向的索引
        int i = 0, j = 0, k = 0;
        List<Integer> ans = new ArrayList<>();
        
        // 总共需要遍历 m * n 个元素，倒序计数
        for (int h = m * n; h > 0; --h) {
            ans.add(matrix[i][j]);
            
            // 【状态修改】：利用题目约束 [-100, 100]，将走过的元素加上 300 作为“已访问”的墓碑标记
            // 【工程警告】：高并发下极不安全，属于副作用极大的 Hack 写法。
            matrix[i][j] += 300;
            
            // 试探性地往前走一步，计算下一个坐标 (x, y)
            int x = i + dirs[k], y = j + dirs[k + 1];
            
            // 【转向判断】：如果下一步越界了，或者碰到了大于 100 的元素（说明碰到了加过 300 的墓碑）
            if (x < 0 || x >= m || y < 0 || y >= n || matrix[x][y] > 100) {
                // 顺时针转弯：状态机切换下一个方向。使用 % 4 保证方向在 0, 1, 2, 3 之间循环
                k = (k + 1) % 4;
            }
            
            // 真正地更新当前坐标到下一步
            i += dirs[k];
            j += dirs[k + 1];
        }
        
        // 【擦屁股】：遍历结束后，必须把原本修改的脏数据恢复原样，否则会被后人骂
        for (i = 0; i < m; ++i) {
            for (j = 0; j < n; ++j) {
                matrix[i][j] -= 300;
            }
        }
        return ans;
    }
}