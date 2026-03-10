import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 1. 建立秩序：排序是双指针夹逼和去重的基础
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 2; i++) {
            // 🌟 你的神级剪枝：老大大于0，后面全正数，绝无可能等于0，直接结束战斗！
            if (nums[i] > 0) {
                break;  
            }
            
            // 🎯 核心防 Bug：老大去重！(和前一个老大一样就跳过)
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // 2. 降维打击：双指针夹逼找小弟
            for (int j = i + 1, k = nums.length - 1; j < k; ) {
                int sum = nums[i] + nums[j] + nums[k];
                
                if (sum == 0) {
                    // 找到一组正确答案
                    result.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    
                    // 🎯 小弟去重：跳过完全一样的左小弟
                    while (j < k && nums[j] == nums[j + 1]) {
                        j++;  
                    }
                    // 🎯 小弟去重：跳过完全一样的右小弟
                    while (j < k && nums[k] == nums[k - 1]) {
                        k--;  
                    }
                    
                    // 左右指针同时向内收缩，寻找下一组可能的小弟
                    j++;
                    k--;
                } else if (sum < 0) {
                    j++; // 整体太小，左小弟往右移换个大点的
                } else {
                    k--; // 整体太大，右小弟往左移换个小点的
                }
            }
        }
        return result;
    }
}