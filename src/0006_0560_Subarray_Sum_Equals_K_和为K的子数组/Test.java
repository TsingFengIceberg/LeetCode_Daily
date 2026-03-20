import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Solution1 s1 = new Solution1();
        Solution2 s2 = new Solution2();
        
        int[] testCase1 = {1, 1, 1}; int k1 = 2;
        int[] testCase2 = {1, 2, 3}; int k2 = 3;
        
        // 1. 正确性校验
        System.out.println("--- 正确性校验 ---");
        System.out.println("Solution1 示例1 (期望2): " + s1.subarraySum(testCase1, k1));
        System.out.println("Solution2 示例1 (期望2): " + s2.subarraySum(testCase1, k1));
        System.out.println("Solution1 示例2 (期望2): " + s1.subarraySum(testCase2, k2));
        System.out.println("Solution2 示例2 (期望2): " + s2.subarraySum(testCase2, k2));
        
        // 2. 构造 LeetCode 极限压测数据 (长度 20000 的随机数组)
        int n = 20000;
        int[] hugeArray = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            hugeArray[i] = random.nextInt(2000) - 1000; // -1000 到 1000
        }
        int targetK = random.nextInt(10000);
        
        // 3. 性能压测 (执行 1000 次长数组计算，放大开销差距)
        System.out.println("\n--- 极限性能压测 (执行 1000 次长度 20000 的数组计算) ---");
        int iterations = 1000;
        
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            s1.subarraySum(hugeArray, targetK);
        }
        long end1 = System.currentTimeMillis();
        
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            s2.subarraySum(hugeArray, targetK);
        }
        long end2 = System.currentTimeMillis();
        
        System.out.printf("Solution1 (官方 HashMap版) 总耗时: %d 毫秒\n", (end1 - start1));
        System.out.printf("Solution2 (手写 Primitive Hash) 总耗时: %d 毫秒\n", (end2 - start2));
        System.out.println("在真实的服务器高并发场景下，消除对象装箱能极大缓解垃圾回收压力！");
    }
}