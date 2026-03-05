class Solution {
    public void nextPermutation(int[] nums) {
        int n = nums.length;
        int temp;
        // 边界防御：如果数组为空或只有一个元素，不存在“排列”的概念，直接返回
        if (n <= 1) {
            return;
        }
        
        // p 相当于我们要找的“必须改变的数”的索引（即从右往左第一个破坏降序趋势的数）
        int p = -1; 
        
        // 【第一步】：从后往前扫描，寻找“死亡禁区”（完全降序序列）的边界
        // 只要 nums[i] >= nums[i+1]，就说明还在降序区里。
        // 一旦发现 nums[i] < nums[i+1]，说明 nums[i] 就是那个需要被替换的数！
        for (int i = n - 2; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) {
                p = i;
                break; // 找到了就立刻停下，记录位置 p
            }
        }
        
        // 【极端边界处理】：如果 p 依然是 -1，说明整个数组都是完全降序的（比如 3, 2, 1）
        // 也就是达到了宇宙最大值，此时直接跳到最后一步：把整个数组反转成升序（1, 2, 3），然后结束。
        if (p == -1) {
            reverse(nums, 0, n - 1);
            return;
        }
        
        // 【第二步 & 第三步】：从右往左，在“死亡禁区”里找一个比 nums[p] 稍微大一点点的“替身”
        // 因为右边是纯降序的，所以从右往左遇到的第一个比 nums[p] 大的数，就是我们要找的最优替身！
        for (int i = n - 1; i > p; i--) {
            if (nums[i] > nums[p]) {
                // 找到替身，两者交换位置
                temp = nums[i];
                nums[i] = nums[p];
                nums[p] = temp;
                break; // 交换完毕，立刻停止
            }
        }
        
        // 【第四步】：尾部重置
        // 此时，p 位置的数变大了，保证了整个排列比以前大。
        // 为了让“增幅尽可能小”，我们需要把 p 后面的所有数字（原本是降序的）变成最小排列，即反转成升序。
        reverse(nums, p + 1, n - 1);
    }

    // 辅助方法：双指针原地反转数组的指定区间（空间复杂度 O(1) 的核心）
    private void reverse(int[] nums, int start, int end) {
        int temp;
        while (start < end) {
            temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }
}