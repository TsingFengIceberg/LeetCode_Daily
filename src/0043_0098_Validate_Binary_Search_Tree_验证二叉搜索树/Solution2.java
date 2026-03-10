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
    // 幽灵指针：用来记录“上一个访问过的节点的值”
    // 初始值设为 Long 的最小值，保证第一个节点绝对能大于它
    private long prev = Long.MIN_VALUE;
    
    public boolean isValidBST(TreeNode root) {
        // Base case：走到树的尽头了，合法
        if (root == null) {
            return true;
        }
        
        // ⬅️ 1. 先一路向左，深入到整棵树的最左下角（最小值所在处）
        // 如果左子树已经被查出不是 BST，直接向上报警拦截
        if (!isValidBST(root.left)) {
            return false;
        }
        
        // 🎯 2. 处理当前节点（中）：极其残酷的递增校验！
        // 因为被“拍扁”后，当前节点的值必须【严格大于】上一个访问的节点的值 (prev)
        // 如果发现当前节点 <= prev，说明序列乱套了（比如右子树里混进了小数字），直接判死刑！
        if (root.val <= prev) {
            return false;
        }
        
        // 校验通过，当前节点顺利成为新的“过去式”，供下一个节点去比较
        prev = root.val;
        
        // ➡️ 3. 左边和中间都查完了，最后去查右子树
        return isValidBST(root.right);
    } 
}