class Solution2 {
    // 【大厂 Code Review：动态规划解法】
    // 时间复杂度：O(n^2)，填满一个 n x n 的矩阵。
    // 空间复杂度：O(n^2)，需要一个 n x n 的二维布尔数组来存储所有状态。
    public String longestPalindrome(String s) {
        int n = s.length();
        // 长度为 1 的情况直接返回
        if (n < 2) {
            return s;
        }

        int maxLen = 1;
        int begin = 0;
        
        // dp[i][j] 表示 s[i..j] 是否是回文串
        boolean[][] dp = new boolean[n][n];
        
        // 初始化：所有长度为 1 的子串都是回文串
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        // 优化点：转为字符数组，避免在循环中频繁调用 charAt() 导致边界检查开销
        char[] charArray = s.toCharArray();
        
        // 【核心遍历顺序】：按子串长度 L 从小到大进行递推
        // L = 2, 3, 4, ..., n
        for (int L = 2; L <= n; L++) {
            // 枚举左边界 i
            for (int i = 0; i < n; i++) {
                // 由长度 L 和左边界 i 确定右边界 j
                int j = L + i - 1;
                
                // 如果右边界越界，直接退出当前一层的循环
                if (j >= n) {
                    break;
                }

                // 字符不相等，绝不可能是回文
                if (charArray[i] != charArray[j]) {
                    dp[i][j] = false;
                } else {
                    // 字符相等的情况
                    // 如果长度是 2 或 3，内部没有子串或只有一个字符，必然是回文
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        // 长度大于 3，状态转移，剥洋葱一样看内部子串
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }

                // 如果 s[i..j] 是回文，并且长度大于当前记录的最大长度，则更新记录
                if (dp[i][j] && j - i + 1 > maxLen) {
                    maxLen = j - i + 1;
                    begin = i;
                }
            }
        }
        
        // 仅在最后进行一次 substring 字符串截取，避免频繁创建 String 对象
        return s.substring(begin, begin + maxLen);
    }
}