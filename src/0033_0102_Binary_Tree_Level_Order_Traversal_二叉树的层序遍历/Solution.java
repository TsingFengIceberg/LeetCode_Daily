import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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


public class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        // 最终返回的结果集
        List<List<Integer>> res = new ArrayList<>();
        
        // 边界防范：空树直接返回空集
        if (root == null) {
            return res;
        }
        
        // 工程选型：使用 Queue 接口搭配 LinkedList
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        // 外层循环：控制树的层级不断向下扩散
        while (!queue.isEmpty()) {
            // 【核心魔法：层级快照】锁定当前层的节点数量
            int size = queue.size();
            
            // 准备一个篮子，专门用来装当前这一层的所有节点值
            List<Integer> currentLevel = new ArrayList<>();
            
            // 内层循环：严格按快照数量，收割当前层，并播种下一层
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();
                
                // 1. 收割：把当前节点的值放进当前层的篮子里
                currentLevel.add(current.val);
                
                // 2. 播种：把当前节点的左右孩子丢进队列，留给下一轮外层循环处理
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            // 当前层收割完毕，把篮子放进总结果集中
            res.add(currentLevel);
        }
        
        return res;
    }
}