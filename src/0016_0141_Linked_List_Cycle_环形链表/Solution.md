# 算法复盘笔记：LeetCode 141. 环形链表 (Linked List Cycle)

## 一、 题目剖析与大厂考点
* **考察频次**：⭐⭐⭐⭐⭐ (字节、阿里、腾讯、美团等所有大厂必考基础题)
* **核心标签**：链表、双指针、哈希表
* **时间/空间复杂度要求**：基础期望 Time: $O(N)$, Space: $O(N)$；面试官终极期望（进阶）为 Time: $O(N)$, Space: $O(1)$。
* **题目痛点**：
  1. 极其容易触发 `NullPointerException`。在步长为 2 的快指针移动前，必须严格校验 `fast != null` 且 `fast.next != null`。
  2. 环的长度未知，甚至环可能只在尾部很小的一段，需要深刻理解“快慢指针必定在环内相遇（套圈）”的数学本质。

## 二、 核心解法沉淀

### 方法 1：哈希表法 (空间换时间，业务防雷常见思路)
遍历链表，将每个节点存入 `HashSet`。如果 `set.add(node)` 返回 `false`（或 `set.contains(node)` 为 `true`），说明该节点被访问过两次，必定存在环。
* **复杂度**：时间 $O(N)$，空间 $O(N)$。

### 方法 2：龟兔赛跑法 / 快慢双指针 (最优解，大厂炫技必备)

定义两个指针，`slow` 每次走 1 步，`fast` 每次走 2 步。如果链表没有环，`fast` 会率先到达 `null`；如果有环，`fast` 必定会在环内“套圈”追上 `slow`。
* **复杂度**：时间 $O(N)$，空间 $O(1)$。
* **数学本质**：每次移动，`fast` 和 `slow` 的距离缩小 1，因此只要有环，无论环多大，快指针绝对不会“跳过”慢指针。

**核心 Java 代码定稿 (0ms 击败 100%)**：
```java
public class Solution {
    public boolean hasCycle(ListNode head) {
        // 剪枝：空链表或只有一个节点且无环的情况
        if (head == null || head.next == null) {
            return false;
        }
        
        ListNode slow = head;
        ListNode fast = head.next; // fast 先走一步，方便直接进入 while 循环的条件判断
        
        while (slow != fast) {
            // fast 到达了链表尾部，说明没有环
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;         // 慢指针走 1 步
            fast = fast.next.next;    // 快指针走 2 步
        }
        
        // slow 和 fast 相等，在环内相遇
        return true;
    }
}

```

## 三、 工业界真实场景映射 (工程实例)

1. **Spring 框架的循环依赖检测**：在 Bean 初始化时，Spring 底层使用类似“哈希表”的机制记录正在创建的 Bean，一旦发现依赖形成闭环，立即抛出异常阻断，防止栈溢出 (`StackOverflowError`)。
2. **分布式数据库死锁检测 (Wait-For Graph)**：MySQL/TiDB 等数据库中，如果事务之间的等待关系（锁等待）有向图闭合成了“环”，系统就会判定发生了死锁，并主动回滚其中一个事务以打破僵局。
3. **密码学伪随机数周期检测 (Pollard's rho 算法)**：利用 $O(1)$ 空间复杂度的快慢指针思想，在海量极大的状态空间中寻找哈希碰撞和周期律，避免了庞大状态记录导致的 OOM。

---

*复盘日期：2026-03-05 | 导师：LeetCode 助手 (大厂面试官) | 战绩：0ms (100.00%) / 45.93MB (66.06%)*
