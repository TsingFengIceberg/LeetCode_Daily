public class Solution2 {
    /**
     * O(1) 空间复杂度解法 (原地算法)
     */
    public void setZeroes(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        boolean row0HasZero = false;
        boolean col0HasZero = false;

        // 步骤 1: 检查第一行和第一列
        // 遍历第 0 行 (列号 j 从 0 到 n-1)
        for (int j = 0; j < n; j++) {
            if (matrix[0][j] == 0) {
                row0HasZero = true;
                break;
            }
        }
        // 遍历第 0 列 (行号 i 从 0 到 m-1)
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                col0HasZero = true;
                break;
            }
        }

        // 步骤 2: 使用第一行和第一列作为“草稿纸”
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        // 步骤 3: 根据“草稿纸”把矩阵内部置 0
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        // 步骤 4: 最后处理第一行和第一列
        if (row0HasZero) {
            for (int j = 0; j < n; j++) {
                matrix[0][j] = 0;
            }
        }
        if (col0HasZero) {
            for (int i = 0; i < m; i++) {
                matrix[i][0] = 0;
            }
        }
    }
}