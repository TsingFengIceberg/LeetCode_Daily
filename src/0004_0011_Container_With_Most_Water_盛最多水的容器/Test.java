/**
 * 本地性能基准测试类
 */
public class Test {
    public static void main(String[] args) {
        Solution1 s1 = new Solution1();
        Solution2 s2 = new Solution2();
        
        // 构造测试用例
        int[] testCase1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int[] testCase2 = {1, 1};
        
        // 1. 正确性校验
        System.out.println("--- 正确性校验 ---");
        System.out.println("Solution1 测试1 (期望49): " + s1.maxArea(testCase1));
        System.out.println("Solution2 测试1 (期望49): " + s2.maxArea(testCase1));
        System.out.println("Solution1 测试2 (期望1): " + s1.maxArea(testCase2));
        System.out.println("Solution2 测试2 (期望1): " + s2.maxArea(testCase2));
        
        // 2. 性能压测 (模拟 LeetCode 评测机的海量调用)
        System.out.println("\n--- 性能基准测试 (执行 1000 万次) ---");
        int iterations = 10_000_000;
        
        // 测试 Solution1 耗时
        long start1 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            s1.maxArea(testCase1);
        }
        long end1 = System.nanoTime();
        
        // 测试 Solution2 耗时
        long start2 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            s2.maxArea(testCase1);
        }
        long end2 = System.nanoTime();
        
        // 输出结果对比
        System.out.printf("Solution1 (基础版) 耗时: %.2f 毫秒\n", (end1 - start1) / 1_000_000.0);
        System.out.printf("Solution2 (极限版) 耗时: %.2f 毫秒\n", (end2 - start2) / 1_000_000.0);
        System.out.println("如果是在真实的 LeetCode 评测中，Solution2 的常数项优化能帮你跨越 5ms 到 1ms 的鸿沟！");
    }
}