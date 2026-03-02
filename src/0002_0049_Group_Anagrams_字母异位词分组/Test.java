import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        // 1. 实例化我们的【四个】解法对象
        Solution1 sol1 = new Solution1();     // 基础版：KlogK 排序法
        Solution2 sol2 = new Solution2();     // 进阶版：int[] 计数 + Arrays.toString (实测被常数拖累)
        Solution3 sol3 = new Solution3();     // 绝杀版：char[] 计数 + String 内存拷贝
        Solution4 sol4 = new Solution4();     // 极客版：Solution3 + 预设容量 + 干掉 Lambda
        
        System.out.println("====== LeetCode 第 49 题：四版本终极演进史 ======\n");

        // 2. 准备测试用例
        String[][] testCases = {
            {"eat", "tea", "tan", "ate", "nat", "bat"}, 
            {""},                                       
            {"a"}                                       
        };

        // 3. 遍历运行所有的测试用例
        for (int i = 0; i < testCases.length; i++) {
            String[] strs = testCases[i];
            System.out.println("【示例 " + (i + 1) + "】 输入: " + Arrays.toString(strs));
            
            // --- 解法 1 ---
            long start1 = System.nanoTime(); 
            List<List<String>> res1 = sol1.groupAnagrams(strs);
            long end1 = System.nanoTime();   
            System.out.println("  [V1 - 排序法]       输出: " + res1 + " \n      耗时: " + (end1 - start1) + " ns");

            // --- 解法 2 ---
            long start2 = System.nanoTime(); 
            List<List<String>> res2 = sol2.groupAnagrams(strs);
            long end2 = System.nanoTime();   
            System.out.println("  [V2 - int[]计数]    输出: " + res2 + " \n      耗时: " + (end2 - start2) + " ns");
            
            // --- 解法 3 ---
            long start3 = System.nanoTime(); 
            List<List<String>> res3 = sol3.groupAnagrams(strs);
            long end3 = System.nanoTime();   
            System.out.println("  [V3 - char[]优化]   输出: " + res3 + " \n      耗时: " + (end3 - start3) + " ns");

            // --- 解法 4 ---
            long start4 = System.nanoTime(); 
            List<List<String>> res4 = sol4.groupAnagrams(strs);
            long end4 = System.nanoTime();   
            System.out.println("  [V4 - 终极微操版]   输出: " + res4 + " \n      耗时: " + (end4 - start4) + " ns\n");
            
            System.out.println("------------------------------------------------------");
        }
        
        System.out.println("✅ 测试完毕！");
        System.out.println("💡 导师提示：");
        System.out.println("1. 本地 ns 级测试受 JVM 预热影响较大，真实差距在数据量达 10000 时才会彻底显现。");
        System.out.println("2. 将 Solution4 提交到 LeetCode，享受极致的毫秒级快感吧！");
    }
}