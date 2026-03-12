class Solution {
    public int[] searchRange(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        int leftBound = -1, rightBound = -1;
        int mid = 0, leftMid = 0, rightMid = 0;
        
        // 【第一阶段：破局点扫描 (Fail-Fast 机制)】
        // 标准二分查找，目的不是直接定边界，而是先在数组中锁定【任意一个】 target 的位置
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                break; // 抓到一个 target，立刻停下！以它为锚点展开精确搜索
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        // 如果循环结束了，left > right 说明整个数组里压根没有 target
        // 提早拦截，直接返回 [-1, -1]，拒绝后续两段的无效计算，极致的性能保护！
        if (left > right) {
            return new int[]{-1, -1};
        }
        
        // 【第二阶段：寻找左边界 (极致压缩搜索空间)】
        // 既然 mid 已经是 target 了，左边界一定在 [0, mid] 之间！
        left = 0;
        right = mid - 1;
        leftBound = mid; // 兜底策略：如果左半边没再找到 target，那当前的 mid 就是最左边界
        while (left <= right) {
            leftMid = left + (right - left) / 2;
            if (nums[leftMid] == target) {
                // 命中 target！继续“得寸进尺”向左看，记录当前位置，逼迫右指针左移
                leftBound = leftMid;
                right = leftMid - 1;
            } else {
                // 因为在 mid 的左侧，且数组递增，这里的值必然 < target，只能向右收缩
                left = leftMid + 1;
            }
        }
        
        // 【第三阶段：寻找右边界 (极致压缩搜索空间)】
        // 同理，右边界一定在 [mid, nums.length - 1] 之间！
        left = mid + 1;
        right = nums.length - 1;
        rightBound = mid; // 兜底策略：如果右半边没再找到 target，那最初的 mid 就是最右边界
        while (left <= right) {
            rightMid = left + (right - left) / 2;
            if (nums[rightMid] == target) {
                // 命中 target！继续“得寸进尺”向右看，记录当前位置，逼迫左指针右移
                rightBound = rightMid;
                left = rightMid + 1;
            } else {
                // 因为在 mid 的右侧，且数组递增，这里的值必然 > target，只能向左收缩
                right = rightMid - 1;
            }
        }
        
        // 完美收官：返回精确锁定的左右边界
        return new int[]{leftBound, rightBound};
    }
}