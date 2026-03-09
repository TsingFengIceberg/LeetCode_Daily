# 算法复盘笔记：LeetCode 148. 排序链表 (Sort List)


---

## 💡 核心解题思路引导 (原声回放)
为了手撕这道究极缝合怪（要求 $O(N \log N)$ 时间复杂度），我们必须使用**归并排序 (Merge Sort)**。以下是破局的三个核心灵魂拷问：

1. **零件一：如何优雅地把链表“从中间劈开”？**
   * *原话引导*：“归并排序的核心是‘分治’。我们要把一条长链表从正中间切成两条短链表，直到切成只剩一个节点。我的提问：在单链表中，我们找中点最标准的套路是什么？（提示：有两个速度不同的指针）。另外，当你找到中点后，必须要做一个什么极其关键的断开操作，才能保证原来的长链表真正变成了两条独立的短链表？”
2. **零件二：如何合并两个“已经排好序”的链表？**
   * *原话引导*：“假设经过无数次切割和最底层的递归，现在你手里有两条已经按升序排好的短链表 `list1` 和 `list2`，现在要把它俩像拉链一样咬合在一起。我的提问：为了避免处理复杂的头节点为空的边界情况，我们在组装新链表时，通常会先 new 一个什么极其好用的‘虚拟节点’来充当车头？”
3. **复杂度预判（强制老规矩）**
   * *原话引导*：“假设你顺着‘快慢指针找中点’ -> ‘递归左右两半’ -> ‘合并两个有序链表’的自顶向下 (Top-Down) 归并思路写出了代码。时间复杂度：是多少？空间复杂度：是多少？（注意，虽然我们没有额外 `new` 数组，但递归会消耗系统调用栈。想想归并排序的递归深度是多少树高？）”

---

## 🚨 真实踩坑与大厂 Code Review 修正

### ❌ Bug 1：快慢指针找中点导致的“栈溢出 (StackOverflow)”死循环
* **我的原错误写法**：`ListNode fast = head; ListNode slow = head;`
* **致命漏洞**：当链表只有 2 个节点 `[4, 2]` 时，循环结束后 `slow` 会走到节点 `2`。执行断开操作 `slow.next = null` 实际上切断的是 `2` 后面的节点，导致原链表 `head` 依然是 `[4, 2]`。后续递归 `sortList(head)` 陷入无限死循环，撑爆调用栈！
* **满分修正**：让 `fast` 提前走一步：**`ListNode fast = head.next;`** 确保偶数长度时，`slow` 停留在左半边的尾部，从而完美将 `[4, 2]` 劈成 `[4]` 和 `[2]`。

### ⚠️ 异味 2：毫无必要的提前预判与别扭的合并逻辑
* **我的原错误写法**：手动判断 `resultHead`，并用带有大量 `if (p != null)` 冗余判断的方式去拼接链表。
* **满分修正**：引入**虚拟头节点 (Dummy Node)**。`ListNode dummy = new ListNode(0);` 作为万能脚手架，一个游标 `curr` 往后连，最后无脑 `return dummy.next;`，消灭一切边界判断。



---

## 💻 工业级代码 (Review Approved 版)

```java
public class Solution {
    public ListNode sortList(ListNode head) {
        // Base Case：空链表或只有 1 个节点的链表，天然有序
        if (head == null || head.next == null) {
            return head;
        }

        // 1. 找中点并断开（大厂避坑：fast 提前走一步防止死循环）
        ListNode fast = head.next;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // mid 是右半边的头，物理斩断链表
        ListNode mid = slow.next;
        slow.next = null;

        // 2. 递归分治：左右两半各自去排序
        ListNode left = sortList(head);
        ListNode right = sortList(mid);

        // 3. 合并有序链表（使用 Dummy Node 优雅拼接）
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        while (left != null && right != null) {
            if (left.val < right.val) {
                curr.next = left;
                left = left.next;
            } else {
                curr.next = right;
                right = right.next;
            }
            curr = curr.next;
        }
        
        // 接上剩余的尾巴
        curr.next = (left != null) ? left : right;

        return dummy.next;
    }
}

```

### 📊 复杂度分析

* **时间复杂度**: $O(N \log N)$。经典的归并排序时间复杂度，每次二分耗时 $O(\log N)$，每层的合并操作耗时 $O(N)$。
* **空间复杂度**: $O(\log N)$。没有开辟额外的数据结构，但递归调用会消耗系统栈，最大深度为二叉树的高度 $\log N$。

---

## 🏗️ 真实大厂工程实例深度剖析

1. **Linux 内核与 RTOS 的空闲内存排序 (Free List)**：在严苛的操作系统内核底层管理内存碎片时，绝对不允许随意 `new` 数组进行排序。利用链表的归并排序，**只修改指针指向，实现绝对的零拷贝 (Zero-Copy) 和 $O(1)$ 额外空间原地排序**。
2. **大数据与数据库的外部排序 (External Sort)**：当有 100GB 的数据需要排序，而内存只有 4GB 时。底层会将数据分块读入内存排序后写回磁盘。此时磁盘文件就是无数个“有序的单向链表”。数据库引擎调用的正是代码里的 `merge` 逻辑，在内存中仅维护指针进行拉链式合并，是解决“内存装不下”海量数据的基石。
