# 算法复盘笔记：LeetCode 160. 相交链表 (Intersection of Two Linked Lists)

## 一、 题目剖析与大厂考点
* **考察频次**：⭐⭐⭐⭐⭐ (美团、快手、腾讯等核心热身题)
* **核心标签**：链表、双指针
* **时间/空间复杂度要求**：极致期望为 Time: $O(M+N)$, Space: $O(1)$。
* **题目痛点**：
  1. 链表长度不一致导致的“相遇”难题。
  2. 极易踩坑的 `NullPointerException` 边界处理。
  3. 认知误区：Java 中节点相交的本质是**内存地址相同 (`headA == headB`)**，而不是节点内部的 `val` 值相等。

## 二、 核心解法沉淀

### 方法 1：长度差对齐法 (候选人首次 AC 方案)
先分别遍历算出两条链表的长度 $M$ 和 $N$，让较长的链表先走 $|M - N|$ 步，然后齐头并进。
* **复杂度**：时间 $O(M+N)$，空间 $O(1)$。
* **工程优化点 (Early Return)**：齐头并进时，一旦发现 `headA == headB` 必须**立即返回**，绝不要用布尔变量 `flag` 拖泥带水地遍历到链表尾部，这在千万级节点的业务中是致命的性能浪费。

### 方法 2：双指针浪漫相遇法 (最优解，大厂炫技必备)

让指针 pA 和 pB 分别遍历走完自己的路后，去走对方的路。因为 $(L_A + C) + L_B = (L_B + C) + L_A$，所以它们必定会在相交点**绝对同时相遇**。
* **复杂度**：时间 $O(M+N)$，空间 $O(1)$。
* **防死循环机制**：如果两链表完全不相交（$C=0$），两个指针会在走完 $L_A + L_B$ 步后，同时指向 `null`，此时 `pA == pB` 成立，循环安全跳出。

**核心 Java 代码定稿**：
```java
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        
        ListNode pA = headA;
        ListNode pB = headB;
        
        // Java 引用比较：内存地址相同即为相交
        while (pA != pB) {
            pA = (pA == null) ? headB : pA.next;
            pB = (pB == null) ? headA : pB.next;
        }
        
        return pA;
    }
}

```

## 三、 工业界真实场景映射 (工程实例)

1. **Git 版本控制系统**：寻找两个分支的合并基准点（Merge Base）。将分支看作反向链表，寻找最近公共祖先本质上就是寻找链表相交点。
2. **区块链的分叉回滚**：当发生硬分叉时，根据高度差使用“长度差对齐法”快速定位两条链的分叉区块（相交点），以执行最长链共识。
3. **JVM 享元模式 (Flyweight) 分析**：在内存调优排查时，判断多个大对象引用链条是否在底层交汇于同一个共享对象资源（Y型引用池）。

---

*复盘日期：2026-03-05 | 导师：LeetCode 助手 (大厂面试官) | 战绩：1ms (99.21%) / 51.84MB (57.54%)*

