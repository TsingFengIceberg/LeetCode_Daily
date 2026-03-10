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
    public void flatten(TreeNode root) {
        flattenAndReturnTail(root);
    }

    // 核心辅助函数：把以 node 为根的树拍扁，并返回拍扁后的【尾巴节点】
    private TreeNode flattenAndReturnTail(TreeNode node) {
        // Base case 1：空节点，没有尾巴，返回 null
        if (node == null) {
            return null;
        }
        // Base case 2：叶子节点，已经被拍扁了，尾巴就是它自己
        if (node.left == null && node.right == null) {
            return node;
        }

        // 1. 分 (Divide)：让小弟去把左右子树分别拍扁，并把它们的尾巴汇报给我
        TreeNode leftTail = flattenAndReturnTail(node.left);
        TreeNode rightTail = flattenAndReturnTail(node.right);

        // 2. 治 (Conquer)：如果左子树存在，开始缝合！
        if (leftTail != null) {
            // 把左子树的尾巴，连上原本的右子树（不管右边是啥，先连上再说）
            leftTail.right = node.right;
            // 把拍扁后的左子树，整体移到右边去
            node.right = node.left;
            // 清空左指针
            node.left = null;
        }

        // 3. 向上汇报尾巴：
        // 如果原本的右子树存在，那整棵树拍扁后的尾巴肯定是 rightTail
        // 如果原本的右子树为空，那尾巴就是刚刚移到右边去的 leftTail
        return rightTail != null ? rightTail : leftTail;
    }
}