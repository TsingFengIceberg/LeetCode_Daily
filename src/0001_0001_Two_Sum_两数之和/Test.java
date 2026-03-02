import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        // 因为在同一个文件夹下，这里可以直接 new Solution
        Solution sol = new Solution();

        System.out.println("--- LeetCode 第 1 题测试开始 ---");

        // 示例 1
        int[] res1 = sol.twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println("Case 1: " + Arrays.toString(res1));

        // 示例 2
        int[] res2 = sol.twoSum(new int[]{3, 2, 4}, 6);
        System.out.println("Case 2: " + Arrays.toString(res2));

        // 示例 3
        int[] res3 = sol.twoSum(new int[]{3, 3}, 6);
        System.out.println("Case 3: " + Arrays.toString(res3));
    }
}