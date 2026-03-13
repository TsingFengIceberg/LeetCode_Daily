class Solution2 {
    // 【Code Review: 极限状态压缩版本 (滚动变量)】
    // 时间复杂度: O(N)
    // 空间复杂度: O(1)，完美去除了 DP 数组，只利用了 4 个常数级别的指针变量！
    public int maxProduct(int[] nums) {
        int max = Integer.MIN_VALUE;
        
        // 【状态压缩核心】彻底抛弃 max_dp 和 min_dp 数组，采用双指针交替滚动
        int prev_max = nums[0];
        int prev_min = nums[0];
        int cur_max = nums[0];
        int cur_min = nums[0];
        
        if (nums.length == 1) return nums[0];
        max = Math.max(max, prev_max);
        
        for (int i = 1; i < nums.length; i++) {
            // 【状态转移逻辑】完全等价于 Solution1，只需将数组查询替换为 prev 变量
            if (prev_max * nums[i] > nums[i]) {//此时prev_max和nums[i]（正正）或（负负）或（0负），那么存在min_dp[i]和nums[i]为（负负得正）的情况。
                cur_max = Math.max(prev_max * nums[i], prev_min * nums[i]);
            }
            else {//此时prev_max和nums[i]（正负）或（负正）或（0正)或（nums[i]为0），那么依旧存在min_dp[i-1]和nums[i]为（负负得正）的情况。
                cur_max = Math.max(nums[i], prev_min * nums[i]);
            }
            if (prev_min * nums[i] < nums[i]) {//此时prev_min和nums[i]（正负）或（负正）或（0正），那么存在max_dp[i-1]和nums[i]为（正负）的情况。
                cur_min = Math.min(prev_min * nums[i], prev_max * nums[i]);
            }
            else {//此时prev_min和nums[i]（正正）或（负负）或（0负）或（nums[i]为0），那么存在max_dp[i-1]和nums[i]为（正负）的情况。
                cur_min = Math.min(nums[i], prev_max * nums[i]);
            }
            
            max = Math.max(max, cur_max);
            
            // 【关键防污染操作！】必须在当前所有状态计算完毕后，统一向后拨动时间齿轮。
            // 绝不允许在计算 cur_min 时使用刚刚被更新过的 cur_max！
            prev_max = cur_max;
            prev_min = cur_min;
        }
        return max;
    }
}