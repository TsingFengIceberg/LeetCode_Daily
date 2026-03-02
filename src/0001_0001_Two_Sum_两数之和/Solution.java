import java.util.HashMap;
import java.util.Map;

public class Solution {
    /**
     * 【核心算法逻辑】
     * 提交 LeetCode 时直接复制这个方法即可。
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            // 先查，O(1) 复杂度寻找另一半
            if (map.containsKey(complement)) {
                // 回首遇到爱：返回 [之前存的下标, 当前下标]
                return new int[] { map.get(complement), i };
            }
            // 没找到，将自己存入 Map 备用
            map.put(nums[i], i);
        }
        
        throw new IllegalArgumentException("未找到匹配的两个数");
    }
}