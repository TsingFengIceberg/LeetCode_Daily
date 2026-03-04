public class Solution1 {
    public int rob(int[] nums) {
        // 【面试官的善意提醒】：在真实工程中，如果 nums 为 null，
        // 这一行在判空之前就会抛出 NullPointerException。
        // 但作为你的解题草稿，它完美展现了你开辟 O(n) dp 数组的思路。
        int[] sum = new int[nums.length]; 
        
        // 边界条件 1：空数组，没钱可偷
        if (nums == null || nums.length == 0) {
            return 0;
        }
        // 边界条件 2：只有 1 间房，别无选择，只能偷这间
        else if (nums.length == 1) {
            return nums[0];
        }
        // 边界条件 3：只有 2 间房，因为会触发警报，只能挑金额大的那间偷
        else if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        // 核心动态规划逻辑：房屋数量 >= 3
        else {
            // sum[i] 的定义：偷窃到第 i 间房屋时，能获取的最高总金额
            // 初始状态化：第 0 间房的最大收益就是它本身的金额
            sum[0] = nums[0];
            // 初始状态化：到第 1 间房时的最大收益，是第 0 间和第 1 间里的最大值
            sum[1] = Math.max(nums[0], nums[1]);
            
            // 从第 3 间房（下标 2）开始，进行状态转移
            for (int i = 2; i < nums.length; i++) {
                // 核心状态转移方程！
                // 面对第 i 间房，你有两个选择，取其中的最大值：
                // 选择 1（不偷）：保持上一间房的最高收益 sum[i - 1]
                // 选择 2（偷）：当前房间金额 nums[i] + 上上间房的最高收益 sum[i - 2]
                sum[i] = Math.max(sum[i - 1], sum[i - 2] + nums[i]);
            }
            // 遍历结束，数组最后一个元素就是整条街的最高收益
            return sum[nums.length - 1];
        }
    }
}