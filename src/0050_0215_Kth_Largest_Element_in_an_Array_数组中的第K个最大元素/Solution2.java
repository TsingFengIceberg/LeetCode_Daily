class Solution2 {
    public int findKthLargest(int[] nums, int k) {
        
        java.util.Random random = new java.util.Random(); 
        
        // target 是我们要找的最终目标索引 (对于这题是 nums.length - k)
        int left = 0, right = nums.length - 1;
        int target = nums.length - k;

        // 外层 while 彻底替代 quickSelect 的递归
        while (left <= right) {
            
            // 1. 在当前 [left, right] 区间内，随机抽取一个索引
            int randomIndex = left + random.nextInt(right - left + 1);

            // 2. 把抽中的倒霉蛋，和最左边的元素 (坑) 交换一下位置
            int temp = nums[left];
            nums[left] = nums[randomIndex];
            nums[randomIndex] = temp;
            
            // --- 核心内联 Partition (挖坑填数法) 开始 ---
            int pivot = nums[left]; // 选最左边的元素作为基准，此时 left 位置变成了一个“坑”
            int i = left, j = right;
            
            while (i < j) {
                // 1. j 指针先走！从右向左找第一个【小于】 pivot 的数
                while (i < j && nums[j] >= pivot) {
                    j--;
                }
                // 找到了！把 nums[j] 挖出来，填到左边的“坑” nums[i] 里
                // 此时 j 位置变成了一个新的“坑”
                nums[i] = nums[j]; 
                
                // 2. i 指针后走！从左向右找第一个【大于】 pivot 的数
                while (i < j && nums[i] <= pivot) {
                    i++;
                }
                // 找到了！把 nums[i] 挖出来，填到右边的“坑” nums[j] 里
                // 此时 i 位置又变成了新的“坑”
                nums[j] = nums[i]; 
            }
            // 当 i == j 时，相遇了！把最初保存的 pivot 填到这个最终的“坑”里
            nums[i] = pivot; 
            // --- 核心内联 Partition 结束 ---
            
            
            // --- 渣男式区间丢弃判断 ---
            // 此时 i 就是 pivot 最终的绝对正确的索引位置
            if (i == target) {
                // 🎯 命中靶心！直接打完收工
                return nums[i]; 
            } else if (i < target) {
                // pivot 在目标的左边，说明目标在右半区间，左边全扔掉！
                // 问：left 应该更新为多少？
                // left = ???;
                left = i + 1;
            } else {
                // pivot 在目标的右边，说明目标在左半区间，右边全扔掉！
                // 问：right 应该更新为多少？
                // right = ???;
                right = i - 1;
            }
        }
        return -1; // 理论上不会到达这里
    }
}