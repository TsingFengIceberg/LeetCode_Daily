class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}



public class Solution {
    public boolean isPalindrome(ListNode head) {
        // Base case: 空链表或只有一个节点，天然是回文
        if (head == null || head.next == null) {
            return true;
        }
        
        // --- 第一步：快慢指针找中点（复用了你在“排序链表”里悟出的防死循环神技） ---
        ListNode slow = head;
        ListNode fast = head.next; // 极其关键：fast 提前走一步，确保偶数时 slow 停在左半边末尾
        ListNode firstHead = null, secondHead = null;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 物理斩断链表：firstHead 是前半段，secondHead 是要被反转的后半段
        firstHead = head;
        secondHead = slow.next;
        slow.next = null; // 斩断旧缘！
        
        // --- 第二步：反转后半段链表（完美重现“206.反转链表”的 0ms 逻辑） ---
        // p 相当于 curr(当前节点), q 相当于 next(暂存节点), r 相当于 prev(新车头)
        ListNode p = secondHead, q = null, r = null;
        while (p != null) {
            q = p.next;  // 暂存下一个节点，防止链表断裂
            p.next = r;  // 掉头：指向新车头
            r = p;       // 新车头前移
            p = q;       // 游标继续往后走
        }
        secondHead = r; // r 就是反转后的后半段真正的头节点
        
        // --- 第三步：双指针比对前半段和反转后的后半段 ---
        ListNode m = firstHead, n = secondHead;
        
        // 极其严谨的奇偶数长度分类讨论：
        // 如果 fast != null，说明原链表长度是【偶数】（例如 1->2->2->1）
        // 此时 fast 停在倒数第一个节点，前半段和后半段长度完全一样
        if (fast != null) {
            while (m != null && n != null) {
                if (m.val != n.val) {
                    return false; // 一旦有不对称的，直接判死刑
                }
                m = m.next;
                n = n.next;
            }
        }
        // 如果 fast == null，说明原链表长度是【奇数】（例如 1->2->3->2->1）
        // 此时 fast 直接冲出了边界为 null。
        // 奇数情况下，slow 刚好停在正中间的那个节点（节点 3 的位置）。
        // 回文的正中间节点天然对称，不需要比对，所以 m 只要走到 slow 之前就可以停下了！
        else {
            while (m != slow && n != null) {
                if (m.val != n.val) {
                    return false;
                }
                m = m.next;
                n = n.next;
            }
        }
        
        // 熬过所有关卡，确认是回文链表！
        return true;
    }
}