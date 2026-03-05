public class Solution2 {
    /**
     * 方法二：逐步冒泡位移法
     * 时间复杂度：$O(N \times K)$ -> 嵌套循环导致时间复杂度爆炸
     * 空间复杂度：$O(1)$
     */
    public void rotate(int[] nums, int k) {
        // 计算实际上需要把后面的几个元素“挤”到前面去
        // 这里的 p 实际上是原数组中需要移动到前面的分界点索引
        int p = nums.length - k % nums.length;
        int temp;
        
        // 外层循环：遍历需要移动到前面的元素（也就是原数组末尾的 k 个元素）
        for (int i = p; i < nums.length; i++){
            // 内层循环：通过类似“冒泡”的方式，将当前元素 nums[i] 一步一步往前挪动
            // 每次挪动跨越 p 个位置，直到把它送回它应该在的位置
            for (int j = i; j > i - p; j--){
                // 经典的相邻元素交换（无辅助函数的裸写）
                temp = nums[j];
                nums[j] = nums[j - 1];
                nums[j - 1] = temp;
            }
        }
    }
}