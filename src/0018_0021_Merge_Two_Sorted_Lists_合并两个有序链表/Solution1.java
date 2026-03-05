class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 }


 public class Solution1 {
    /**
     * 方法一：迭代法 + 伪头节点 (Dummy Node)
     * 时间复杂度：$O(N + M)$，其中 N 和 M 是两个链表的长度。
     * 空间复杂度：$O(1)$，常数级额外空间，极致性能。
     * 评语：大厂业务代码中的绝对标准写法，鲁棒性极高，无惧栈溢出。
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // 1. 定义一个伪头节点，值随便给个 -1，它不参与最终结果，只负责“站岗”
        ListNode dummy = new ListNode(-1);
        
        // 2. 定义一个游标指针 curr，最初指向伪头节点
        ListNode curr = dummy;
        
        // 3. 当两个链表都还没遍历完时，谁小就牵谁的手
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1; // 把较小的 l1 挂到 curr 后面
                l1 = l1.next;   // l1 指针往前挪一步
            } else {
                curr.next = l2; // 把较小的 l2 挂到 curr 后面
                l2 = l2.next;   // l2 指针往前挪一步
            }
            // curr 游标也要往前挪一步，准备迎接下一个节点
            curr = curr.next;
        }
        
        // 4. 收尾拼接：循环结束时，必定至少有一个链表已经空了。
        // 直接把 curr.next 指向那个还没空的链表即可，O(1) 瞬间完成拼接！
        curr.next = (l1 != null) ? l1 : l2;
        
        // 5. 返回伪头节点的下一个节点，即真正的合并后的头节点
        return dummy.next;
    }
}