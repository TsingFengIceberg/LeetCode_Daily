public class Solution2 {
    /**
     * 分治法求最大子数组和
     * @param nums 输入数组
     * @return 最大连续子数组和
     */
    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        return maxSubArrayHelper(nums, 0, nums.length - 1);
    }

    /**
     * 分治法辅助函数
     * @param nums 数组
     * @param left 当前处理区间的左边界
     * @param right 当前处理区间的右边界
     * @return 该区间内的最大子数组和
     */
    private int maxSubArrayHelper(int[] nums, int left, int right) {
        // 递归终止条件：区间拆分到只有一个元素时，直接返回该元素
        if (left == right) {
            return nums[left];
        }

        // Java 工程细节：计算中间位置，防溢出写法。绝对不要写成 (left + right) / 2
        int mid = left + (right - left) / 2;

        // 1. 治：递归求解左半部分的最大子数组和
        int leftMax = maxSubArrayHelper(nums, left, mid);
        
        // 2. 治：递归求解右半部分的最大子数组和
        int rightMax = maxSubArrayHelper(nums, mid + 1, right);

        // 3. 治：计算横跨左右两半部分的最大子数组和（必须包含 mid 和 mid + 1）
        int crossMax = crossSum(nums, left, right, mid);

        // 合并：返回三者中的最大值
        return Math.max(Math.max(leftMax, rightMax), crossMax);
    }

    /**
     * 计算横跨中心点的最大子数组和
     */
    private int crossSum(int[] nums, int left, int right, int mid) {
        // 构建“左半桥”：从 mid 开始向左扫描
        int leftSubSum = Integer.MIN_VALUE; // 初始化为最小整数
        int currentSum = 0;
        for (int i = mid; i >= left; i--) {
            currentSum += nums[i];
            leftSubSum = Math.max(leftSubSum, currentSum);
        }

        // 构建“右半桥”：从 mid + 1 开始向右扫描
        int rightSubSum = Integer.MIN_VALUE;
        currentSum = 0;
        for (int i = mid + 1; i <= right; i++) {
            currentSum += nums[i];
            rightSubSum = Math.max(rightSubSum, currentSum);
        }

        // 真正的横跨中心连续和 = 左半桥极限 + 右半桥极限
        return leftSubSum + rightSubSum;
    }
}