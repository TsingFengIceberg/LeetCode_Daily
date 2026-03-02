public class Solution1 {
    /**
     * 解法 1：两次遍历（覆盖 + 补零）
     * 时间复杂度：O(N)，N 为数组长度。
     * 空间复杂度：O(1)，原地操作。
     */
    public void moveZeroes(int[] nums) {
        // slow 指针：记录下一个非零元素应该存放的位置
        int slow = 0;
        
        // 第 1 次遍历：把所有非 0 的数字，按顺序全挤到数组最前面
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                nums[slow] = nums[fast];
                slow++; // 坑位向前移一步
            }
        }
        
        // 第 2 次遍历：前面非 0 的都排好了，slow 后面的坑位全是 0，直接覆盖补齐
        while (slow < nums.length) {
            nums[slow] = 0;
            slow++;
        }
    }
}