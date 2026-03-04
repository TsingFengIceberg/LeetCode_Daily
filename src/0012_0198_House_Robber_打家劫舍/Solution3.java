public class Solution3 {
    /**
     * 大厂极致优雅版 O(1) 空间解法
     */
    public int rob(int[] nums) {
        // prev1 代表“上上间房”的最高收益，初始虚拟为 0
        int prev1 = 0; 
        // prev2 代表“上一间房”的最高收益，初始虚拟为 0
        int prev2 = 0; 
        
        // 使用 foreach 循环直接遍历，无需判断长度边界
        for (int num : nums) {
            // temp 记录如果走到当前房子时的最高收益
            // Math.max(不偷当前房子(维持 prev2), 偷当前房子(prev1 + 当前现金 num))
            int temp = Math.max(prev2, prev1 + num);
            
            // 状态滚动：窗口整体向右滑动一格
            prev1 = prev2; // 上上间变成上一间
            prev2 = temp;  // 上一间变成当前最高收益
        }
        
        // 遍历结束后，prev2 就是经过所有房间后的最高收益
        return prev2;
    }
}