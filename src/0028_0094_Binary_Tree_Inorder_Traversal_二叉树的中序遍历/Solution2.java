import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

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
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        // Java 工程规范：推荐使用 Deque 接口来实现栈，而不是过时的 Stack 类
        Deque<TreeNode> stack = new LinkedList<>();
        
        TreeNode curr = root;
        
        // 核心循环：当前节点不为空，或者栈不为空时，说明还没遍历完
        while (curr != null || !stack.isEmpty()) {
            
            // 1. 疯狂向左走，把路上的左孩子全部压栈
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            
            // 2. 左边走到头了（此时 curr 为 null），从栈顶弹出一个节点
            curr = stack.pop();
            
            // 3. 访问该节点（将其值加入结果集）
            res.add(curr.val);
            
            // 4. 转向该节点的右子树，准备下一轮的压栈
            curr = curr.right;
        }
        
        return res;
    }
}