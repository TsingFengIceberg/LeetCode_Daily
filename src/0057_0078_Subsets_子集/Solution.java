import java.util.List;
import java.util.ArrayList;

class Solution {
    // 全局结果集，用来装所有的子集（相当于存放所有快照的档案室）
    List<List<Integer>> res = new ArrayList<>();
    
    public List<List<Integer>> subsets(int[] nums) {
        // 起手式：传入原数组、空的当前子集 sub（作为初始路径）、以及控制遍历不回头的 start 指针（初始为 0）
        backtrack(nums, new ArrayList<>(), 0);
        return res;
    }

    // 回溯核心函数：
    // nums: 原数组
    // sub: 当前正在构建的子集路径
    // start: 当前层允许选择的起始下标（核心防倒车机制）
    private void backtrack(int[] nums, List<Integer> sub, int start) {
        
        // 【核心差异 1：无条件拍照收集】
        // 在“全排列”中，必须等 sub 满了才能收集。
        // 但在“子集”中，决策树的【每一个节点】都是合法答案（包括最开始刚进来的空集 []）。
        // 所以不需要 if 终止条件，一进函数，立刻对当前 sub 进行深拷贝（拍快照）存入结果集！
        res.add(new ArrayList<>(sub));

        // 【核心差异 2：防倒车的 start 指针】
        // 循环严格从 start 开始，而不是从 0 开始。
        // 保证了选过 nums[i] 后，下一层只能选 i 及其后面的元素。
        // 这彻底杜绝了 [1, 2] 和 [2, 1] 的重复，也让全排列里的 used 数组彻底下岗！
        for (int i = start; i < nums.length; i++) {
            // --- 核心动作 1：做选择 ---
            // 抓住当前的数字 nums[i]，把它加入到当前子集里
            sub.add(nums[i]);
            
            // --- 核心动作 2：递归进入下一层决策树 ---
            // 🚨 极其关键的一步：传的是 i + 1！
            // 表示下一层递归，只能从当前选的数字的【下一个位置】开始挑，永远不回头！
            backtrack(nums, sub, i + 1);
            
            // --- 核心动作 3：撤销选择 (回溯 / 悔棋) ---
            // 把刚刚加进去的最后一个元素踢出来，恢复案发现场。
            // 这样 for 循环进入下一次迭代时，sub 是干净的，可以继续尝试装入下一个兄弟节点。
            sub.remove(sub.size() - 1);
        }
    }
}