import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        // 1. 将 List 转换为 HashSet，实现 O(1) 极速查找
        Set<String> wordDictSet = new HashSet<>(wordDict);
        
        // 2. 初始化 dp 数组，长度为 s.length() + 1
        // dp[i] 表示字符串 s 的前 i 个字符能否被成功拆分
        boolean[] dp = new boolean[s.length() + 1];
        
        // 3. 埋下发令枪（边界条件）
        dp[0] = true;
        
        // 4. 外层循环 i：代表当前接力赛要跑到的终点（从 1 跑到 s.length()）
        for (int i = 1; i <= s.length(); i++) {
            // 内层循环 j：代表前一个中转站（从起跑线 0 开始，一直试到 i-1）
            for (int j = 0; j < i; j++) {
                
                // 🚨 【核心状态转移方程】
                // 请把你刚才推理的双重条件写在这里：
                // 如果 (中转站 j 是通的) 并且 (从 j 到 i 的这块碎片在字典里)
                if (dp[j] && wordDictSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // 只要找到一种合法的拆法，立刻停止寻找下一个 j，极速剪枝！
                }
            }
        }
        
        // 5. 最终返回：跑到整个字符串最后一位时，交接棒是否还在手里
        return dp[s.length()];
    }
}