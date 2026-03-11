import java.util.HashMap;
import java.util.Map;

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
    
    /**
     * 主函数：根据前序和中序遍历构建二叉树
     * 时间复杂度：O(N) - N 为节点数，构建哈希表 O(N)，递归构建整棵树 O(N)
     * 空间复杂度：O(N) - 哈希表空间 O(N)，递归调用栈最坏 O(N)
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // 【工程化小贴士】你在这里传入了 inorder.length 作为初始容量，非常好。
        // 但大厂极其严苛的代码规范中，考虑到 HashMap 0.75 的负载因子，
        // 极致的写法可能是 new HashMap<>((int)(inorder.length / 0.75f) + 1)
        // 不过在这里直接传入 length 已经超越了 80% 的面试者。
        Map<Integer, Integer> inorderIndexMap = new HashMap<>(inorder.length);
        
        // 缓存中序遍历的“值 -> 索引”映射，将查找根节点的复杂度从 O(N) 降为 O(1)
        for (int i = 0; i < inorder.length; i++) {
            inorderIndexMap.put(inorder[i], i);
        }
        
        // 初始调用：传入前序和中序数组的完整边界 [0, length - 1]
        return buildTreeHelper(preorder, 0, preorder.length - 1,
                               inorder, 0, inorder.length - 1,
                               inorderIndexMap);
    }

    /**
     * 递归分治辅助函数
     * @param preorder        前序遍历数组
     * @param preorderStart   当前子树在前序数组中的起始边界（包含）
     * @param preorderEnd     当前子树在前序数组中的结束边界（包含）
     * @param inorder         中序遍历数组
     * @param inorderStart    当前子树在中序数组中的起始边界（包含）
     * @param inorderEnd      当前子树在中序数组中的结束边界（包含）
     * @param inorderIndexMap 中序遍历的值到索引的哈希映射
     */
    private TreeNode buildTreeHelper(int[] preorder, int preorderStart, int preorderEnd,
                               int[] inorder, int inorderStart, int inorderEnd,
                               Map<Integer, Integer> inorderIndexMap) {
        
        // Base Case 越界保护
        // 【Code Review 提示】这里通常用 if，因为满足条件直接 return 结束当前层了。
        // 用 while 逻辑上也绝对正确（因为第一轮就 return 弹出了），保留你的原汁原味！
        while (preorderStart > preorderEnd || inorderStart > inorderEnd) {
            return null;
        }
        
        // 1. 找老大：前序遍历的第一个节点，绝对是当前区间的根节点
        int rootValue = preorder[preorderStart];
        TreeNode root = new TreeNode(rootValue);
        
        // 2. 定位地盘：以 O(1) 速度在中序遍历中找到根节点的位置
        int rootIndexInorder = inorderIndexMap.get(rootValue);
        
        // 3. 算规模：计算左子树的节点数量 (中序遍历里，根节点索引减去起始索引)
        int leftSubtreeSize = rootIndexInorder - inorderStart;
        
        // 4. 分地盘：递归构建左子树
        // 前序新范围：起点往后挪一位 [preorderStart + 1], 终点是起点加上左子树长度 [preorderStart + leftSubtreeSize]
        // 中序新范围：起点不变 [inorderStart], 终点是根节点前一位 [rootIndexInorder - 1]
        root.left = buildTreeHelper(preorder, preorderStart + 1, preorderStart + leftSubtreeSize,
                                    inorder, inorderStart, rootIndexInorder - 1,
                                    inorderIndexMap);
                                    
        // 5. 分地盘：递归构建右子树
        // 前序新范围：左子树终点的下一位 [preorderStart + leftSubtreeSize + 1], 终点不变 [preorderEnd]
        // 中序新范围：起点是根节点后一位 [rootIndexInorder + 1], 终点不变 [inorderEnd]
        root.right = buildTreeHelper(preorder, preorderStart + leftSubtreeSize + 1, preorderEnd,
                                     inorder, rootIndexInorder + 1, inorderEnd,
                                     inorderIndexMap);
                                     
        // 返回当前构建好的根节点
        return root;
    }
}