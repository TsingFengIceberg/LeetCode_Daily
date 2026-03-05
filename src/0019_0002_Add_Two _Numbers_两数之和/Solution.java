class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // 【工程思维】使用布尔值来记录进位（Carry）。
        // 两个 0~9 的数字相加，最大为 9 + 9 + 1 = 19，进位必然是 0 或 1，用 boolean 完全合乎逻辑。
        boolean flag = false;
        
        // 【核心技巧】使用“伪头节点”（Dummy Node）来简化新链表头部的初始化逻辑
        // 这样我们就不需要特判 if (head == null) 了，极大地提升了代码的优雅度。
        ListNode head = new ListNode();
        
        // tail 游标指针，用于在合并过程中不断向后延伸（穿针引线）
        ListNode tail = head;
        
        // 【边界处理】使用 || 而不是 &&，只要还有一个链表没遍历完，就继续。
        // 这完美解决了两个链表长度不一致的问题（例如 [9,9] 和 [1]）。
        while (l1 != null || l2 != null) {
            ListNode node = new ListNode();
            
            // 【防御性编程】优雅的判空取值。如果链表已经走到尽头，就将其值视作 0，不影响加法结果。
            // 同时把上一轮的进位（flag ? 1 : 0）加进来。
            int sum = (l1 != null ? l1.val : 0) + (l2 != null ? l2.val : 0) + (flag ? 1 : 0);
            
            // 处理进位逻辑
            if (sum >= 10) {
                sum -= 10;   // 剥离出个位数，作为当前节点的值
                flag = true; // 触发进位标志
            } else {
                flag = false;// 清除进位标志
            }
            
            // 将计算好的值赋给新节点，并挂在 tail 后面
            node.val = sum;
            tail.next = node;
            
            // 游标向后移动
            tail = node;
            
            // 安全地推进 l1 和 l2 指针，防止 NullPointerException
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        
        // 【极易漏掉的死角】最高位进位检查！
        // 当两个链表都遍历完毕时，如果最后一次加法产生了进位（比如 5 + 5 = 10），
        // 必须在链表最末尾额外追加一个值为 1 的节点。你完美防住了这个面试大坑！
        if (flag) {
            tail.next = new ListNode(1);
        }
        
        // 返回伪头节点的下一个节点，即真正的结果链表头部
        return head.next;
    }
}