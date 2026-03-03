public class Test {
    public static void main(String[] args) {
        Solution1 s1 = new Solution1();
        Solution2 s2 = new Solution2();
        
        String testCase1 = "abcabcbb"; // 期望 3
        String testCase2 = "bbbbb";    // 期望 1
        String testCase3 = "pwwkew";   // 期望 3
        
        // 1. 正确性校验
        System.out.println("--- 正确性校验 ---");
        System.out.println("Solution1 示例1 (期望3): " + s1.lengthOfLongestSubstring(testCase1));
        System.out.println("Solution2 示例1 (期望3): " + s2.lengthOfLongestSubstring(testCase1));
        System.out.println("Solution1 示例2 (期望1): " + s1.lengthOfLongestSubstring(testCase2));
        System.out.println("Solution2 示例2 (期望1): " + s2.lengthOfLongestSubstring(testCase2));
        System.out.println("Solution1 示例3 (期望3): " + s1.lengthOfLongestSubstring(testCase3));
        System.out.println("Solution2 示例3 (期望3): " + s2.lengthOfLongestSubstring(testCase3));
        
        // 2. 性能压测 (执行 1000 万次，模拟 LeetCode 评测机和放大 JVM 开销)
        System.out.println("\n--- 性能基准测试 (执行 10,000,000 次) ---");
        int iterations = 10_000_000;
        
        long start1 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            s1.lengthOfLongestSubstring(testCase1);
        }
        long end1 = System.nanoTime();
        
        long start2 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            s2.lengthOfLongestSubstring(testCase1);
        }
        long end2 = System.nanoTime();
        
        System.out.printf("Solution1 (HashMap版) 耗时: %.2f 毫秒\n", (end1 - start1) / 1_000_000.0);
        System.out.printf("Solution2 (int数组版) 耗时: %.2f 毫秒\n", (end2 - start2) / 1_000_000.0);
        System.out.println("在大规模调用下，避免对象创建和哈希计算的 int 数组将带来数倍的性能提升！");
    }
}