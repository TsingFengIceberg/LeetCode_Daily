class Solution1 {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return n; // 拦截极小数组
        }
        
        // dp[i] 的极其严苛的物理意义：
        // 【必须、一定、强制】以 nums[i] 这个数字为结尾的，最长递增子序列长度！
        int [] dp = new int[n];
        dp [0] = 1;
        int max = 1; // 派出一个“全局星探”，时刻记录历史最高分
        
        for (int i = 1; i < n; i++) {
            dp [i] = 1; // 初始状态：每个人至少可以自己单干，长度为 1
            
            // 【内部竞争】：回头看前面的所有数字，找一个能接上去的最优“大哥”
            for (int j = 0; j < i; j++) {
                if (nums [j] < nums [i]) {
                    // 找到了一个比自己小的！放心大胆接在它屁股后面
                    dp [i] = Math.max(dp [i], dp [j] + 1);
                }
            }
            
            // 【全国总决赛】：因为最长子序列不一定长在最后面（可能半路就杀青了）
            // 所以必须把当前 nums[i] 的成绩，和历史总冠军进行比对，留下最大的！
            max = Math.max(max, dp [i]);
        }
        return max;
    }
}