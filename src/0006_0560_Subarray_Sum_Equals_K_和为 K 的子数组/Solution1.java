import java.util.HashMap;

/**
 * 前缀和 + 标准 HashMap 解法
 * 时间复杂度: O(N)
 * 空间复杂度: O(N)
 */
public class Solution1 {
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int preSum = 0;
        
        // Key 存的是前缀和，Value 存的是这个前缀和出现的次数
        HashMap<Integer, Integer> map = new HashMap<>();
        
        // 🚨 大厂高频踩坑点：必须初始化 (0, 1)。
        // 含义：在没有遍历任何元素时，前缀和为 0 的情况已经存在了 1 次。
        // 这为了解决那些“从数组第 0 位开始，自身总和恰好等于 k”的子数组。
        map.put(0, 1); 

        for (int i = 0; i < nums.length; i++) {
            // 1. 累加当前的前缀和
            preSum += nums[i];
            
            // 2. 查账本：看看历史上有多少个前缀和等于 preSum - k
            if (map.containsKey(preSum - k)) {
                count += map.get(preSum - k);
            }
            
            // 3. 记账：把当前的前缀和也登记到账本里，供后面的数字查阅
            map.put(preSum, map.getOrDefault(preSum, 0) + 1);
        }
        
        return count;
    }
}