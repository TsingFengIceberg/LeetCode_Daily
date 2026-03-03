/**
 * 极致性能优化版（使用 int 数组替代 HashMap）
 * 时间复杂度: O(n)
 * 空间复杂度: O(1) （固定长度 128 的数组，常数级别开销）
 */
public class Solution2 {
    public int lengthOfLongestSubstring(String s) {
        // 优化点 A：ASCII 码总共 128 个字符，直接用数组模拟完美的 HashMap，避免装箱拆箱开销
        int[] charIndexMap = new int[128];
        
        // 优化点 B：初始化数组为 -1，表示所有字符都尚未出现过
        java.util.Arrays.fill(charIndexMap, -1);
        
        int left = 0;
        int maxSubLength = 0;
        int length = s.length();
        
        for (int right = 0; right < length; right++) {
            // char 在 Java 中本质上是一个 16 位的无符号整数，可以直接隐式转换为 int 作为数组下标
            char currentChar = s.charAt(right);
            
            // 如果该字符出现过，并且它上一次出现的位置在当前滑动窗口内 (>= left)
            if (charIndexMap[currentChar] >= left) {
                // 窗口左边界收缩到重复字符的下一个位置
                left = charIndexMap[currentChar] + 1;
            }
            
            // 记录当前字符的最新索引
            charIndexMap[currentChar] = right;
            
            // 优化点 C：直接计算，不用 Math.max 节省微小的栈帧开销
            int currentLength = right - left + 1;
            if (currentLength > maxSubLength) {
                maxSubLength = currentLength;
            }
        }
        
        return maxSubLength;
    }
}