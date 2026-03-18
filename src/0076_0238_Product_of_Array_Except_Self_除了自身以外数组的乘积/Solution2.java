class Solution2 {
    public int[] productExceptSelf(int[] nums) {
        // 1. 直接复用最终的返回数组 result 作为前缀积的存储容器
        // 这样就不需要额外 new 一个 prefix 数组，满足了进阶的 O(1) 额外空间要求
        int[] result = new int[nums.length];
        
        // 2. 初始化前缀积的基座：包含当前元素在内的左侧连乘积
        result[0] = nums[0];     
        
        // 3. 正向遍历，把 result 数组填满（此时 result[i] 存的是从 0 到 i 的连乘积）
        for (int i = 1; i < result.length; i++){
            result[i] = nums[i] * result[i - 1];
        }
        
        // 4. 极致的内存压榨核心：用一个动态变量 temp 替代整个 suffix 数组
        // temp 代表了“严格在当前指针 i 右侧的所有元素的乘积”
        int temp = 1;
        
        // 5. 反向遍历，边算后缀积边结算最终答案
        // 注意循环条件 i > 0，因为走到 i = 0 时，result[i - 1] 会越界，需要单独特判
        for (int i = result.length - 1; i > 0; i--){     
            // 核心公式：除自己外的乘积 = (严格在左侧的前缀积) * (严格在右侧的后缀积)
            // 此时 result[i - 1] 恰好就是严格在 i 左侧的所有元素的乘积！
            // temp 恰好就是严格在 i 右侧的所有元素的乘积！
            result[i] = result[i - 1] * temp;
            
            // 结算完当前位置后，立刻更新 temp，把当前的 nums[i] 乘进去，供下一个位置使用
            temp *= nums[i];
        }
        
        // 6. 边界打补丁：处理最初被遗漏的索引 0
        // 当 i = 0 时，它左边没有任何元素（或者说左侧乘积为 1）
        // 所以 result[0] 直接等于它右侧所有元素的连乘积，也就是遍历结束后的 temp！
        result[0] = temp;
        
        return result;
    }
}