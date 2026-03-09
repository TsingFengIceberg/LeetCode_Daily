import java.util.LinkedList;
import java.util.Queue;

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

class Solution2 {
    public TreeNode invertTree(TreeNode root) {
        // 边界防范：空树直接返回
        if (root == null) {
            return null;
        }
        
        // 工程规范：使用 Queue 接口搭配 LinkedList 实现队列
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        // 只要队列里还有节点，就说明还有子树没被翻转
        while (!queue.isEmpty()) {
            // 1. 从队列里拿出一个节点
            TreeNode current = queue.poll();
            
            // 2. 核心操作：交换该节点的左右小弟 (这就是你在递归里写的 temp 交换逻辑)
            TreeNode temp = current.left;
            current.left = current.right;
            current.right = temp;
            
            // 3. 把小弟们丢进队列，等后面排队轮到它们时，再去翻转它们的子树
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
        
        return root;
    }
}