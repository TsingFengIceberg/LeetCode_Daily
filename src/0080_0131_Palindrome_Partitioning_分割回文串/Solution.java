import java.util.ArrayList;
import java.util.List;

class Solution {
    // 全局变量：存放最终所有合法分割方案的集合
    List<List<String>> res = new ArrayList<>();
    // 全局变量：存放当前正在尝试的、从根节点到当前节点的“切割路径”
    List<String> path = new ArrayList<>();

    public List<List<String>> partition(String s) {
        // 触发回溯搜索，最开始这把刀停在索引 0 的位置
        dfs(s, 0);
        return res;
    }

    /**
     * 核心回溯逻辑
     * @param s 原始字符串
     * @param startIndex 我们当前这把刀要从哪个位置开始切
     */
    private void dfs(String s, int startIndex) {
        // 1. Base Case (终止条件)
        // 思考：当 startIndex 走到哪里时，说明整个字符串都被我们切完了？
        if (startIndex == s.length()) {
            // 🚨 大厂 Java 基础预警：
            // path 是一个引用对象，如果我们直接 res.add(path)，后续 path 变了，res 里的结果也会跟着变！
            // 这里应该怎么写，才能把当前的 path “快照”存进 res 中？
            res.add(new ArrayList<>(path));
            return;
        }

        // 2. 单层搜索逻辑（横向遍历所有可能的切割线位置 i）
        // i 就是这把刀这一步要切下的终点位置（包含 i）
        for (int i = startIndex; i < s.length(); i++) {
            // 判断当前截取出来的子串 s[startIndex...i] 是否是回文串
            if (isPalindrome(s, startIndex, i)) {
                // 如果是回文串，把它截取出来，加入当前路径 path
                String substring = s.substring(startIndex, i + 1);
                path.add(substring);
            } else {
                // 如果不是回文串，直接剪枝，连递归都不用进，尝试下一个切割位置
                continue;
            }

            // 递归：基于当前合法的切割，去切剩下的那部分字符串
            // 思考：下一次递归，新的切割起点应该从哪里开始？
            dfs(s, i + 1);

            // 撤销选择（回溯的核心所在！）
            // 刚刚我们尝试了把 s[startIndex...i] 切下来，现在我们要把这个选择“撤回”，
            // 以便 for 循环进入下一次循环，去尝试切 s[startIndex...i+1]
            /* 填空 4：如何把 path 的最后一个元素移除？ */
            path.removeLast();
        }
    }

    /**
     * 辅助 API：使用双指针，判断字符串 s 在闭区间 [left, right] 内是否是回文串
     */
    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            // 如果左右指针指向的字符不相等，说明绝对不是回文串
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            // 指针向中间逼近
            left++;
            right--;
        }
        return true; // 循环顺利走完，说明是回文串
    }
}