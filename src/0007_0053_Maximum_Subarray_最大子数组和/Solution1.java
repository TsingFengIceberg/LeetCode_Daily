public class Solution1 {
    /**
     * 动态规划求最大子数组和 (Kadane 算法)
     * @param nums 输入数组
     * @return 最大连续子数组和
     */
    public int maxSubArray(int[] nums) {
        // 边界条件防御：大厂编码规范第一步，永远先检查入参
        if (nums == null || nums.length == 0) {
            return 0; 
        }

        // maxSum 记录全局最大值，初始化为第一个元素
        int maxSum = nums[0];
        // currentSum 记录以当前元素结尾的连续子数组最大和
        int currentSum = 0;

        for (int i = 0; i < nums.length; i++) {
            // 核心状态转移：“资产”还是“负债”？
            if (currentSum < 0) {
                // 如果前面累加的是负债，果断抛弃，从当前元素另起炉灶
                currentSum = nums[i];
            } else {
                // 如果前面累加的是资产，抱紧大腿，继续累加
                currentSum += nums[i];
            }
            
            // 每次更新 currentSum 后，挑战一下全局最大值记录
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
            
            // 提示：如果追求极致代码行数，上面这个 if-else 可以简写为：
            // currentSum = Math.max(nums[i], currentSum + nums[i]);
            // maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }
}