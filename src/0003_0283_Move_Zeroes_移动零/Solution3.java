public class Solution3 {
    /**
     * 解法 3：终极大厂微操版 (0ms 击败 100%)
     * 优化核心：
     * 1. 避免自我交换：只有在 fast 和 slow 不相等时，才进行操作，极致节省无用读写。
     * 2. 省略 temp 变量：因为 nums[slow] 在 fast != slow 时绝对是 0，
     * 所以直接赋值并把 fast 原位置 0 即可，连交换逻辑都省了。
     */
    public void moveZeroes(int[] nums) {
        int slow = 0;
        
        for (int fast = 0; fast < nums.length; fast++) {
            // 只有遇到非零元素时才处理
            if (nums[fast] != 0) {
                // 【绝杀微操】：快慢指针不重合时，才需要真正的数据转移
                if (fast != slow) {
                    nums[slow] = nums[fast]; // 把非 0 数字填到前面 slow 的坑位
                    nums[fast] = 0;          // 原位置直接赋 0（因为 slow 曾经指的肯定是 0）
                }
                // 不管进没进上面的 if，只要是非 0 元素，slow 坑位就往前走一步
                slow++; 
            }
        }
    }
}