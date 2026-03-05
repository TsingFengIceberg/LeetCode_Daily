public class Solution1 {
    /**
     * 方法一：三次翻转法（最优解之一，推荐在面试中手写）
     * 时间复杂度：$O(N)$
     * 空间复杂度：$O(1)$
     */
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        // 【优化点1】提取公因式，避免重复计算，提高可读性
        k = k % n; 
        
        // 【优化点2】剪枝：如果不需要移动，或者数组为空/只有一个元素，直接返回
        if (k == 0 || n <= 1) {
            return;
        }

        // 第一步：整体翻转
        reverse(nums, 0, n - 1);
        // 第二步：翻转前 k 个元素
        reverse(nums, 0, k - 1);
        // 第三步：翻转剩下的 n - k 个元素
        reverse(nums, k, n - 1);
    }
    
    // 注意：如果是内部调用的辅助方法，建议用 private，体现良好的封装性
    private void reverse(int[] nums, int start, int end){
        while (start < end){
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }
}