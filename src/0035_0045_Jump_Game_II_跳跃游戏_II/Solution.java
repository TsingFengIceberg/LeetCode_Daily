class Solution {
    public int jump(int[] nums) {
        // 记录最终需要返回的最小跳跃次数
        int jumps = 0;
        
        // 记录“当前这一跳”所能覆盖的势力范围的右边界
        // 一开始还没起跳，所以边界在起点 0
        int currentEnd = 0;
        
        // 记录在当前势力范围内走动时，暗中侦察到的“下一步能跳到的最远位置”
        int currentMaxPosition = 0;
        
        // 【极其关键的边界防范】：遍历到倒数第二个元素即可 (nums.length - 1)
        // 因为题目保证一定能到达终点。如果我们已经到了终点，就不需要再起跳了。
        // 如果把 i 遍历到最后一个元素，且刚好 i == currentEnd 时，会错误地多算一次起跳。
        for (int i = 0; i < nums.length - 1; i++) {
            
            // 核心动作 1：在当前的势力范围内往前走，每走一步，
            // 都要算一下站在当前位置 i，最远能跳到哪里 (i + nums[i])
            // 并不断刷新我们的“暗中侦察”最远纪录
            currentMaxPosition = Math.max(currentMaxPosition, i + nums[i]);
            
            // 核心动作 2：当我们一步步走，终于碰到了当前这一跳的边界 currentEnd 时
            // 意味着：我们把当前这一跳的红利彻底吃干抹净了，到了“被迫起跳”的生死关头
            if (i == currentEnd) {
                // 既然必须起跳，跳跃次数直接 +1
                jumps++;
                // 起跳之后，我们的新势力范围边界，就延伸到了刚刚一路上侦察到的最远位置
                currentEnd = currentMaxPosition;
            }
        }
        
        return jumps;
    }
}