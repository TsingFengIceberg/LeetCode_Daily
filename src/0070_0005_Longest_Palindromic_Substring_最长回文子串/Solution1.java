class Solution1 {
    public String longestPalindrome(String s) {
        // 【边界拦截】如果字符串只有一个字符，天然是回文，直接返回
        if (s.length() == 1) return s;
        
        // 声明双指针，用于向左右两边扩展
        int start = 0, end = 0;
        
        // 状态组 1：用于专门记录【偶数长度】回文串的最优解（中心是缝隙）
        int maxStart1 = 0, maxEnd1 = 0, maxLength1 = 1;
        // 状态组 2：用于专门记录【奇数长度】回文串的最优解（中心是单个字符）
        int maxStart2 = 0, maxEnd2 = 0, maxLength2 = 1;
        
        // ==========================================
        // 第一轮扫描：寻找【偶数长度】的最长回文串
        // 中心点是 i-1 和 i 之间的“虚空缝隙”
        // ==========================================
        for (int i = 1; i < s.length(); i++) {
            start = i - 1;
            end = i;
            // 只要没有越界，且两边的字符相等，就不断向外扩展
            while (start >= 0 && end < s.length() && s.charAt(start) == s.charAt(end)) {
                start--;
                end++;
            }
            // 【核心边界解析：为什么长度是 end - start - 1？】
            // 当 while 循环打破时，说明 start 和 end 指向的字符【已经不相等】或【越界】了。
            // 这意味着真正的回文串范围是开区间 (start, end)，即闭区间 [start + 1, end - 1]。
            // 真实长度计算公式：(end - 1) - (start + 1) + 1 = end - start - 1。
            if (end - start - 1 > maxLength1) {
                maxStart1 = start; // 注意：此时保存的 start 实际上是回文串左边界的前一个位置
                maxEnd1 = end;     // 注意：此时保存的 end 实际上是回文串右边界的后一个位置
                maxLength1 = end - start - 1;
            }
        }
        
        // ==========================================
        // 第二轮扫描：寻找【奇数长度】的最长回文串
        // 中心点是具体的字符 i
        // ==========================================
        for (int i = 1; i < s.length() - 1; i++) {
            start = i - 1;
            end = i + 1; // 跳过中心字符 i，直接比对它左右两边的字符
            while (start >= 0 && end < s.length() && s.charAt(start) == s.charAt(end)) {
                start--;
                end++;
            }
            // 同理，计算打断时的真实回文长度
            if (end - start - 1 > maxLength2) {
                maxStart2 = start;
                maxEnd2 = end;
                maxLength2 = end - start - 1;
            }
        }
        
        // ==========================================
        // 全局结果仲裁与提取
        // ==========================================
        
        // 【兜底处理】如果扫遍了整个字符串，发现连一个长度为 2 的回文串都没找到（比如 "abcd"）
        // 那就老老实实返回它的第一个字符。这里完美解答了你之前的疑问！
        if (maxLength1 == 1 && maxLength2 == 1) {
            return s.substring(0, 1);
        }
        
        // 比较奇数情况和偶数情况，谁更长就提取谁
        // 【Java 提取子串的艺术】
        // s.substring(beginIndex, endIndex) 是左闭右开的 [beginIndex, endIndex)
        // 回顾上面我们保存的 maxStart 是越界的前一个位置，所以起点必须 +1，即 maxStart + 1
        // 而 maxEnd 刚好是越界的后一个位置，配合 substring 的“右开”特性，正好完美截取！
        if (maxLength1 > maxLength2) {
            return s.substring(maxStart1 + 1, maxEnd1);
        } else {
            return s.substring(maxStart2 + 1, maxEnd2);
        }
    }
}