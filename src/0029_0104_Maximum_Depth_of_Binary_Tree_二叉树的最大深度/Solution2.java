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

public class Solution2 {
    public int maxDepth(TreeNode root) {
        // 【Base Case / 终止条件】
        // 递归最重要的一步：如果当前探索到了空节点（越过了叶子节点），
        // 那么空节点对深度的贡献是 0，直接向上级返回 0。
        if (root == null) {
            return 0;
        }
        
        // 【Divide / 分治拆解】
        // 抛出问题给左小弟：帮我求出左子树的最大深度。
        // 这是一次方法调用栈的压栈操作（下潜）。
        int leftDepth = maxDepth(root.left);
        
        // 抛出问题给右小弟：帮我求出右子树的最大深度。
        int rightDepth = maxDepth(root.right);
        
        // 【Conquer / 合并结果】
        // 左右小弟都把结果汇报上来了。当前这棵树的最大深度是多少呢？
        // 当然是取左右两边更深的那一个（Math.max），然后再算上我自己这个根节点（+1），
        // 最后把这个总和汇报给我的上一级。
        return Math.max(leftDepth, rightDepth) + 1;
    }
}