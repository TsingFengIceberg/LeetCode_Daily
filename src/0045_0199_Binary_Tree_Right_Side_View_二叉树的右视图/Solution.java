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


class Solution {
    public List<Integer> rightSideView(TreeNode root) {
        // 存放最终右视图结果的集合
        ArrayList<Integer> view = new ArrayList<>();
        // 核心武器：队列，用于广度优先搜索 (BFS) / 层序遍历
        Queue<TreeNode> queue = new LinkedList<>();
        
        TreeNode currentNode = null;
        int currentSize = 0;
        
        // Base case: 如果是一棵空树，直接返回空集合，防止空指针异常
        if (root == null) {
            return view;
        }
        
        // 根节点入队，开启层序遍历的第一层
        queue.add(root);
        
        // 只要队列不为空，说明还有下一层需要探索
        while (!queue.isEmpty()) {
            // 🌟 核心定格：拍照记下当前层到底有几个节点
            // 必须提前提取出来，因为等下我们会往队列里塞下一层的节点，队列大小会动态变化
            currentSize = queue.size();
            
            // 巧妙的倒计时遍历：逐个处理当前层的所有节点
            for (int i = currentSize; i > 0; i--) {
                // 把节点从队列头部请出来
                currentNode = queue.poll();
                
                // 🎯 灵魂判断：当倒计时走到 1 时，说明当前节点是这一层的【最后一位乘客】！
                // 每一层的最后一位乘客，就是从右边看过去唯一能看到的那个节点！
                if (i == 1) {
                    view.add(currentNode.val);
                }
                
                // 为下一层做准备：如果有左亲儿子，塞进队列尾部排队
                if (currentNode.left != null) {
                    queue.add(currentNode.left);
                }
                // 为下一层做准备：如果有右亲儿子，塞进队列尾部排队
                // 注意顺序：先左后右，这样才能保证同一层里，最右边的节点永远是最后出队的
                if (currentNode.right != null) {
                    queue.add(currentNode.right);
                }   
            }
        }
        
        // 遍历结束，所有层的最右侧节点都已经收集完毕
        return view;
    }
}