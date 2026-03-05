# 算法复盘笔记：LeetCode 19. 删除链表的倒数第 N 个结点 (Remove Nth Node From End of List)

## 一、 题目剖析与大厂考点
* **考察频次**：⭐⭐⭐⭐ (极其经典的快慢指针必考题)
* **核心标签**：链表、双指针 (快慢指针 / 延迟指针)
* **时间/空间复杂度要求**：面试官终极期望为 Time: O(L) (且必须是一趟扫描 One Pass), Space: O(1)。
* **题目痛点与易错点**：
  1. **长度未知**：单链表无法直接知道总长度，如何只遍历一次就找到倒数第 N 个节点？
  2. **删除头节点的极端边界**：如果链表长度为 N，要删除的就是第一个节点。此时如果没有前驱节点，极易引发 `NullPointerException` 或逻辑死结。

## 二、 核心解法沉淀

### 最优解：双指针“游标卡尺”法 + 伪头节点 (Dummy Node)
利用两个指针拉开固定的间距，构造一把长度为 N+1 的“尺子”在链表上滑动。



* **核心微操 1：Dummy Node 防御**
  创建一个 `Dummy Node` 指向 `head`，让 `slow` 和 `fast` 都从 `Dummy` 出发。这完美解决了“删除真正的头节点时没有前驱节点”的绝境。
* **核心微操 2：制造 N+1 的间距**
  我们要删除倒数第 N 个节点，就必须让 `slow` 指针停在它的**前驱节点**上（也就是倒数第 N+1 个位置）。
  因此，先让 `fast` 走 `N+1` 步。然后 `fast` 和 `slow` 同步前进，当 `fast` 撞到 `null` 越界时，`slow` 恰好稳稳停在目标的前驱节点上。
* **执行删除**：`slow.next = slow.next.next`。

**核心 Java 代码定稿 (0ms 击败 100%)**：
```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        
        ListNode slow = dummy;
        ListNode fast = dummy;
        
        // 制造长度为 N+1 的间距
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }
        
        // 尺子整体平移，直到 fast 越界
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        
        // 此时 slow 停在被删节点的前驱节点上
        slow.next = slow.next.next;
        
        return dummy.next;
    }
}

```

## 三、 工业界真实场景映射 (工程实例)

1. **区块链交易的“N-Block 确认” (最终性检验)**：流式读取最新产生的区块 (`fast` 指针)，同时在内存中维护一个落后 N 个区块的 `slow` 指针。当 `fast` 推进时，`slow` 指针所在的区块交易即被视为安全且最终确认，无需把整条区块链加载到内存。
2. **监控告警系统的“零开销上下文回溯”**：在海量日志流中，系统只在环形缓冲区内保留 `fast` 和 `slow` 之间固定 N 个长度的窗口。一旦撞见致命错误，瞬间截取这 N 条前置日志作为报错上下文发给开发者，极大地节省了内存和事后查日志的磁盘 I/O 开销。

---

*复盘日期：2026-03-05 | 导师：LeetCode 助手 (大厂面试官) | 战绩：0ms (100.00%) / 42.88MB (46.01%)*

