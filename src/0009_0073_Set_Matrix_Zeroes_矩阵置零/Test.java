import java.util.Arrays;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Solution1 s1 = new Solution1();
        Solution2 s2 = new Solution2();

        System.out.println("====== 基础正确性测试 ======");
        // 故意构造一个 m != n 的长方形矩阵，测试是否会越界
        int[][] testMatrix = {
            {0, 1, 2, 0},
            {3, 4, 5, 2},
            {1, 3, 1, 5}
        }; // m=3, n=4

        int[][] copy1 = deepCopy(testMatrix);
        int[][] copy2 = deepCopy(testMatrix);

        s1.setZeroes(copy1);
        s2.setZeroes(copy2);

        System.out.println("原始矩阵:");
        printMatrix(testMatrix);
        System.out.println("Solution1 (O(m+n) 空间) 结果:");
        printMatrix(copy1);
        System.out.println("Solution2 (O(1) 空间) 结果:");
        printMatrix(copy2);


        System.out.println("\n====== 大规模性能压测 ======");
        int m = 2000;
        int n = 2000;
        int[][] massiveMatrix = new int[m][n];
        Random random = new Random();
        
        // 随机撒入少量 0
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                massiveMatrix[i][j] = random.nextInt(100) < 2 ? 0 : 1;
            }
        }

        int[][] massiveCopy1 = deepCopy(massiveMatrix);
        int[][] massiveCopy2 = deepCopy(massiveMatrix);

        // 测试 S1
        long start1 = System.nanoTime();
        s1.setZeroes(massiveCopy1);
        long end1 = System.nanoTime();

        // 测试 S2
        long start2 = System.nanoTime();
        s2.setZeroes(massiveCopy2);
        long end2 = System.nanoTime();

        System.out.println("矩阵规模: " + m + " x " + n);
        System.out.println("Solution1 耗时: " + (end1 - start1) / 1_000_000.0 + " ms");
        System.out.println("Solution2 耗时: " + (end2 - start2) / 1_000_000.0 + " ms");
        System.out.println("注意：O(1) 解法不仅省内存，还因为去除了额外数组的频繁寻址，时间上通常也有微弱优势！");
    }

    // 辅助方法：深拷贝矩阵
    private static int[][] deepCopy(int[][] original) {
        int m = original.length;
        int n = original[0].length;
        int[][] copy = new int[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, n);
        }
        return copy;
    }

    // 辅助方法：打印矩阵
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}