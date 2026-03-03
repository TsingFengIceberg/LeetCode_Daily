/**
 * 极致性能优化版双指针解法
 * 时间复杂度: O(n) （常数项极小）
 * 空间复杂度: O(1)
 */
public class Solution2 {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        
        while (left < right) {
            // 优化点 A：用局部变量缓存高度，减少数组在堆内存中的重复寻址
            int hLeft = height[left];
            int hRight = height[right];
            
            // 优化点 B：使用三元运算符 ?: 替代 Math.min 和 Math.max，彻底消除方法栈调用开销
            int minHeight = hLeft < hRight ? hLeft : hRight;
            int currentArea = minHeight * (right - left);
            maxArea = maxArea > currentArea ? maxArea : currentArea;
            
            // 优化点 C：极限贪心，内层快速跳过所有比 minHeight 还要矮的柱子！
            // 核心逻辑：既然短板决定了高度，如果移动后的新板子比之前的短板还矮，容积绝对不可能变大，直接无脑跳过。
            if (hLeft < hRight) {
                // 注意边界：必须加上 left < right，防止数组越界
                while (left < right && height[left] <= minHeight) {
                    left++;
                }
            } else {
                while (left < right && height[right] <= minHeight) {
                    right--;
                }
            }
        }
        return maxArea;
    }
}