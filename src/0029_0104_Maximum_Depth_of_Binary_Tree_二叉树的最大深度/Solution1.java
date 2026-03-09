import java.util.Queue;
import java.util.LinkedList;

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
    public int maxDepth(TreeNode root) {
        // 【工程选型】使用 Queue 接口搭配 LinkedList 实现，这是 Java 中广度优先搜索 (BFS) 的标准结构。
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode nd = root;
        int depth = 0; // 记录树的最大深度（层数）
        int size = 0;  // 用于记录每一层节点的数量（快照）
        
        // 【边界防范】如果进来就是一棵空树，直接返回深度 0
        if (nd == null) {
            return 0;
        }
        
        // 初始状态：将根节点入队，开启水波纹的第一层
        queue.offer(nd);
        
        // 外层循环：只要队列不为空，说明还有下一层需要探索
        while (!queue.isEmpty()) {
            // 【核心魔法：层级快照】在处理当前层之前，先记录下当前队列里有几个元素。
            // 这一步极其关键！因为在后面的 for 循环中，我们会不断往队列里塞入下一层的孩子，
            // 只有固定了 size，才能保证我们这轮循环只处理“当前层”的节点。
            size = queue.size();
            
            // 内层循环：严格按照快照的 size 遍历当前层的所有节点
            for (int i = 0; i < size; i++) {
                // 弹出当前层的一个节点
                nd = queue.poll();
                
                // 【拓展下一层】如果它有左孩子，将左孩子入队（为下一层做准备）
                if (nd.left != null) {
                    queue.offer(nd.left);
                }
                // 如果它有右孩子，将右孩子入队
                if (nd.right != null) {
                    queue.offer(nd.right);
                }
            }
            // 当内部 for 循环结束时，意味着“当前这一层”的节点已经全部出队，
            // 且“下一层”的节点已经全部入队。此时层数计数器 +1。
            depth++;
        }
        
        // 队列清空，意味着所有层都遍历完毕，返回累加的总层数。
        return depth;
    }
}