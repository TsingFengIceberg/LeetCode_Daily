class Solution2 {
    public boolean exist(char[][] board, String word) {
        char[] words = word.toCharArray(); // 转换成字符数组，Java 里查询更快
        int[] count = new int[128]; // 统计 board 中每个字符的数量，ASCII 码范围内的字符都可以用这个数组来统计

        for (char[] row : board) { // 遍历 board，统计每个字符的数量
            for (char c : row) {
                count[c]++;
            }
        }

        // 1. 双重 for 循环遍历每一个格子，当作起点去尝试 DFS
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                // 如果从当前 (i, j) 起点能找到匹配的单词，直接返回 true
                if (dfs(board, words, count, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    // k 代表我们当前在匹配 word 里的第几个字符
    private boolean dfs(char[][] board, char[] words, int[] count, int i, int j, int k) {
        // 1. 过滤失败条件：越界，或者当前格子的字符和我们要找的字符不匹配（这天然包含了遇到 '#' 的情况）
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != words[k] || count[board[i][j]] < 0) {
            return false;
        }
        
        // 2. 宣告胜利条件：既然没被上面拦截，说明当前字符匹配了！
        // 如果这已经是单词的最后一个字符，说明全部找齐了，直接返回 true！
        if (k == words.length - 1) {
            return true;
        }

        // --- 下面的代码和你写的一模一样，状态标记 -> 深搜 -> 回溯恢复 ---
        char temp = board[i][j]; 
        board[i][j] = '#'; 
        count[temp]--; // 这个字符已经被用掉了，数量 -1
        
        boolean res = dfs(board, words, count, i - 1, j, k + 1) || 
                      dfs(board, words, count, i + 1, j, k + 1) || 
                      dfs(board, words, count, i, j - 1, k + 1) || 
                      dfs(board, words, count, i, j + 1, k + 1);   

        board[i][j] = temp; 
        count[temp]++; // 回溯时恢复字符数量
        return res; 
    }
}