import java.util.HashMap;

/**
 * 基础滑动窗口解法（使用 HashMap）
 * 时间复杂度: O(n)
 * 空间复杂度: O(Σ) 其中 Σ 是字符集大小，最多 O(n)
 */
public class Solution1 {
    public int lengthOfLongestSubstring(String s) {
        int left = 0;
        int right = 0;
        int maxSubLength = 0;
        int length = s.length();
        // 记录字符最后一次出现的索引
        HashMap<Character, Integer> charIndexMap = new HashMap<>();
        
        while (right < length) {
            char currentChar = s.charAt(right);
            // 如果遇到重复字符，更新 left 指针。注意必须用 Math.max 防止 left 倒退（"abba" 陷阱）
            if (charIndexMap.containsKey(currentChar)) {
                left = Math.max(left, charIndexMap.get(currentChar) + 1);
            }
            // 记录或更新当前字符的最新索引
            charIndexMap.put(currentChar, right);
            // 更新最大长度
            maxSubLength = Math.max(maxSubLength, right - left + 1);
            
            right++;
        }
        return maxSubLength;
    }
}