# 算法复盘笔记：LeetCode 102. 二叉树的层序遍历 (Binary Tree Level Order Traversal)




## 💡 核心思路：BFS 与“队列大小快照法”
层序遍历（广度优先搜索 BFS）的核心，是利用队列（Queue）“先进先出”的特性，像水波纹一样一层层向外扩展。

**大厂破局基石：如何区分哪几个节点属于“同一层”？**
答案是**层级快照 (Snapshot)**。在每一层循环开启**之前**，先用 `size = queue.size()` 记录下当前队列里有几个元素。接下来的内部 `for` 循环严格执行 `size` 次，只处理当前层的节点，并将它们的下一代安全地排到队列末尾。



---

## 💻 工业级代码与详尽注释

```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res; // 边界防范
        }
        
        // 【工程选型】使用 Queue 接口搭配 LinkedList 实现 BFS
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        // 外层循环：控制整体向下探索的层数
        while (!queue.isEmpty()) {
            // 【核心魔法：层级快照】锁定当前层需要处理的节点数量
            int size = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            
            // 内层循环：按快照数量，精准收割当前层，播种下一层
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();
                currentLevel.add(current.val);
                
                // 将左右子节点按顺序排入队列末尾
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            // 当前层处理完毕，装入最终结果集
            res.add(currentLevel);
        }
        return res;
    }
}

```

### 📊 复杂度分析

* **时间复杂度**: $O(N)$。每个节点进队出队各一次，并且加入子列表一次，操作皆为常数时间。
* **空间复杂度**: $O(N)$。主要开销为底层的 `Queue`。最坏情况下（一棵完美的满二叉树），最底层的叶子节点有大约 $N/2$ 个会同时存在于队列中。
