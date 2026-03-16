# [0079. 单词搜索 (Word Search)]

## 💡 回溯算法 (Backtracking) 的底层灵魂：状态标记与现场恢复
这道题是经典的二维网格 DFS（深度优先搜索）应用，但在大厂面试中，其核心考点在于**空间复杂度的极致压缩**。
常规思路会开辟一个 `boolean[][] visited` 数组来防止“走回头路”，这会带来额外的 $O(M \times N)$ 空间开销。
**大厂标准解法（原地修改 In-place）**：
1. **毁容标记**：在 DFS 向下深入前，用 `char temp = board[i][j]` 暂存当前字符，并将其修改为一个不可能出现的特殊字符（如 `#`），表示该路径已被占用。
2. **回溯复原**：如果当前方向的 DFS 探底后返回了 `false`（死胡同），在退出当前递归层之前，**必须**执行 `board[i][j] = temp` 将格子恢复原样。因为在其他的 DFS 搜索分支中，这个格子可能是一条必经之路。

## ⏱️ 复杂度极限推演：为什么是 $O(3^L)$？
* **空间复杂度：$O(L)$**。得益于原地修改，我们省去了访问数组。系统唯一需要的空间是 DFS 递归调用栈的深度，最大深度恰好是我们要找的单词长度 $L$。
* **时间复杂度：$O(M \times N \times 3^L)$**。外层双重循环寻找起点耗时 $O(M \times N)$。在最坏情况下，单次 DFS 除了第一步有 4 个方向，后续每一步由于不能走回头路，最多有 3 个分支。递归深度为 $L$，因此单次 DFS 是一棵庞大的三叉树，时间复杂度高达 $O(3^L)$。

## 🚀 进阶压榨：降维打击的“神级剪枝 (Pruning)”
为了应对 $O(3^L)$ 可能带来的超时风险，我们必须在启动极其沉重的 DFS 之前，利用 $O(M \times N)$ 的轻量级前置校验进行无情拦截：
1. **长度绝对压制**：如果单词长度 $L > M \times N$，网格根本装不下，直接 `return false`。
2. **字符频次核对（ASCII 桶计数）**：利用大小为 128 的 `int[] count` 数组，先遍历网格统计所有字符的“库存”，再去扣减 `word` 需要的字符量。如果某个字符库存不足（`< 0`），直接扼杀在摇篮里，完美避开那些“注定失败却要空转几亿次回溯”的极端用例。

## 🏭 工业界真实工程实例：无剪枝引发的血案与架构底座
回溯算法和剪枝思想是现代高级软件工程不可或缺的底层逻辑：
1. **API 网关的 ReDoS 防御战**：Java 的正则引擎底层采用的就是 DFS + 回溯。如果黑客传入恶意 Payload（如 `aaaaaaaaaaaaab` 去匹配 `^(a+)+$`），正则引擎会陷入几亿次的暴力回溯，导致 CPU 瞬间 100% 打满僵死（正则表达式拒绝服务攻击）。架构师必须在正则校验前加入**长度截断**和**字符集预检**等剪枝手段，才能保住高可用底线。
2. **MySQL 数据库优化器 (CBO) 的极速选路**：当你的 SQL `JOIN` 了 6 张表时，优化器需要从 $6! = 720$ 种执行计划中找出最快的一条。它底层同样使用的是带剪枝的 DFS：维护一个 `current_min_cost`，当深度搜索还没拼完 6 张表时，若发现当前路径的预估磁盘 IO 成本已经超标，立刻触发剪枝并回溯，从而在几毫秒内为你选出最优的表连接顺序。

## 最终 Java 代码实现
```java
class Solution {
    /**
     * 单词搜索 - DFS 回溯 + 极致剪枝版
     */
    public boolean exist(char[][] board, String word) {
        int m = board.length, n = board[0].length;
        char[] words = word.toCharArray();

        // ✂️ 剪枝 1：长度绝对压制
        if (words.length > m * n) {
            return false;
        }

        // ✂️ 剪枝 2：字符频次降维打击 (ASCII桶)
        int[] count = new int[128];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                count[board[i][j]]++; // 统计网格库存
            }
        }
        for (char c : words) {
            count[c]--;
            if (count[c] < 0) {
                return false; // 库存不足，直接拦截，拒绝启动 DFS
            }
        }

        // 1. 双重 for 循环寻找 DFS 起点
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dfs(board, words, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    // k 代表当前匹配到 word 的第几个字符
    private boolean dfs(char[][] board, char[] words, int i, int j, int k) {
        // 1. 过滤失败条件：越界 或 字符不匹配 (含 '#' 的情况)
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != words[k]) {
            return false;
        }
        
        // 2. 宣告胜利：当前字符匹配，且已经是最后一个字符
        if (k == words.length - 1) {
            return true;
        }

        // 3. 状态标记（原地毁容）
        char temp = board[i][j];
        board[i][j] = '#';

        // 4. 四面八方深搜
        boolean res = dfs(board, words, i - 1, j, k + 1) || 
                      dfs(board, words, i + 1, j, k + 1) || 
                      dfs(board, words, i, j - 1, k + 1) || 
                      dfs(board, words, i, j + 1, k + 1);

        // 5. 回溯恢复现场（吃干抹净）
        board[i][j] = temp;
        
        return res;
    }
}