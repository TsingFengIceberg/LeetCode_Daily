import java.util.HashMap;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}


class Solution {
    public int pathSum(TreeNode root, int targetSum) {
        // Key: 前缀和 (必须用 Long 防溢出)
        // Value: 该前缀和在当前连续向下的路径中出现的次数
        HashMap<Long, Integer> prefixSumCount = new HashMap<>();
        
        // 🚨 极其关键的初始化（垫底值）
        // 如果一条路径直接从根节点开始，且恰好等于 targetSum
        // 此时 currSum - targetSum = 0。为了能正确统计到这种情况，必须预置 0L 出现了 1 次。
        prefixSumCount.put(0L, 1);
        
        // 使用一个辅助方法进行 DFS，避免修改原树的节点值
        return dfs(root, prefixSumCount, targetSum, 0L);
    }

    private int dfs(TreeNode node, HashMap<Long, Integer> prefixSumCount, int targetSum, long currSum) {
        if (node == null) {
            return 0;
        }

        // 1. 更新当前路径的前缀和
        currSum += node.val;

        // 2. 核心逻辑：检查是否存在合法的截断路径
        // 只要历史路径中存在前缀和为 (currSum - targetSum) 的节点，
        // 说明从那个节点到当前节点的路径和正好就是 targetSum。
        int count = prefixSumCount.getOrDefault(currSum - targetSum, 0);

        // 3. 将当前前缀和加入哈希表，准备向左右子树传递
        prefixSumCount.put(currSum, prefixSumCount.getOrDefault(currSum, 0) + 1);

        // 4. 递归遍历左右子树
        count += dfs(node.left, prefixSumCount, targetSum, currSum);
        count += dfs(node.right, prefixSumCount, targetSum, currSum);

        // 5. 🚨 状态回溯（恢复现场）
        // 重点：离开当前节点，返回上一层时，必须把当前节点的前缀和从 Map 中扣除 1
        // 这样才绝对不会去污染其他兄弟分支的计算逻辑！
        prefixSumCount.put(currSum, prefixSumCount.get(currSum) - 1);

        return count;
    }
}