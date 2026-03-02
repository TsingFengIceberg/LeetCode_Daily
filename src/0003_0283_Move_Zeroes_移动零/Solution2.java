public class Solution2 {
    /**
     * 解法 2：一次遍历（双指针交换法）
     * 时间复杂度：O(N)，只遍历了一次数组。
     * 空间复杂度：O(1)，原地操作。
     * 优化点：省去了最后补 0 的 while 循环，且在全是非 0 元素的极端情况下，虽然发生了原地交换，但步骤更精简。
     */
    public void moveZeroes(int[] nums) {
        // slow 指针：指向下一个准备接收非零元素的位置（或者说，指向第一个 0 的位置）
        int slow = 0;
        
        for (int fast = 0; fast < nums.length; fast++) {
            // 当快指针遇到非 0 元素时，把它和慢指针的值进行交换
            if (nums[fast] != 0) {
                // 如果 fast 和 slow 相等，其实相当于自己跟自己交换，不影响结果
                // 但如果 fast > slow，此时 nums[slow] 必定是 0，交换后 0 就被扔到后面去了
                int temp = nums[slow];
                nums[slow] = nums[fast];
                nums[fast] = temp;
                
                slow++; // 慢指针前进一步
            }
        }
    }
}