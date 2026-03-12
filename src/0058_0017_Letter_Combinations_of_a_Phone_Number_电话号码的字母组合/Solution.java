import java.util.List;
import java.util.ArrayList;

class Solution {
    // 【降维打击 1：查表法字典】
    // 放弃笨重的 HashMap，利用数组天然的连续下标进行绝对的 O(1) 映射。
    // 这在工业界处理极度密集、范围极小的数据映射时，是性能最高的写法。
    private String[] phoneMap = {
        "",     // 0 号位空着（题目说输入只有 2-9）
        "",     // 1 号位空着
        "abc",  // 2 号位
        "def",  // 3 号位
        "ghi",  // 4 号位
        "jkl",  // 5 号位
        "mno",  // 6 号位
        "pqrs", // 7 号位
        "tuv",  // 8 号位
        "wxyz"  // 9 号位
    };
    
    // 全局结果集，档案室
    List<String> res = new ArrayList<>();
    
    public List<String> letterCombinations(String digits){
        // 🚨 边界防御：如果输入的电话号码是空的，直接返回空列表
        // 防止下面 digits.length() 报错，或者向 res 里塞入一个空的 ""
        if (digits == null || digits.length() == 0) {
            return res;
        }
        
        // 启动回溯引擎：传入原始输入，当前拨号的深度 0，以及极其高效的拼接器
        backtrack(digits, 0, new StringBuilder());
        return res;
    }

    // 回溯核心引擎：
    // index: 当前正在处理 digits 里的第几个数字（这是控制深度的指南针）
    // path: 当前拼装出来的字母组合
    private void backtrack(String digits, int index, StringBuilder path) {
        // 【触发结束条件】
        // 当我们拼凑的路径长度，等于电话号码的总长度时，说明所有的拨轮都拨完了。
        // （在这里用 index == digits.length() 作为条件也是等价的）
        if (path.length() == digits.length()) {
            // 【降维打击 2：快照收集】
            // 把 StringBuilder 转化为不可变的 String 对象，存入档案室
            res.add(path.toString());
            return;
        }
        
        // 【提取当前拨轮的数字】
        char s = digits.charAt(index);
        
        // 【横向遍历：当前拨轮上的所有字母】
        // s - '0' 是神仙操作：利用 ASCII 码将字符 '2' 转换成真正的整数 2
        // 然后从 phoneMap[2] 中取出 "abc"，并转换成字符数组 ['a', 'b', 'c'] 以便遍历
        for (char c : phoneMap[s - '0'].toCharArray()) {
            // --- 核心动作 1：做选择 ---
            // 极其高效地追加字符，不产生新的 String 垃圾对象
            path.append(c);
            
            // --- 核心动作 2：递归进入下一层（下一个数字拨轮）---
            // 注意这里是 index + 1，表示下一层要去破译下一个数字了！
            backtrack(digits, index + 1, path);
            
            // --- 核心动作 3：撤销选择 (回溯 / 悔棋) ---
            // 把刚刚追加到末尾的字母删掉，恢复案发现场。
            // 这样 for 循环下一次拿到新字母时，才能拼装出新的组合！
            path.deleteCharAt(path.length() - 1);
        }
    }
}