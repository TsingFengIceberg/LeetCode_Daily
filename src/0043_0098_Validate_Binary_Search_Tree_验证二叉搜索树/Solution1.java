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

public class Solution1 {
    public boolean isValidBST(TreeNode root) {
        // 初始状态：根节点没有任何束缚。
        // 使用 Long.MIN_VALUE 和 Long.MAX_VALUE 完美避开 Integer 边界幽灵！
        return judgeBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean judgeBST(TreeNode node, long min, long max) {
        // Base case：空节点天然是合法的二叉搜索树
        if (node == null) {
            return true;
        }
        
        // 🚨 核心校验：如果当前节点敢越雷池一步（触碰了长辈传下来的上下界），直接判死刑！
        if (node.val <= min || node.val >= max) {
            return false;
        }
        
        // 📜 往下传圣旨：
        // 1. 往左子树走：下界不变（依然是 min），但上界变成了当前节点的值（node.val）。
        //    即：“左边的子孙们，你们全都不准超过我！”
        // 2. 往右子树走：上界不变（依然是 max），但下界变成了当前节点的值（node.val）。
        //    即：“右边的子孙们，你们全都不准比我小！”
        // 必须左右两边都合法，整棵树才合法 (&&)
        return judgeBST(node.left, min, node.val) && judgeBST(node.right, node.val, max);
    }
}