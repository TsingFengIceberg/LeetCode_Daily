class Solution1 {
    // 【Code Review: 基础 DP 版本】
    // 时间复杂度: O(N)，只需遍历一次数组。
    // 空间复杂度: O(N)，使用了两个长度为 N 的状态数组，没有进行状态压缩。
    public int maxProduct(int[] nums) {
        int max = Integer.MIN_VALUE;
        int max_dp [] = new int [nums.length];
        int min_dp [] = new int [nums.length];
        
        // 【边界处理】只有一个元素时直接返回
        if (nums.length == 1) return nums[0];
        
        // 【状态初始化】将首个元素作为初始状态，并同步更新全局 max，防止漏判
        max_dp[0] = nums[0];
        min_dp[0] = nums[0];
        max = Math.max(max, max_dp[0]);
        
        for (int i = 1; i < nums.length; i++) {
            // 【状态转移逻辑】利用穷举法实现了严密的 3-way Max/Min 判断逻辑
            if (max_dp[i - 1] * nums[i] > nums[i]) {//此时max_dp[i - 1]和nums[i]（正正）或（负负）或（0负），那么存在min_dp[i]和nums[i]为（负负得正）的情况。
                max_dp[i] = Math.max(max_dp[i - 1] * nums[i], min_dp[i - 1] * nums[i]);
            }
            else {//此时max_dp[i - 1]和nums[i]（正负）或（负正）或（0正)或（nums[i]为0），那么依旧存在min_dp[i-1]和nums[i]为（负负得正）的情况。
                max_dp[i] = Math.max(nums[i], min_dp[i - 1] * nums[i]);
            }
            if (min_dp[i - 1] * nums[i] < nums[i]) {//此时min_dp[i - 1]和nums[i]（正负）或（负正）或（0正），那么存在max_dp[i-1]和nums[i]为（正负）的情况。
                min_dp[i] = Math.min(min_dp[i - 1] * nums[i], max_dp[i - 1] * nums[i]);
            }
            else {//此时min_dp[i - 1]和nums[i]（正正）或（负负）或（0负）或（nums[i]为0），那么存在max_dp[i-1]和nums[i]为（正负）的情况。
                min_dp[i] = Math.min(nums[i], max_dp[i - 1] * nums[i]);
            }
            
            // 【全局最值收集】每一轮独立比对，抓取历史峰值
            max = Math.max(max, max_dp[i]);
        }
        return max;
    }
}