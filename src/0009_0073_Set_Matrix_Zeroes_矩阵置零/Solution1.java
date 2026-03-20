public class Solution1 {
    /**
     * O(m+n) 空间复杂度解法
     * @param matrix 输入的二维矩阵
     */
    public void setZeroes(int[][] matrix) {
        // 大厂规范：防御性编程，判空
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        // 申请两个布尔数组作为备忘录
        boolean[] row = new boolean[m];
        boolean[] col = new boolean[n];

        // ==========================================
        // TODO 1: 第一次遍历矩阵，寻找 0
        // 如果 matrix[i][j] == 0，请将对应的 row[i] 和 col[j] 标记为 true
        // ==========================================
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    row[i] = true;
                    col[j] = true;
                }
            }
        }

        // ==========================================
        // TODO 2: 第二次遍历矩阵，根据备忘录进行置 0 操作
        // 如果 row[i] 或者 col[j] 为 true，请将 matrix[i][j] 置为 0
        // ==========================================
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (row[i] || col[j]) {
                    matrix[i][j] = 0;
                }
            }
        }
    }
}