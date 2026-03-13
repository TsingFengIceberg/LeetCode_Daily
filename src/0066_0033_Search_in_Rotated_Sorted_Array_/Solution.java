class Solution {
    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        
        // 【边界说明】必须使用 <=，因为搜索区间是闭区间 [left, right]。
        // 当 left == right 时（即区间内只剩最后一个元素），我们仍需要进循环验证这个元素是否就是 target。
        while (left <= right) {
            // 防溢出写法：等同于 (left + right) / 2。
            // 避免 left 和 right 接近整型最大值时，直接相加导致 int 溢出变为负数。
            int mid = left + (right - left) / 2;
            
            // 命中目标，直接返回
            if (nums[mid] == target) {
                return mid;
            } 
            // 【核心边界 1】判断左半段 [left, mid] 是否是纯粹的单调递增区间
            // 为什么必须用 <= ？
            // 因为当区间只剩 1 个或 2 个元素时，由于 mid 是向下取整，mid 会和 left 重合。
            // 此时 nums[left] == nums[mid]，由于只有一个元素，它自身当然算作"有序"，所以必须包含等于的情况。
            else if (nums[left] <= nums[mid]){
                // 【核心边界 2】目标值 target 是否稳稳落在这个完美的左半段有序区间内？
                // 必须同时满足：>= 地板（nums[left]） 且 <= 天花板（nums[mid]）。
                if (nums[mid] >= target && nums[left] <= target) {
                    right = mid - 1; // 确认在左半段，果断砍掉右边
                } 
                else {
                    left = mid + 1;  // 不在这个完美的左半段，那一定在另一半（可能包含旋转点的右半段）
                }
            }
            // 走到这里，说明 nums[left] > nums[mid]，即左半段发生了旋转。
            // 那么根据局部有序定理，右半段 [mid, right] 绝对是一段完美的单调递增区间。
            else{
                // 【核心边界 3】目标值 target 是否稳稳落在这个完美的右半段有序区间内？
                // 同样需要满足：> 地板（nums[mid]） 且 <= 天花板（nums[right]）。
                // （注：因为第一步已经排除了 nums[mid] == target 的情况，所以这里 nums[mid] 严格 < target 逻辑极其严密）
                if (nums[mid] < target && nums[right] >= target) {
                    left = mid + 1;  // 确认在右半段，果断砍掉左边
                } 
                else {
                    right = mid - 1; // 不在完美的右半段，那必定在包含旋转点的左半段
                }
            }                          
        }
        // 整个循环结束都没有 return，说明数组中不存在 target
        return -1;
    }
}