# [0437. 路径总和 III (Path Sum III)]

## 💡 启发式探索：前缀和思想在树形结构上的降维打击
在普通的数组中（如 560 题），前缀和是线性的。但在这道题中，我们将“前缀和+哈希表”的思想平移到了带有分支的二叉树上。
核心逻辑在于：当我们顺着树向下遍历，走到某个节点时，如果当前的累计前缀和为 `currSum`，我们只需要回头在哈希表中查询是否存在前缀和为 `currSum - targetSum` 的历史记录。如果有，说明从那个历史节点到当前节点的**中间截断路径**，其节点值之和恰好等于 `targetSum`。

## 🚨 踩坑实录与面试官灵魂拷问（一字不差的答疑错误记录）
在初次尝试拆解时，你暴露出了一些在基础数据结构和工程经验上的薄弱点，这也是本题最有价值的沉淀：

* **错误点 1：对时间/空间复杂度的直觉偏差**
    * 你的原话：“*时间复杂度logn？空间复杂度logn?*”
    * **纠偏**：这是对树的 DFS 遍历理解不深。实际上我们遍历了树上的**每一个节点**且仅访问一次，哈希表的存取是 $O(1)$ 的，因此**时间复杂度是严格的 $O(N)$**。空间复杂度受限于递归调用的栈深和哈希表大小，最坏情况（树退化为链表）下也是 **$O(N)$**。
* **错误点 2：忽略大数相加的整型溢出陷阱**
    * 你的原话：“*溢出？如何防范我不清楚诶*”
    * 你的代码原样：“`root.left.val += root.val;`”
    * **纠偏**：题目明确给定 `Node.val` 的范围是 $[-10^9, 10^9]$。如果树结构很深，前缀和累加极易突破 Java `int` 类型的最大值（约 21 亿），导致整型溢出变成负数。必须使用 `long` 类型来承载前缀和传递，并将哈希表定义为 `HashMap<Long, Integer>`。
* **错误点 3：工程化大忌——修改（污染）输入参数**
    * 你在初版代码中，为了偷懒直接把原树的值改成了前缀和（`root.left.val += root.val;`）。
    * **纠偏**：在真实的工业界，这棵树可能正缓存在内存中被高并发读取。为了算一个路径和直接毁容原树（Side Effect），会导致其他业务接口全部崩溃。必须通过方法参数 `long currSum` 将状态向下传递。
* **错误点 4：遗漏根节点自身满足条件的边界垫底值**
    * 你的初版代码直接 `new HashMap<>()` 后就开始 get。
    * **纠偏**：如果一条路径直接从根节点开始且正好等于 `targetSum`，此时 `currSum - targetSum = 0`。为了能接住这种情况，必须在 DFS 启动前，在哈希表里预置 `prefixSumCount.put(0L, 1);`。

## ⚙️ 核心解法：状态隔离与回溯 (Backtracking)
你最初的直觉“*把左子树的那个节点对应的哈希表元素去掉*”非常敏锐。在代码落地时，这被称为**恢复现场（状态回溯）**。
在 DFS 离开当前节点，向上返回上一层时，必须执行 `prefixSumCount.put(currSum, prefixSumCount.get(currSum) - 1);`。因为当前节点的前缀和不能去污染其他兄弟分支的计算逻辑，保证了路径必须是“严格向下”的。

## 🏭 工业界真实工程实例：微服务架构中的分布式链路追踪 (APM)
这道算法题的骨架，在真实的大厂后端架构中有极强的落地场景。
在微服务（如字节/阿里的高并发架构）中，一个请求会生成一棵庞大的**链路调用树 (Trace Tree)**。假如某段连续的微服务调用累积耗时触发了 `800ms` 的熔断告警（这里的 800ms 就是 `targetSum`）。
* **前缀和**：相当于计算从网关 (Root) 开始向下层层 RPC 调用的**累积耗时**。
* **哈希表 O(1) 嗅探**：通过 `currSum - targetSum`，在海量流式数据中瞬间精准定位出导致超时的源头微服务区间。
* **状态回溯（核心痛点）**：对应着高并发场景下的**上下文隔离 (Context Isolation)**。在多线程或异步调用中（类似于并发遍历多个子树），如果不及时清理 ThreadLocal 或缓存中的残存状态（即回溯 -1），就会导致严重的数据串讲和脏数据污染，引发监控误报甚至严重的生产事故。

## 最终 Java 代码实现
```java
import java.util.HashMap;

class Solution {
    public int pathSum(TreeNode root, int targetSum) {
        // Key: 前缀和 (必须用 Long 防溢出)
        // Value: 该前缀和在当前连续向下的路径中出现的次数
        HashMap<Long, Integer> prefixSumCount = new HashMap<>();
        
        // 🚨 极其关键的初始化（垫底值）
        // 涵盖从根节点直接出发就能满足 targetSum 的路径
        prefixSumCount.put(0L, 1);
        
        return dfs(root, prefixSumCount, targetSum, 0L);
    }

    private int dfs(TreeNode node, HashMap<Long, Integer> prefixSumCount, int targetSum, long currSum) {
        if (node == null) {
            return 0;
        }

        // 1. 更新当前路径的前缀和 (使用 long 防溢出)
        currSum += node.val;

        // 2. 核心逻辑：检查是否存在合法的截断路径
        int count = prefixSumCount.getOrDefault(currSum - targetSum, 0);

        // 3. 将当前前缀和加入哈希表，准备向左右子树传递
        prefixSumCount.put(currSum, prefixSumCount.getOrDefault(currSum, 0) + 1);

        // 4. 递归遍历左右子树
        count += dfs(node.left, prefixSumCount, targetSum, currSum);
        count += dfs(node.right, prefixSumCount, targetSum, currSum);

        // 5. 🚨 状态回溯（恢复现场）
        // 离开节点时必须从哈希表中扣除，绝不能污染兄弟分支
        prefixSumCount.put(currSum, prefixSumCount.get(currSum) - 1);

        return count;
    }
}
