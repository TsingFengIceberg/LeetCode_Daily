public class Solution1 {
    /**
     * O(N) 两次遍历法：基于计数排序思想
     * 虽然不是进阶的一趟扫描，但在实际工程中，常数极小，且不易出错。
     */
    public void sortColors(int[] nums) {
        if (nums == null || nums.length <= 1) return;

        // 1. 统计各种颜色的数量
        int count0 = 0, count1 = 0, count2 = 0;
        for (int num : nums) {
            if (num == 0) count0++;
            else if (num == 1) count1++;
            else if (num == 2) count2++;
        }

        // 2. 根据数量，重新按顺序覆盖原数组
        int i = 0;
        // 先铺满 0
        while (count0 > 0) {
            nums[i++] = 0;
            count0--;
        }
        // 再铺满 1
        while (count1 > 0) {
            nums[i++] = 1;
            count1--;
        }
        // 最后铺满 2
        while (count2 > 0) {
            nums[i++] = 2;
            count2--;
        }
    }
}