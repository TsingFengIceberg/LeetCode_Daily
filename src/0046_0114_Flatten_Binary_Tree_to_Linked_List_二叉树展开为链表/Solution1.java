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
    public void flatten(TreeNode root) {
        // Base case: 树为空，直接返回，防空指针
        if (root == null) {
            return;
        }
        
        // current 是我们拿着剪刀一路往下走的“园丁游标”
        TreeNode current = root;
        // prev 是我们每次派出去寻找“接盘侠”的探子
        TreeNode prev = null;
        
        // 只要游标还没走到单链表的尽头，就继续做手术
        while (current != null) {
            // 核心判断：只有当前节点有左树枝，才需要进行物理切割和拼接
            if (current.left != null) {
                // 1. 寻找接盘侠：派探子 prev 进入左子树
                prev = current.left;
                // 让探子疯狂往右跑，直到跑到左子树的最右下角
                while (prev.right != null) {
                    prev = prev.right;
                }
                
                // 2. 物理断骨重连（极其血腥优雅的三步走）：
                // 第一步：把 current 原本的一大坨右子树，用胶水死死粘在接盘侠的右边
                prev.right = current.right;
                // 第二步：把 current 原本的整坨左子树，整体平移到 current 的右边
                current.right = current.left;
                // 第三步：过河拆桥，把 current 的左指针彻底切断（设为空）
                current.left = null;
            }
            
            // 3. 无论刚才做没做手术，现在 current 的左边绝对是空的。
            // 园丁极其自信地顺着右边的藤蔓，往下一步走，去处理下一个节点！
            current = current.right;
        }
    }
}