public class Solution2 {
    public int rob(int[] nums) {
        // 前置的边界条件拦截，与 Solution1 完全一致
        if (nums == null || nums.length == 0) {
            return 0;
        }
        else if (nums.length == 1) {
            return nums[0];
        }
        else if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        else {
            // 状态压缩核心：因为当前状态只依赖前两个状态，
            // 所以无需开辟整个 sum 数组，用两个变量滚动记录即可。
            
            // sump1 相当于 sum[i-2]，记录“上上间房”的最高收益
            int sump1 = nums[0];
            // sump2 相当于 sum[i-1]，记录“上一间房”的最高收益
            int sump2 = Math.max(nums[0], nums[1]);
            // temp 用于在滚动更新时暂存数据
            int temp;
            
            for (int i = 2; i < nums.length; i++) {
                // 1. 先把当前的“上一间房收益 (sump2)”暂存起来
                temp = sump2;
                // 2. 更新当前的“最高收益 (新的 sump2)”
                // 计算公式依旧是：max(不偷(sump2), 偷(sump1 + nums[i]))
                sump2 = Math.max(sump2, sump1 + nums[i]);
                // 3. 变量滚动：原本的“上一间房收益 (temp)”现在变成了“上上间房收益 (sump1)”
                // 准备迎接下一轮循环
                sump1 = temp;
            }
            // 循环结束，sump2 停留在最后一步的最高收益上
            return sump2;
        }
    }
}