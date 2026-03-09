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


public class Solution {
    // 1. 定义一个全局记分牌，用来记录历史出现过的最大直径
    private int maxDiameter = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        // 触发递归遍历
        getDepth(root);
        // 遍历结束后，记分牌上保留的就是整棵树的全局最大直径！
        return maxDiameter;
    }

    private int getDepth(TreeNode node) {
        if (node == null) return 0;
        
        int leftDepth = getDepth(node.left);
        int rightDepth = getDepth(node.right);
        
        // 【暗中观察与更新】：在这里，利用 leftDepth 和 rightDepth，
        // 偷偷更新全局记分牌 maxDiameter！
        maxDiameter = Math.max(maxDiameter, leftDepth + rightDepth);
        
        // 向上级汇报我这个节点的深度
        return Math.max(leftDepth, rightDepth) + 1;
    }
}