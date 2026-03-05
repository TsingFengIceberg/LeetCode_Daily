import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        // 构造测试数据：10万级别大数组，模拟 LeetCode 极限 Case
        int N = 100000;
        int K = 45678; // 随机一个较大的 K
        
        int[] original = new int[N];
        for (int i = 0; i < N; i++) {
            original[i] = i;
        }

        // 为了保证测试公平，每次测试都拷贝一份全新的数组
        int[] test1 = Arrays.copyOf(original, N);
        int[] test2 = Arrays.copyOf(original, N);
        int[] test3 = Arrays.copyOf(original, N);
        int[] test4 = Arrays.copyOf(original, N);

        System.out.println("=== 轮转数组 (N=" + N + ", K=" + K + ") 性能测试开始 ===");

        // 1. 测试 Solution1（三次翻转法）
        Solution1 s1 = new Solution1();
        long start1 = System.nanoTime();
        s1.rotate(test1, K);
        long end1 = System.nanoTime();
        System.out.printf("Solution1 (翻转法): 耗时 %.3f ms\n", (end1 - start1) / 1000000.0);

        // 3. 测试 Solution3（额外数组法）
        Solution3 s3 = new Solution3();
        long start3 = System.nanoTime();
        s3.rotate(test3, K);
        long end3 = System.nanoTime();
        System.out.printf("Solution3 (额外数组): 耗时 %.3f ms\n", (end3 - start3) / 1000000.0);

        // 4. 测试 Solution4（环状替换法）
        Solution4 s4 = new Solution4();
        long start4 = System.nanoTime();
        s4.rotate(test4, K);
        long end4 = System.nanoTime();
        System.out.printf("Solution4 (环状替换): 耗时 %.3f ms\n", (end4 - start4) / 1000000.0);

        // 2. 测试 Solution2（暴力冒泡法）- 放在最后是因为它极有可能会卡住很久
        Solution2 s2 = new Solution2();
        long start2 = System.nanoTime();
        System.out.println("Solution2 (暴力冒泡) 正在疯狂计算中，请稍候...");
        s2.rotate(test2, K);
        long end2 = System.nanoTime();
        System.out.printf("Solution2 (暴力冒泡): 耗时 %.3f ms\n", (end2 - start2) / 1000000.0);
    }
}