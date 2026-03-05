class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
        next = null;
    }
 }

public class Solution1 {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode a = headA;
        ListNode b = headB;
        int m = 0, n = 0;
        while(a != null){
            m++;
            a = a.next;
        }
        while(b != null){
            n++;
            b = b.next;
        }
        a = headA;
        b = headB;
        if (m > n){
            for(int i = 0; i < m - n; i++){
                headA = headA.next;
            }
        } else {
            for(int i = 0; i < n - m; i++){
                headB = headB.next;
            }
        }
        // 齐头并进，遇到相同的直接返回即可！
        while (headA != null && headB != null) {
            // Java 中的 == 比较的是对象的内存地址，恰好符合“相交节点”的定义
            if (headA == headB) {
                return headA; // 🎯 找到了直接回家，绝不多走一步！
            }
            headA = headA.next;
            headB = headB.next;
        }
        return null; // 走到头都没碰到，说明不相交
    }
}
