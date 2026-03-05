class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 }


public class Solution2 {
    /**
     * 方法二：递归法 (Recursion)
     * 时间复杂度：$O(N + M)$
     * 空间复杂度：$O(N + M)$ -> 隐式的系统调用栈空间开销
     * 评语：极其优雅、极具美感。如果链表长度可控，这绝对是装杯神技。
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // 1. 终止条件 (Base Case)：任何一方为空，直接返回另一方即可
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        }
        
        // 2. 核心逻辑：比较头节点的大小
        if (l1.val <= l2.val) {
            // 如果 l1 更小，那么 l1 就是当前的老大。
            // l1 后面的小弟（l1.next）是谁呢？
            // 是 "l1 剩下的小弟" 和 "l2 整个队伍" 合并后的结果。
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            // 如果 l2 更小，同理，l2 是老大。
            // l2 的下一个节点，由递归交办给底下的人去合并。
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }
}