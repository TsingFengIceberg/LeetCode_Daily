# 算法复盘笔记：LeetCode 234. 回文链表 (Palindrome Linked List)



---

## 🧠 破题思路与顿悟实录 (一字不落的思维轨迹)

### 1. 敏锐的直觉破局
* **我的原话**：“这题不看提示，我知道先用双指针法变成两段，然后一段用链表倒序发倒序后，两个就可以比较了”
* **面试官解析**：一语道破天机！要想达成大厂极其严苛的 $O(1)$ 空间复杂度（绝对不允许 `new` 数组或使用递归栈），唯一的出路就是**“原地动刀子”**。这完美融合了之前积累的两大核心武器：
  1. **快慢指针找中点**（复刻 LeetCode 148 的防死循环神技）。
  2. **原地反转链表**（复刻 LeetCode 206 的三指针流转）。



### 2. 极其敏锐的奇偶边界把控
在编写代码时，利用 `if (fast != null)` 进行了一次极其惊艳的分类讨论：
* **如果是偶数（`fast != null`）**：前后两半长度完全一致，全量遍历比对。
* **如果是奇数（`fast == null`）**：慢指针 `slow` 刚好停在正中间的“奇点”。回文的中心天然对称，因此比对时前半段的游标只要走到 `slow` 之前就可以安全停下！

---

## 💻 工业级代码与详尽注释 (S 级满分缝合版)

```java
public class Solution {
    public boolean isPalindrome(ListNode head) {
        // Base case: 空链表或只有一个节点，天然是回文
        if (head == null || head.next == null) {
            return true;
        }
        
        // --- 第一步：快慢指针找中点（复用防死循环神技） ---
        ListNode slow = head;
        ListNode fast = head.next; // fast 提前走一步，确保偶数时 slow 停在左半边末尾
        ListNode firstHead = null, secondHead = null;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 物理斩断链表，分成 firstHead (前半段) 和 secondHead (后半段)
        firstHead = head;
        secondHead = slow.next;
        slow.next = null; 
        
        // --- 第二步：反转后半段链表（0ms 极速反转逻辑） ---
        // p 为 curr, q 为 next, r 为 prev(新车头)
        ListNode p = secondHead, q = null, r = null;
        while (p != null) {
            q = p.next;  // 暂存下一个节点
            p.next = r;  // 掉头：指向新车头
            r = p;       // 新车头前移
            p = q;       // 游标继续往后走
        }
        secondHead = r; // r 就是反转后的后半段真正的头节点
        
        // --- 第三步：双指针比对前半段和反转后的后半段 ---
        ListNode m = firstHead, n = secondHead;
        
        // 奇偶数长度极其严谨的分类讨论：
        if (fast != null) { 
            // 偶数长度：前后两段长度完全一样
            while (m != null && n != null) {
                if (m.val != n.val) {
                    return false;
                }
                m = m.next;
                n = n.next;
            }
        } else {
            // 奇数长度：slow 刚好停在正中间的对称中心
            // 前半段比对到 slow 之前即可停下
            while (m != slow && n != null) {
                if (m.val != n.val) {
                    return false;
                }
                m = m.next;
                n = n.next;
            }
        }
        
        // 🌟 大厂架构师加分项：在 return true 之前，最好再调用一次反转链表，把后半段恢复原状，避免副作用！
        return true;
    }
}

```

### 📊 复杂度分析

* **时间复杂度**: $O(n)$。找中点遍历了 $n/2$ 次，反转后半段遍历了 $n/2$ 次，最后的比对遍历了 $n/2$ 次。整体依然是线性的 $O(n)$，且常数极小，因此能跑到 4ms 的极速。
* **空间复杂度**: $O(1)$。全程只使用了几个指针变量（`slow`, `fast`, `p`, `q`, `r` 等），没有开辟任何额外的数据结构（如栈或数组），完美达成进阶要求！

---

## 🏗️ 真实大厂工程实例深度剖析

1. **IoT 嵌入式设备/RTOS 的数据流校验**：在只有几百 KB 内存的微型医疗设备中，传感器产生的时序波形数据通过链表存储。为了检测波形是否触发了对称共振异常，系统必须进行回文校验。绝对禁止使用大块数组内存，底层只能依靠这段 $O(1)$ 空间的原地指针反转操作来完成比对。
2. **生物信息学 (DNA 限制性酶切位点识别)**：在基因测序中，底层的分析引擎需要在几十亿个碱基对（链式结构）中寻找类似于 `GAATTC` 的“回文序列”（限制性内切酶的靶点）。由于基因链极其庞大，为了避免内存泄漏和溢出，算法同样大量采用原地切分比对的思维骨架。
