import java.util.List;
import java.util.ArrayList;

class Solution {
    List<List<Integer>> res = new ArrayList<>();
    public List<List<Integer>> permute(int[] nums) {
        backtrack(nums, new ArrayList<>(), new boolean[nums.length]);
        return res;
    }

    // res 是全局的最终结果集，path 是当前正在走的路径，used 记录元素是否被使用
    private void backtrack(int[] nums, List<Integer> path, boolean[] used) {
        // 1. 【触发结束条件】
        // 如果 path 里面装满数字了（长度等于 nums 的长度）
        if (path.size() == nums.length) {
            // 结合我刚教你的“拍照片”技巧，你怎么把 path 加入到 res 里？
            res.add(new ArrayList<>(path));
            return;
        }

        // 2. 【遍历所有候选节点】
        for (int i = 0; i < nums.length; i++) {
            // 如果当前数字已经被用过了，直接跳过它！(剪枝)
            // 你的代码思路：判断 used[i] 是否为 true ...
            if (used[i]) {
                continue;
            }
            
            // --- 核心动作 1：做选择 ---
            // 1. 把它标记为已使用
            // 2. 把 nums[i] 加入 path 列表的末尾
            // 你的代码：...
            used[i] = true;
            path.add(nums[i]);
            
            // --- 进入下一层决策树 ---
            backtrack(nums, path, used);
            
            // --- 核心动作 2：撤销选择 (回溯 / 悔棋) ---
            // 1. 把它标记为“未使用”
            // 2. 把刚刚加入 path 的最后一个元素删掉！(恢复案发现场)
            // 你的代码：...
            used[i] = false;
            path.remove(path.size() - 1);
        }
    }
}