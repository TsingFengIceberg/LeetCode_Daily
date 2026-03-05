class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class Solution {
    /**
     * 最优解：双指针 (快慢指针) + 伪头节点
     * 时间复杂度：O(L) - 严格的一趟扫描
     * 空间复杂度：O(1)
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // 【核心神技 1】设置 Dummy Node，彻底解决如果我们要删除的是真正的“头节点”时引发的 NPE 问题
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        
        // 初始化快慢指针，都指向 Dummy Node
        ListNode slow = dummy;
        ListNode fast = dummy;
        
        // 【核心神技 2】制造一把长度为 N+1 的“游标卡尺”
        // 让 fast 先往前走 n + 1 步。
        // 为什么要多走 1 步？因为我们要让 slow 最终停在被删节点的“前驱节点”上，而不是被删节点本身。
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }
        
        // 步骤 2：尺子造好了，fast 和 slow 保持固定距离，同时往后平移
        // 当 fast 走到链表尽头 (null) 时，平移停止。
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        
        // 步骤 3：此时 slow 完美精准地停在了“倒数第 N 个节点的前一个节点”
        // 执行标准的单链表节点删除微操：A.next = A.next.next
        slow.next = slow.next.next;
        
        // 返回真正的头节点，深藏功与名
        return dummy.next;
    }
}