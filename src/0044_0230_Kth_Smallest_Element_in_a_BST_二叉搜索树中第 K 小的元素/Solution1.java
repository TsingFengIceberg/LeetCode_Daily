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

class Solution1 {
    private int count = 0;
    
    public int kthSmallest(TreeNode root, int k) {
        if (root == null) {
            return -1; // Return -1 if the tree is empty
        }
        int left = kthSmallest(root.left, k);
        if (left != -1) {
            return left; // If the left subtree has the kth smallest, return it
        }
        count++;
        if (count == k) {
            return root.val; // If the current node is the kth smallest, return its value
        }
        int right = kthSmallest(root.right, k);
        if (right != -1) {
            return right; // If the right subtree has the kth smallest, return it
        }
        return -1; // Return -1 if the kth smallest element is not found
    }
}