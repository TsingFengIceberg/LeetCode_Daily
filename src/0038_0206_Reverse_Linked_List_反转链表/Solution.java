class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode resultHead = null;
        ListNode temp = null;
        while(head != null) {
            temp = head;
            head = head.next;
            temp.next = resultHead;
            resultHead = temp;
        }
        return resultHead;
    }
}