# 算法复盘笔记：LeetCode 24. 两两交换链表中的节点 (Swap Nodes in Pairs)

## 一、 题目剖析与大厂考点
* 考察频次：⭐⭐⭐⭐ (极其考察代码基本功的指针微操题)
* 核心标签：链表、指针模拟、递归
* 时间/空间复杂度要求：Time: O(N), Space: O(1) (迭代法)。
* 题目痛点与易错点：
  1. 断链危机：在交换 `A -> B` 变成 `B -> A` 时，如果没有提前保存 `C` 的引用，原链表就会从中断裂丢失。
  2. 跨组缝合：不仅要完成局部的两两交换，还要把上一组的尾巴准确地连接到当前这一组的新头部。
  3. 头部特判：如果不用伪头节点 (Dummy Node)，原本的第二个节点会变成新头节点，极易出现逻辑死结或空指针。

## 二、 核心解法沉淀

### 硬核解法：无 Dummy Node 的四指针状态机模拟
抛弃了常规的伪头节点取巧法，直接使用四个游标指针 (`p`, `q`, `r`, `s`) 进行真实的物理位置替换。
* 变量定义：
  * `p`: 当前配对的节点 1
  * `q`: 当前配对的节点 2
  * `r`: 下一轮的起点 (用于保存现场，防止断链)
  * `s`: 上一轮交换后的尾巴 (用于跨组缝合)
* 状态机：使用 `boolean isFirst` 巧妙避开了第一组没有“上一组尾巴”的边界问题，并提前锁定 `newHead = q` 作为最终返回值。
* 核心微操顺序：
  1. 局部反转：`p.next = r; q.next = p;`
  2. 跨接缝合：`s.next = q;` (非首个节点时)
  3. 游标推进：`s = p; p = r; q = r.next; r = q.next;`

核心 Java 代码定稿 (0ms 击败 100%)：
```java
public class Solution {
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) return head;
        
        ListNode p = head;
        ListNode q = head.next;
        ListNode r = q.next;
        ListNode s = null;
        boolean isFirst = true;
        ListNode newHead = q;
        
        while (p != null && q != null) {
            p.next = r;
            q.next = p;
            
            if (!isFirst) {
                s.next = q;
            } else {
                isFirst = false;
            }
            
            if (r == null || r.next == null) break;
            
            s = p;
            p = r;
            q = r.next;
            r = q.next;
        }
        return newHead;
    }
}

```

## 三、 工业界真实场景映射 (工程实例)

1. 网络协议栈的大小端转换 (Endianness Swapping)：在处理网络字节序时，底层的 `htons()` 宏在软件层面上就是对 16 位整数内部的相邻两个字节进行原位 (In-place) 两两交换。
2. 音频处理的立体声声道反转：在处理交错排列的 PCM 音频流（左, 右, 左, 右...）时，如果不申请额外庞大的数组内存，直接在链式缓冲流中进行两两相邻样本交换，实现极低延迟的声道互换。

---

*复盘日期：2026-03-05 | 导师：LeetCode 助手 (大厂面试官) | 战绩：0ms (100.00%) / 42.38MB (49.92%)*

