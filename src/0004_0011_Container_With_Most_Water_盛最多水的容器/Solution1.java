/**
 * 基础双指针解法
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 */
public class Solution1 {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        
        while (left < right) {
            // 计算当前面积
            int currentArea = Math.min(height[left], height[right]) * (right - left);
            // 更新最大面积
            maxArea = Math.max(maxArea, currentArea);
            
            // 贪心策略：移动较短的那块板
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }
}