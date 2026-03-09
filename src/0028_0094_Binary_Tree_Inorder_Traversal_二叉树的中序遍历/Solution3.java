import java.util.ArrayList;
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

class Solution3 {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        
        // Morris 遍历的核心：当前节点 root 不为空时持续推进
        while (root != null) {
            
            // 【情况 1：左子树为空】
            if (root.left == null) {
                // 根据中序遍历“左-根-右”的法则，既然左边没路了，就该访问自己了
                ans.add(root.val);
                // 访问完自己后，向右子树进发（如果右子树是我们之前建立的线索，这里就会神奇地回到上层节点！）
                root = root.right;
            } 
            
            // 【情况 2：左子树不为空】
            else {
                // 我们需要找到当前节点 root 在中序遍历下的“前驱节点”
                // （即左子树中最右下角的那个节点）
                TreeNode prev = root.left;
                
                // 不断向右下角试探。
                // 这里的 prev.right != root 是灵魂：用于判断这棵树是不是已经被我们改造（线索化）过了
                while (prev.right != null && prev.right != root) {
                    prev = prev.right;
                }
                
                // 此时，prev 已经精准定位到了前驱节点。接下来分两种状态：
                
                // 【状态 2.1：第一次来到该节点】
                if (prev.right == null) {
                    // 建立线索：借用这个本该为空的右指针，让它指向当前的 root。
                    // 这样当左子树全遍历完时，我们就能顺着这根“线”，不用栈也能找回回家的路！
                    prev.right = root;
                    // 线索建好后，安心地继续深入左子树
                    root = root.left;
                } 
                
                // 【状态 2.2：第二次来到该节点（从线索顺藤摸瓜回来的）】
                else {
                    // 此时 prev.right == root，说明这棵左子树我们已经完完整整地遍历过了。
                    // 按照“左-根-右”，左边走完了，现在理所应当该把当前的 root 记入答案！
                    ans.add(root.val);
                    // 【大厂极其看重的工程操守】：用完即焚！把我们临时改动的树结构恢复原样
                    prev.right = null;
                    // 左边和根都搞定了，大步迈向右子树去处理剩下的部分
                    root = root.right;
                }
            }
        }
        return ans;
    }
}