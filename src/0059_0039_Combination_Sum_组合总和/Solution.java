import java.util.List;
import java.util.ArrayList;

class Solution {
    // 全局结果集，用来装所有凑齐 target 的合法组合
    List<List<Integer>> res = new ArrayList<>();
    
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        // 启动回溯引擎：传入候选数组，空背包 path，初始目标值 target，以及防倒车起跑线 0
        backtrack(candidates, new ArrayList<>(), target, 0);
        return res;
    }

    // 回溯核心引擎：
    // nums: 候选数组
    // path: 当前背包里装的数字集合
    // target: 当前还【剩余】多少额度需要凑齐（做减法）
    // start: 当前这层允许挑选的起始下标（物理隔绝，防倒车）
    private void backtrack(int[] nums, List<Integer> path, int target, int start) {
        // 【1. 剪枝：废片处理】
        // 如果 target 减成了负数，说明刚才选的数字太大了，这条路彻底走爆了。
        // 立刻 return 终止这条分支，回头去试别的数字。
        if (target < 0) {
            return;
        }
        
        // 【2. 收集：成功快照】
        // 如果 target 刚好减到 0，说明背包里的数字完美凑齐了目标值！
        if (target == 0) {
            // 🚨 核心微操：必须拍快照（深拷贝），否则 path 后续的回溯操作会把结果全清空
            res.add(new ArrayList<>(path));
            return;
        }
        
        // 【3. 横向遍历与做选择】
        // 🚨 防倒车机制：循环严格从 start 开始。跨过前面的数字后，子子孙孙再也看不到它们，彻底杜绝 [3, 2] 这种重复组合！
        for (int i = start; i < nums.length; i++) {
            // --- 核心动作 1：做选择 ---
            // 把当前看到的数字装进背包
            path.add(nums[i]);
            
            // --- 核心动作 2：递归进入下一层深渊 ---
            // 🚨 【无限弹药魔法】：注意最后一个参数传的是当前的 i！
            // 既然传了 i，下一层循环依然会从 i 开始扫，所以它能无限次重复选中当前的 nums[i]。
            // 而 target 减去 nums[i]，代表额度变少了，向下逼近结束条件。
            backtrack(nums, path, target - nums[i], i);
            
            // --- 核心动作 3：撤销选择 (回溯 / 悔棋) ---
            // 从深渊退回来后，把刚才装进去的数字拿出来，恢复案发现场。
            // 这样循环进入下一次 i++ 时，背包是干净的，可以去装下一个候选数字。
            path.remove(path.size() - 1);
        }
    }
}