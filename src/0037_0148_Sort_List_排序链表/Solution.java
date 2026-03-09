class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}


public class Solution {
    public ListNode sortList(ListNode head) {
        // Base Case：空链表或只有 1 个节点的链表，直接返回（天然有序）
        if (head == null || head.next == null) {
            return head;
        }

        // 1. 找中点并断开 (修复栈溢出 Bug)
        // 让 fast 提前一步出发，确保在偶数节点时，slow 停在靠左的中点处
        ListNode fast = head.next;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // mid 是右半边链表的头节点
        ListNode mid = slow.next;
        // 物理斩断链表，分成 left 和 right 两半
        slow.next = null;

        // 2. 分治：向下递归排序左右两半
        ListNode left = sortList(head);
        ListNode right = sortList(mid);

        // 3. 合并：把两个排好序的短链表合并成一个长链表 (修复冗余代码)
        // 使用标准的虚拟头节点套路
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
            // 游标向后移动
            curr = curr.next;
        }

        // 把剩下的尾巴直接接上
        curr.next = (left != null) ? left : right;

        // dummy 的下一个节点就是合并后的真实头节点
        return dummy.next;
    }
}