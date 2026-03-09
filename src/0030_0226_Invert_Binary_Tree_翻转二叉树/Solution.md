# 算法复盘笔记：LeetCode 226. 翻转二叉树 (Invert Binary Tree)

## 💡 核心思路：遍历与交换的艺术
本题的微观本质极其简单：**遍历整棵树，把每一个节点的 `left` 和 `right` 指针互换。**
由于二叉树具有完美的自相似性，我们既可以采用 **DFS (深度优先/递归)** 由下至上或由上至下地交换，也可以采用 **BFS (广度优先/迭代)** 像剥洋葱一样逐层互换。



---

## 💻 工业级代码与详尽注释

### 方法一：DFS 后序遍历（极简递归法）
**思路**：先命令左右小弟各自去翻转自己的子树，等小弟们汇报工作后，我再把左右小弟互换。
```java
public class Solution {
    public TreeNode invertTree(TreeNode root) {
        // 【Base Case】越过叶子节点，返回 null
        if (root == null) {
            return null;
        }
        
        // 【Divide & Conquer】先让左右子树各自完成翻转任务
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        
        // 【核心操作】交换当前节点的左右孩子
        // 注意：这里直接使用底层返回的节点赋值，省去了多余的 temp 变量，代码更优雅
        root.left = right;
        root.right = left;
        
        return root;
    }
}

```

* **时间复杂度**: $O(N)$。每个节点被访问且仅被访问一次。
* **空间复杂度**: $O(H)$。$H$ 为树的高度。最坏退化成链表时为 $O(N)$，平衡树时为 $O(\log N)$，消耗在于系统调用栈。

### 方法二：BFS 层序遍历（防爆栈迭代法）

**思路**：利用队列，按层访问节点，拿到一个节点就立刻互换它的左右手，然后把左右孩子塞进队列等待后续处理。

```java
import java.util.LinkedList;
import java.util.Queue;

public class Solution {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        
        // 【工程选型】使用 LinkedList 实现 Queue 接口
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            
            // 【核心操作】交换当前节点的左右指针
            TreeNode temp = current.left;
            current.left = current.right;
            current.right = temp;
            
            // 将不为空的左右孩子推入队列，排队等待下一轮的翻转
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
        return root;
    }
}

```

* **时间复杂度**: $O(N)$。
* **空间复杂度**: $O(N)$。最坏情况下（满二叉树），队列中最多同时存放 $N/2$ 个底层叶子节点。

---

## 🏗️ 真实大厂工程实例深度剖析 (Engineering Real-world Use Cases)

这道题在 LeetCode 上看似只是指针游戏，但在工业界却是底层框架设计的核心利器。

### 1. 前端/客户端中台：Virtual DOM 的 RTL 国际化适配

* **痛点**：字节跳动（TikTok）或阿里出海中东时，阿拉伯语/希伯来语是**从右向左 (RTL)** 阅读的，整个 UI 布局必须水平镜像翻转。
* **降维打击解法**：底层渲染引擎在计算虚拟 DOM 树（Virtual DOM Tree）布局前，执行一次类似 `invertTree` 的递归，将组件树所有节点的左右指针调换。不用改写上万行业务代码，瞬间实现 $O(N)$ 级别的全局镜像自适应。

### 2. 大数据计算引擎：SQL 优化器 (Optimizer) 的 AST 子树翻转

* **痛点**：在 Hive 或 Flink 中执行 `JOIN` 语句，如果解析出的抽象语法树 (AST) 将几十 GB 的大表放在了 `Hash Join` 节点的左侧（Build 端），会瞬间将 JVM 内存打爆引发 OOM。
* **降维打击解法**：基于规则的优化器 (RBO) 在执行前会探测 AST 树。当发现两侧数据量失衡时，直接**翻转 Join 节点的左右子树指针**，让小表去充当内存建表的 Build 端。几行指针调换的代码，拯救了整个分布式集群的生命线。

