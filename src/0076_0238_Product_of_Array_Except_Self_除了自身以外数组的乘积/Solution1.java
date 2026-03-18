class Solution {
    public int[] productExceptSelf(int[] nums) {
        // 1. 开辟三个数组：
        // prefix[i] 代表包含 nums[i] 在内的，从左到右的连乘积
        // suffix[i] 代表包含 nums[i] 在内的，从右到左的连乘积
        // result[i] 存放最终的答案
        int[] prefix = new int[nums.length];
        int[] suffix = new int[nums.length];
        int[] result = new int[nums.length];
        
        // 2. 状态基座初始化
        prefix[0] = nums[0];
        suffix[suffix.length - 1] = nums[nums.length - 1];
        
        // 3. 一次正向遍历，构造前缀积数组
        for (int i = 1; i < prefix.length; i++){
            // 当前元素的前缀积 = 当前元素的值 * 前一个元素的前缀积
            prefix[i] = nums[i] * prefix[i - 1];
        }
        
        // 4. 一次反向遍历，构造后缀积数组
        for (int i = suffix.length - 2; i >= 0; i--){
            // 当前元素的后缀积 = 当前元素的值 * 后一个元素的后缀积
            suffix[i] = nums[i] * suffix[i + 1];
        }
        
        // 5. 边界特判：打补丁处理 result 数组的头部和尾部
        // result[0] 的左边没有任何元素，所以它只等于右边所有元素的乘积（即 suffix[1]）
        result[0] = suffix[1];
        // result[最后一个] 的右边没有任何元素，只等于左边所有元素的乘积（即 prefix[倒数第二个]）
        result[result.length - 1] = prefix[result.length - 2];
        
        // 6. 核心结算：处理中间的普遍情况
        for (int i = 1; i < result.length - 1; i++){
            // i 位置除自身外的乘积 = 严格在它左边元素的连乘积 * 严格在它右边元素的连乘积
            result[i] = prefix[i - 1] * suffix[i + 1];
        }
        
        return result;
    }
}