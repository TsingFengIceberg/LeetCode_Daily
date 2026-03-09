# 算法复盘笔记：LeetCode 101. 对称二叉树 (Symmetric Tree)

## 💡 核心思路：双指针同步下潜与镜像比对
验证对称性的本质，是验证**两棵子树是否互为镜像**。
无论是递归还是迭代，核心动作都是同时拉取两个节点（左子树节点 `L` 和 右子树节点 `R`）进行“三步连环问”：
1. **双空检验**：如果两个都为空，说明这条路对称到底了，返回 `true`。
2. **单空检验**：如果一个为空一个不为空，结构不对称，直接判死刑 `false`。
3. **值检验**：如果值不相等，直接判死刑 `false`。
如果以上都通过，则继续交叉比对它们的孩子：**外侧比外侧 (`L.left` 与 `R.right`)，内侧比内侧 (`L.right` 与 `R.left`)**。



---

## 💻 工业级代码与详尽注释

### 方法一：DFS 递归法 (自顶向下分解任务)
**面试官 Code Review 视角**：判空逻辑极为严谨，代码极其优雅。
```java
class Solution1 {
    public boolean isSymmetric(TreeNode root) {
        // LeetCode 提示节点数 >= 1，但工程习惯上最好加一句防空指针：
        // if (root == null) return true;
        return IsSame(root.left, root.right);
    }

    private boolean IsSame(TreeNode left, TreeNode right) {
        // 1. 结构比对：双空为真
        if (left == null && right == null) {
            return true;
        }
        // 2. 结构比对：单空为假 (到这里说明必定有一个不为空)
        if (left == null || right == null) {
            return false;
        }
        // 3. 值比对
        if (left.val != right.val) {
            return false;
        }
        // 4. 交叉递归：外侧对接外侧，内侧对接内侧
        return IsSame(left.left, right.right) && IsSame(left.right, right.left);
    }
}

```

* **时间复杂度**: $O(N)$。遍历整棵树，每个节点处理一次。
* **空间复杂度**: $O(H)$。$H$ 为树的高度。最坏退化成链表时为 $O(N)$，平衡树时为 $O(\log N)$，消耗在于系统调用栈。

### 方法二：BFS 迭代法 (队列成对出入机制)

**面试官 Code Review 视角**：通过队列实现了手动模拟的双指针同步移动，有效防止极深树结构的栈溢出 (`StackOverflowError`)。

```java
import java.util.LinkedList;
import java.util.Queue;

public class Solution2 {
    public boolean isSymmetric(TreeNode root) {
        // 工程规范：引入 Queue 接口
        Queue<TreeNode> queue = new LinkedList<>();
        
        // 【起手式】将根节点的左右孩子作为第一对候选人入队
        queue.offer(root.left);
        queue.offer(root.right);
        
        while (!queue.isEmpty()) {
            // 每次循环强制连续弹出两个节点进行比对
            TreeNode left = queue.poll();
            TreeNode right = queue.poll();
            
            // 镜像判断逻辑与递归完全一致
            if (left == null && right == null) {
                continue; // 这一对匹配成功，且没有孩子了，继续检查队列里的下一对
            }
            if (left == null || right == null) {
                return false;
            }
            if (left.val != right.val) {
                return false;
            }
            
            // 【核心灵魂：严格的交叉入队顺序】
            // 必须保证下一轮 poll 出来的两个节点是镜像位置！
            // 1. 先塞一对“外侧”
            queue.offer(left.left);
            queue.offer(right.right);
            // 2. 再塞一对“内侧”
            queue.offer(left.right);
            queue.offer(right.left);
        }
        
        return true;
    }
}

```

* **时间复杂度**: $O(N)$。每个节点进队一次，出队一次。
* **空间复杂度**: $O(N)$。最坏情况下，队列里要装满底层的节点（最多约为 $N/2$）。

---

## 🏗️ 真实大厂工程实例深度剖析

* **自动化测试中台 (跨语言布局验证)**：在测试 UI 虚拟 DOM 树从 LTR（从左向右）切换为 RTL（从右向左，如阿拉伯语）时，底层会调用镜像对比算法，判断渲染出的两棵 DOM 树是否完美对称。左侧面板的按钮必须精准映射到右侧对应位置。
* **数据库/编译器内核 (表达式去重)**：解析 SQL 生成抽象语法树 (AST) 时，优化器利用对称树算法识别如 `(A + B)` 和 `(B + A)` 这样的可交换操作符。一旦匹配为镜像子树，直接在执行计划中进行去重合并，砍掉一半的计算成本。
