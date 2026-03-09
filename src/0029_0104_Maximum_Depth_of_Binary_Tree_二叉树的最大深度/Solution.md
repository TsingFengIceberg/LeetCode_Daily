# 算法复盘笔记：LeetCode 104. 二叉树的最大深度 (Maximum Depth of Binary Tree)


## 💡 核心思路：两大遍历阵营的经典碰撞
求一棵树的最大深度，本质上有两种截然不同的降维打击方式：
1. **横向扩展（BFS 广度优先搜索）**：像水波纹一样一层层荡开，用一个计数器记录荡了多少层。核心技术点是**“队列大小快照法”**。
2. **纵向深挖（DFS 深度优先搜索）**：基于后序遍历的分治思想，分别向左右子树索要它们的最大深度，取最大值后加上当前根节点的一层。

---

## 💻 工业级代码与详尽注释

### 方法一：BFS 层序遍历（迭代法 / 队列快照）

```java
import java.util.Queue;
import java.util.LinkedList;

public class Solution1 {
    public int maxDepth(TreeNode root) {
        // 【工程选型】使用 Queue 接口搭配 LinkedList 实现 BFS
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode nd = root;
        int depth = 0; // 记录总层数
        int size = 0;  // 记录每一层节点数的“快照”
        
        // 边界防范
        if (nd == null) {
            return 0;
        }
        
        queue.offer(nd);
        
        while (!queue.isEmpty()) {
            // 【核心魔法：层级快照】记录当前队列大小，锁定“当前层”的边界
            size = queue.size();
            
            // 严格按照快照大小进行内部循环，把当前层的节点全部清空，并塞入下一层
            for (int i = 0; i < size; i++) {
                nd = queue.poll();
                if (nd.left != null) {
                    queue.offer(nd.left);
                }
                if (nd.right != null) {
                    queue.offer(nd.right);
                }
            }
            // 一层遍历彻底结束，深度 +1
            depth++;
        }
        return depth;
    }
}

```

* **时间复杂度**: $O(N)$。其中 $N$ 是二叉树的节点数，每个节点精确出队入队一次。
* **空间复杂度**: $O(N)$。最坏情况下（一棵完美的满二叉树），最底层的叶子节点有 $N/2$ 个，它们会同时存在于队列中，因此空间消耗为线性。

### 方法二：DFS 后序遍历（递归分治法）

```java
public class Solution2 {
    public int maxDepth(TreeNode root) {
        // 【Base Case】越过叶子节点到达 null，深度贡献为 0
        if (root == null) {
            return 0;
        }
        
        // 【Divide】抛出问题：分别向左右子树探寻最大深度（压栈下潜）
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        
        // 【Conquer】合并结果：取左右两边更深的那一个，加上当前这一层 (+1)
        return Math.max(leftDepth, rightDepth) + 1;
    }
}

```

* **时间复杂度**: $O(N)$。每个节点在递归中被访问一次。
* **空间复杂度**: $O(H)$。其中 $H$ 是二叉树的高度。主要开销在于 JVM 的方法调用栈。在最坏情况下（树退化成链表），递归深度达到 $N$，空间为 $O(N)$；在最好情况下（平衡二叉树），高度为 $\log N$，空间为 $O(\log N)$。

---

## 🏗️ 真实大厂工程实例深度剖析 (Engineering Real-world Use Cases)

算法的选择在海量数据和分布式架构下，往往意味着系统生与死的抉择。

### 1. 广度优先搜索 (BFS) —— 搜索引擎爬虫与二度人脉

* **业务场景**：字节抖音推荐“可能认识的人”（二度人脉），或者百度/Google 的爬虫引擎顺着超链接爬取全网。
* **工程痛点与选型**：在爬虫业务中，绝不能用 DFS！因为互联网中存在大量恶意的**“爬虫陷阱 (Spider Trap)”**（比如无限深度的动态生成目录）。如果用 DFS 递归，爬虫会陷入无底洞导致单机 StackOverflow。
* **大厂架构改造**：真实工业界不会用单机 `LinkedList`。我们会把 BFS 的队列替换成**分布式消息中间件 (Kafka/RocketMQ)**。将网页 URL 作为消息塞入 Kafka，下游成百上千台机器作为 Consumer 消费并解析出下一层 URL 继续塞回 Kafka，实现抗并发的分布式层序遍历！

### 2. 深度优先搜索 (DFS) —— 微服务全链路追踪系统

* **业务场景**：阿里双十一大促，用户点击“下单”，请求在后台串行/并行经过了网关、订单、库存、促销等几十个微服务。
* **工程痛点与选型**：一旦接口超时，如何找出是哪个底层服务拖了后腿？整个调用链构成了一棵巨大的“Trace Tree”。
* **大厂架构实现**：像阿里的 EagleEye（鹰眼）或开源的 SkyWalking，其计算关键路径耗时的底层逻辑就是 **DFS（后序遍历）** 的变种。正如 `maxDepth` 递归一样，只有当底层的库存接口（左子树）和促销接口（右子树）都返回耗时结果后，上层的订单服务（根节点）才能计算出自身分支的总耗时，从而精准算出这棵调用树的“最大深度/最长耗时路径”。

