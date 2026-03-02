import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        Solution1 sol1 = new Solution1(); 
        Solution2 sol2 = new Solution2(); 
        Solution3 sol3 = new Solution3(); 
        Solution4 sol4 = new Solution4(); // 硬件调优版

        System.out.println("====== LeetCode 第 283 题：移动零 四版本极限测试 ======\n");

        int[][] testCases = {
            {0, 1, 0, 3, 12},           
            {0},                        
            {1, 2, 3, 4, 5},            
            {0, 0, 0, 0, 0},            
            {4, 2, 4, 0, 0, 3, 0, 5, 1, 0} 
        };

        for (int i = 0; i < testCases.length; i++) {
            int[] original = testCases[i];
            System.out.println("【示例 " + (i + 1) + "】 原数组: " + Arrays.toString(original));

            int[] arr1 = Arrays.copyOf(original, original.length);
            int[] arr2 = Arrays.copyOf(original, original.length);
            int[] arr3 = Arrays.copyOf(original, original.length);
            int[] arr4 = Arrays.copyOf(original, original.length);

            // V1
            long start1 = System.nanoTime();
            sol1.moveZeroes(arr1);
            long end1 = System.nanoTime();
            System.out.println("  [V1 - 常规补零] 耗时: " + (end1 - start1) + " ns");

            // V2
            long start2 = System.nanoTime();
            sol2.moveZeroes(arr2);
            long end2 = System.nanoTime();
            System.out.println("  [V2 - 无脑交换] 耗时: " + (end2 - start2) + " ns");

            // V3
            long start3 = System.nanoTime();
            sol3.moveZeroes(arr3);
            long end3 = System.nanoTime();
            System.out.println("  [V3 - 极致微操] 耗时: " + (end3 - start3) + " ns");

            // V4
            long start4 = System.nanoTime();
            sol4.moveZeroes(arr4);
            long end4 = System.nanoTime();
            System.out.println("  [V4 - 硬件调优] 耗时: " + (end4 - start4) + " ns\n");
        }
    }
}