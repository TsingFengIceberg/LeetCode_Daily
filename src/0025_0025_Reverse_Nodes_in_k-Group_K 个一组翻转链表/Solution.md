# LeetCode 25. K 个一组翻转链表 (Reverse Nodes in k-Group) - 深度复盘笔记

## 💡 核心算法思想：分段探路与局部翻转
本题是链表操作的终极考验，俗称“指针炼狱”。核心思路是将长链表按 $K$ 个一组进行截断，对截断后的局部子链表使用经典的三指针（`p, q, r`）原地翻转法，最后再将翻转后的局部链表重新拼接到主链表上。

* **时间复杂度**：`O(n)`。其中 n 为链表总节点数。探路指针 `subTail` 遍历了一次链表，内部的翻转操作对每个节点又遍历了一次，整体每个节点最多被访问两次，属于严格的线性时间复杂度。
* **空间复杂度**：`O(1)`。全程只复用了几个固定的局部对象引用（`left`, `right`, `p`, `q`, `r` 等），没有开辟任何新的堆内存空间，符合进阶要求。

---

## 🧠 面试官 Code Review 与工程化指导

### 1. 变量命名规范（Java 硬性要求）
* 你的原始代码中存在 `Left` 和 `Right` 首字母大写的情况。
* **大厂规范**：在 Java 中，首字母大写严格保留给**类名**（如 `ListNode`）或**接口名**。普通变量必须使用小驼峰命名法（`left`, `right`），这是 Code Review 时的第一道红线。

### 2. Dummy Node（虚拟头节点）的降维打击
* 你的原始代码引入了 `boolean first = true` 以及 `if-else` 来特殊处理第一组翻转后的新头节点。
* **大厂优雅写法**：遇到“头节点可能被修改”的链表题，**永远在原 `head` 前面接一个 `Dummy Node`**（如 `ListNode dummy = new ListNode(0, head);`）。这样，包括第一组在内的所有组前面都有了“前驱节点”，可以彻底消灭 `first` 变量和丑陋的分支判断，代码极其优雅。

---

## 🏭 工业界工程实例分析

不要以为这题是纯粹的“面试造火箭”，在底层架构中，这是极其硬核的基石技术：

1. **高并发无锁队列（Treiber Stack）的批量消费**
   * **场景**：阿里/字节中间件底层的多线程任务调度。生产者通过 CAS 无锁压栈（LIFO），但消费者需要按顺序（FIFO）执行。
   * **落地**：消费者批量 CAS 截取链表前 $K$ 个节点，在本地线程中利用本题的 `O(1)` 原地翻转算法纠正顺序，全程零加锁、零 GC 开销，瞬间打穿高并发瓶颈。
2. **数据库底层内存池（Memory Pool）管理**
   * **场景**：Redis 或 JVM 堆外内存的高性能分配（TLAB）。
   * **落地**：为了避免多线程激烈争抢全局空闲链表（Free List），工作线程会一次性截取 $K$ 个连续内存块形成的子链表，断开后继指针带回本地使用。有时甚至会在截取后重组指针顺序以提升 CPU Cache 命中率。

---

## 💻 最终定稿 Java 代码 (带详细工业级注释)

```java
class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode resultHead = head; 
        
        // left 用于将当前翻转完的组拼接到前面的链表上
        ListNode left = head; 
        // right 记录下一组的起始位置，防止断链
        ListNode right = head; 
        
        ListNode subHead = head; 
        ListNode subTail = head; 
        ListNode p, q, r = null; 
        
        int i = k; 
        boolean first = true; 

        // 探路指针前进
        while (subTail != null) {
            
            // 刚好凑齐 K 个节点
            if (i == 1) {
                // 1. 保护现场：记住下一组的开头
                right = subTail.next; 
                // 2. 断开当前组，形成独立子链表
                subTail.next = null; 
                
                // 3. 准备翻转：r 初始化为 right，翻转后新尾巴自然连上后续链表
                p = subHead; 
                r = right; 
                
                // 4. 标准单链表翻转模板
                while (p != null) {
                    q = p.next; 
                    p.next = r; 
                    r = p;      
                    p = q;      
                }
                
                // 5. 组装拼链
                if (!first) {
                    left.next = subTail; 
                    left = subHead; 
                } else {
                    resultHead = subTail; 
                    first = false; 
                }
                
                // 6. 状态重置，进入下一轮
                i = k; 
                subHead = right; 
                subTail = right; 
            }
            
            if (subTail != null) {
                subTail = subTail.next;
                i--;
            }
        }
        
        return resultHead; 
    }
}