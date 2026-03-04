import java.util.Random;

public class Test {
    public static void main(String[] args) {
        // 构造一个规模为 100 万的测试数组
        int size = 10_000_000;
        int[] nums = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            // 随机生成 -50 到 50 的数字
            nums[i] = random.nextInt(101) - 50;
        }

        Solution1 s1 = new Solution1();
        Solution2 s2 = new Solution2();

        // 测试动态规划 (O(n))
        long startDP = System.nanoTime();
        int resultDP = s1.maxSubArray(nums);
        long endDP = System.nanoTime();
        double timeDP = (endDP - startDP) / 1_000_000.0;

        // 测试分治法 (O(n log n))
        long startDC = System.nanoTime();
        int resultDC = s2.maxSubArray(nums);
        long endDC = System.nanoTime();
        double timeDC = (endDC - startDC) / 1_000_000.0;

        System.out.println("数据规模: " + size);
        System.out.println("---------------------------------");
        System.out.println("动态规划结果: " + resultDP + ", 耗时: " + timeDP + " ms");
        System.out.println("分治法结果:   " + resultDC + ", 耗时: " + timeDC + " ms");
        System.out.println("---------------------------------");
        System.out.println("结论：在海量数据下，O(n) 的动态规划在单次执行时具备压倒性优势。");
    }
}