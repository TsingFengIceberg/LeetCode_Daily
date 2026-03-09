# 算法复盘笔记：LeetCode 94. 二叉树的中序遍历 (Binary Tree Inorder Traversal)

## 💡 核心基础对齐
**中序遍历 (Inorder Traversal)** 的访问顺序严格遵循：**左子树 -> 根节点 -> 右子树**。

---

## ⚔️ 三重境界解法全解析

### 境界一：递归法 (基础送分，大厂不收)
依赖 JVM 底层的方法调用栈（System Call Stack）来记录回退路径。代码极简，但容易在超深树结构中引发 `StackOverflowError`。
* **时间复杂度**: $O(N)$
* **空间复杂度**: $O(H)$ (H 为树的高度)

### 境界二：迭代法 (大厂标准要求，手动维护栈)
通过显式声明数据结构来模拟系统压栈过程：**一股脑儿往左走到底，把路上的节点全压入栈；走不动了，就弹出一个节点访问，然后转向它的右子树，继续重复。**



```java
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        // Java 工程规范：推荐使用 Deque 接口来实现栈，而不是带全局锁、性能差的 Stack 类
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode curr = root;
        
        while (curr != null || !stack.isEmpty()) {
            // 1. 疯狂向左走，把路上的左孩子全部压栈
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            // 2. 左边走到头了，从栈顶弹出一个节点
            curr = stack.pop();
            // 3. 访问该节点（加入结果集）
            res.add(curr.val);
            // 4. 转向该节点的右子树，准备下一轮的压栈
            curr = curr.right;
        }
        return res;
    }
}

```

* **时间复杂度**: $O(N)$
* **空间复杂度**: $O(H)$

---

### 境界三：Morris 遍历 (神级操作，彻底消除栈空间)

**核心思想**：完全抛弃传统的栈，巧妙利用树中大量空闲的叶子节点右指针（将其作为线索指向父节点），硬生生地把空间复杂度降到了绝对的 $O(1)$。

#### 🎨 图解原理：建立线索与拆除线索的魔法

假设二叉树如下：

```text
      (4)
      / \
    (2) (5)
    / \
  (1) (3)

```

**第一阶段：疯狂向下探，建立“回家”的线索**

1. **当前 root = 4**：找 4 在左子树里的前驱节点，找到 **3**。`3.right` 为空，**建立线索**：让 `3.right = 4`。root 向左移动到 2。
2. **当前 root = 2**：找 2 的前驱，找到 **1**。`1.right` 为空，**建立线索**：让 `1.right = 2`。root 向左移动到 1。
*(此时树结构已被偷偷改造，带箭头的表示线索：)*

```text
      (4) <-------+
      / \         |
    (2)<-+      (5)
    / \  |        
  (1)-+ (3)-------+

```

**第二阶段：触底反弹，收割结果并销毁证据**
3. **当前 root = 1**：左到底了，**输出 [1]**。向右走，顺着线索瞬间回到 2。
4. **当前 root = 2 (第二次来)**：找 2 的前驱还是 1。发现 `1.right == 2`！说明左子树全逛完了。**输出 [1, 2]**，**销毁证据**（`1.right = null`恢复原貌）。向右走到 3。
5. **当前 root = 3**：左到底了，**输出 [1, 2, 3]**。向右走，顺着线索瞬间回到 4。
6. **当前 root = 4 (第二次来)**：找 4 的前驱是 3。发现 `3.right == 4`。**输出 [1, 2, 3, 4]**，**销毁证据**（`3.right = null`）。向右走到 5。
7. **当前 root = 5**：左到底了，**输出 [1, 2, 3, 4, 5]**。遍历结束。

#### 💻 Morris 遍历工业级带注释代码

```java
class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        
        while (root != null) {
            // 【情况 1：左子树为空】
            if (root.left == null) {
                ans.add(root.val);
                // 访问完自己后，向右子树进发（若是线索，会神奇回到上层）
                root = root.right;
            } 
            // 【情况 2：左子树不为空】
            else {
                // 寻找当前节点的中序前驱节点（左子树最右下角）
                TreeNode prev = root.left;
                while (prev.right != null && prev.right != root) {
                    prev = prev.right;
                }
                
                // 【状态 2.1：第一次来到该节点，建立线索】
                if (prev.right == null) {
                    prev.right = root; // 牵一根线回家
                    root = root.left;  // 安心深入左子树
                } 
                // 【状态 2.2：第二次来到该节点，拆除线索】
                else {
                    ans.add(root.val); // 左子树全遍历完，轮到自己
                    prev.right = null; // 【大厂工程操守】用完即焚，恢复树原貌
                    root = root.right; // 迈向右子树
                }
            }
        }
        return ans;
    }
}

```

* **时间复杂度**: $O(N)$（整棵树每条边最多走两次，均摊依然是线性）。
* **空间复杂度**: $O(1)$（极致压榨机器性能）。

---

## 🚀 大厂实战变种 (Follow-up)

* **业务场景**：中序遍历最大的用武之地在 **二叉搜索树 (BST)**。对 BST 进行中序遍历，得到的一定是**严格递增的有序序列**。
* **底层工程**：数据库的索引（B+ 树思想）、寻找排行榜上的“上一名/下一名”，底层逻辑皆源自中序遍历。

