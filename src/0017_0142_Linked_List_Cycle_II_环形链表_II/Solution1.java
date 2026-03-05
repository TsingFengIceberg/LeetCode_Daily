class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
        next = null;
    }
 }
 
public class Solution1 {
    /**
     * 方法一：快慢双指针法 (龟兔赛跑进阶版)
     * 时间复杂度：$O(N)$
     * 空间复杂度：$O(1)$ -> 顶级面试官最期望的解法
     */
    public ListNode detectCycle(ListNode head) {
        // slow 走一步，fast 走两步，寻找相遇点
        ListNode slow = head;
        ListNode fast = head;
        // entry 用于在找到相遇点后，从头节点出发寻找入环点
        ListNode entry = head;
        
        // 确保 fast 和 fast.next 不为空，防止 NullPointerException
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            // 步骤一：快慢指针在环内相遇了！
            if (slow == fast) {
                // 步骤二：数学魔法启动。
                // 根据公式 a = c + (n-1)L，此时让 entry 从 head 出发，slow 从相遇点出发。
                // 它们每次都只走 1 步，最终必定在“入环点”相遇。
                while (slow != entry) {
                    slow = slow.next;
                    entry = entry.next;
                }
                // 找到入环点，直接返回
                return entry;
            }
        }
        // 如果 fast 走到了尽头，说明没有环，返回 null
        return null;
    }
}