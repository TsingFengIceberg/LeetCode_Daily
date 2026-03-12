import java.util.ArrayList;
import java.util.List;

class Solution {
    // 全局结果集，档案室
    private List<String> result = new ArrayList<>();
    
    public List<String> generateParenthesis(int n) {
        // 启动回溯引擎：
        // 传入初始空字符串、手里握着的左括号数量 n、右括号数量 n、以及最大对数 n
        backtrack(new String(), n, n, n);
        return result;
    }

    // 回溯核心引擎：
    // current: 当前拼装出的括号序列
    // open: 手里【还剩下】多少个左括号 '('
    // close: 手里【还剩下】多少个右括号 ')'
    // max: 一共需要生成的括号对数
    private void backtrack(String current, int open, int close, int max) {
        // 【1. 杀青快照（结束条件）】
        // 当拼接的字符串长度等于括号总数 (2 * n) 时，说明手里的牌全打光了
        if (current.length() == max * 2) {
            result.add(current); // 记录合法组合
            return;
        }
        
        // 【2. 打出左括号的铁律】
        // 左括号是“极其包容”的。只要手里还有左括号 (open > 0)，随时都可以打出去！
        if (open > 0) {
            // 🚨 隐式回溯魔法：current + "(" 会生成一个全新的 String 对象传给下一层。
            // 当前层的 current 并没有被改变，所以不需要像 StringBuilder 那样手动 deleteCharAt 撤销选择！
            backtrack(current + "(", open - 1, close, max);
        }
        
        // 【3. 打出右括号的生死线（全题唯一的灵魂）】
        // 🚨 极其严苛的合法性前置剪枝！
        // 为什么是 open < close？
        // 因为 open 和 close 代表的是手里【剩余】的数量。
        // 只有当剩余的右括号 (close) 比剩余的左括号 (open) 多时，
        // 说明前面肯定有“已经打出去但还未匹配的左括号”在等我，此时打出右括号才是合法的！
        if (open < close) {
            backtrack(current + ")", open, close - 1, max);
        }
    }
}