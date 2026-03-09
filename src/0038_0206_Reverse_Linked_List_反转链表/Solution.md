# 算法复盘笔记：LeetCode 206. 反转链表 (Reverse Linked List)


---

## 💡 核心思路：滑动窗口与指针变道
反转链表最怕的就是“改了当前节点的指向后，找不到下一个节点在哪了（链表断裂）”。
这套算法的本质是一个在链表上不断向右滑动的**状态机**：
1. **暂存（保命）**：在改变当前节点的指向前，必须先有个变量记住它，或者让遍历指针先跳到下一个安全节点上去（你的代码选择了 `head = head.next` 提前跑路）。
2. **反转（变道）**：把当前节点 `temp` 的 `next` 指向我们构建的新车头 `resultHead`。
3. **平移（推进）**：新车头 `resultHead` 前移到 `temp` 的位置，准备迎接下一个节点。



---

## 💻 工业级代码与详尽注释 (0ms 极速版)

```java
class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class Solution {
    public ListNode reverseList(ListNode head) {
        // 最终反转后的新链表头，初始为 null（也是原链表尾部的最终归宿）
        ListNode resultHead = null; 
        
        // 游标节点，用于执行具体的反转操作
        ListNode temp = null; 
        
        while(head != null) {
            // 1. 锁定当前需要被反转的节点
            temp = head;
            
            // 2. 提前让 head 跑到下一个节点去避险，防止链表断裂丢失后续数据
            head = head.next;
            
            // 3. 执行反转：把当前节点的 next 指向前面的 resultHead
            temp.next = resultHead;
            
            // 4. 整体平移：resultHead 来到当前节点的位置，成为新的“车头”
            resultHead = temp;
        }
        
        // 循环结束时，原链表已被全部扒光，resultHead 就是反转后的新车头
        return resultHead;
    }
}

```

### 📊 复杂度分析

* **时间复杂度**: $O(N)$。只需遍历链表一次，每个节点的操作都是常数时间 $O(1)$ 的指针赋值。
* **空间复杂度**: $O(1)$。只申请了 `resultHead` 和 `temp` 两个指针变量，没有任何随数据规模增长的额外空间开销，极致的原地 (In-place) 操作。

---

## 🏗️ 真实大厂工程实例深度剖析

1. **Nginx / Netty 高并发异步日志 (无锁队列优化)**：在高并发网关中，几万个工作线程为了不阻塞，会通过无锁的 `CAS` 操作以“头插法”将日志疯狂塞入内存链表。但这导致日志的时间变成了 LIFO（后进先出）。异步刷盘的后台线程在摘下这段链表后，会利用极速的 $O(N)$ 反转链表算法，将其瞬间变为 FIFO（先进先出），从而保证日志写入磁盘的时间顺序绝对正确。
2. **Git 底层版本树与区块链追溯**：Git 的每一个 Commit 都只保存指向 Parent 的单向指针（从现在指向过去）。当我们在终端执行 `git log --reverse` 要求正向查看项目演进历史时，Git 引擎会在内存中顺着指针拿到一条倒序链表，并对其执行“反转链表”操作，从而将时间线拨正渲染给开发者。
