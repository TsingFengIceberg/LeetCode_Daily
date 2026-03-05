class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
        next = null;
    }
 }


public class Solution2 {
    /**
     * 方法二：双指针拼接法（最优解）
     * 时间复杂度：$O(M+N)$ 
     * 空间复杂度：$O(1)$
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        // 边界情况防御：如果有任意一个链表为空，绝对不可能相交
        if (headA == null || headB == null) {
            return null;
        }

        // 定义两个游标指针
        ListNode pA = headA;
        ListNode pB = headB;

        // 当 pA 和 pB 相等时跳出循环（要不就是在相交节点相遇，要不就是同时走到 null）
        while (pA != pB) {
            // pA 走完自己的路，就去走 B 的路
            pA = (pA == null) ? headB : pA.next;
            // pB 走完自己的路，就去走 A 的路
            pB = (pB == null) ? headA : pB.next;
        }

        // 此时 pA (或 pB) 要么是相交节点，要么是 null（说明不相交）
        return pA;
    }
}
