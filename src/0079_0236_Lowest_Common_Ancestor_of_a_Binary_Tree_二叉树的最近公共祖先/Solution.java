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
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 1. Base Case：如果碰到底了，或者遇到了 p 甚至遇到了 q，直接向上返回 root
        if (root == null || root == p || root == q) {
            return root;
        }
        
        // 2. 派左小弟去左子树找，【把结果存下来！】千万别重复调用！
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        // 3. 派右小弟去右子树找，【把结果存下来！】
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        // 4. 汇总左右小弟的汇报结果 (灵魂三问)
        if (left != null && right != null) {
            // 左右都找到了人，说明 p 和 q 分散在我的两边，那我 root 就是最近公共祖先！
            return root; 
        }
        
        // 如果左边为空，说明都在右边，把右边的结果汇报给上一级
        // 如果右边为空，说明都在左边，把左边的结果汇报给上一级
        return left != null ? left : right;
    }
}