# 📝 LeetCode 105. 从前序与中序遍历序列构造二叉树 - 面试复盘笔记

## 1. 💡 思路推导与讨论过程

在本次辅导中，我们首先确定了本题的核心解法：**分治法 (Divide and Conquer)**。
整个推导过程可以抽象为“找老大”和“分地盘”的游戏：
1. **找老大（定根）**：前序遍历（`preorder`）的特长是把“根节点”放在最前面。因此，`preorder` 的第一个元素，绝对是当前整棵子树的 Root。
2. **分地盘（划界）**：中序遍历（`inorder`）的特长是“左-根-右”。当我们拿着刚才找到的 Root，去 `inorder` 里定位到它的位置时，它就像一刀切下去，把剩下的节点完美分成了**左子树**和**右子树**。
3. **算规模**：一旦在 `inorder` 里切分出左右子树，就能立刻算出左子树有几个节点（`leftSize`）。拿着这个数量，回到 `preorder` 里，将前序序列也精准切分成左右两块，进而向下递归。

## 2. ⚠️ 易错点与极致性能调优

* **性能陷阱 1：$O(N^2)$ 的线性查找**
  如果每次都在递归中用 `for` 循环去 `inorder` 数组找根节点，在树退化成链表的极端情况下，时间复杂度会劣化为 $O(N^2)$。
  **✅ 破局点**：利用题目“节点值无重复”的特性，提前在主函数中遍历一次 `inorder`，将 `[节点值 -> 数组索引]` 存入 `HashMap<Integer, Integer>` 中，使得后续极其频繁的查询降阶为 $O(1)$ 的超快查找。此时整体**时间复杂度优化为 $O(N)$**。
* **性能陷阱 2：频繁的对象创建与 GC**
  如果每次递归都用 `Arrays.copyOfRange()` 切割生成新数组，会产生极大的内存开销和频繁的垃圾回收（GC），这是大厂工程规范绝对不允许的。
  **✅ 破局点**：采用**双指针（边界下标）**原地圈定当前子树在原数组中的有效范围（如 `preStart`, `preEnd`, `inStart`, `inEnd`）。
* **工程小细节**：
  在初始化 HashMap 时，根据已知的数据长度预设容量 `new HashMap<>(inorder.length)`，可以避免哈希表底层的动态扩容（Rehash）损耗。

## 3. 💻 最终定稿 Java 代码

* **执行用时**：2 ms (击败 99.10%)
* **消耗内存**：45.56 MB (击败 69.02%)
* **时间复杂度**：$O(N)$
* **空间复杂度**：$O(N)$

```java
public class Solution {
    
    /**
     * 主函数：根据前序和中序遍历构建二叉树
     * 时间复杂度：O(N) - N 为节点数，构建哈希表 O(N)，递归构建整棵树 O(N)
     * 空间复杂度：O(N) - 哈希表空间 O(N)，递归调用栈最坏 O(N)
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // 预设容量避免频繁扩容，大厂标准写法
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
        
        // Base Case：指针交错说明当前区间为空，越界保护
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

```

## 4. 🏭 大厂真实工程实例 (Engineering Cases)

这道题在真实高并发系统和底层中间件中，解决的是**“层级结构数据的扁平化传输与内存重建”**的核心痛点：

1. **🌐 分布式 RPC 调用与定制化序列化 (Serialization/Deserialization)**
在微服务架构下（如 Dubbo/gRPC），庞大的业务对象树（如商品类目树、评论盖楼树）无法直接通过网络传输。发送端会用前序/后序遍历将内存指针“拍扁”成一维的连续二进制数组（序列化）；接收端则需要用极高的性能（即本题的 $O(N)$ 哈希重构算法）瞬间把数组还原回内存中的对象树（反序列化）。
2. **🗄️ 分布式配置中心与注册中心 (ZooKeeper / Nacos)**
配置中心的节点在逻辑上是树状目录。为了保证极高的写入速度，底层的日志（WAL - Write Ahead Log）通常是 Append-Only 的扁平追加结构。当机房宕机重启或主备切换时，新的主节点需要根据快照和这一维的线性日志，极速在内存里重建出整棵微服务目录树。
3. **⚙️ SQL 解析引擎与大数据计算 (AST 抽象语法树构建)**
在大数据计算（Flink/Spark）中，用户输入的 SQL 字符串会被解析器转化成“抽象语法树 (AST)”。为了在不同计算节点（TaskManagers）间分发执行计划，引擎常将其转化为逆波兰表达式（后序遍历序列）。计算节点收到后，再次运用本题的思想，根据序列瞬间重构出算子执行树。

