class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class Solution {
    public ListNode swapPairs(ListNode head) {
        // 边界防御：空链表或只有一个节点，直接原样返回，不需要交换
        if (head == null || head.next == null) {
            return head;
        }
        
        // 初始化四指针微操阵列
        // 假设当前链表是： 1(p) -> 2(q) -> 3(r) -> 4 -> null
        ListNode p = head;       // p: 当前要交换的【节点 1】
        ListNode q = head.next;  // q: 当前要交换的【节点 2】
        ListNode r = q.next;     // r: 下一轮的起点（保存现场，防止 3 被垃圾回收或丢失）
        ListNode s = null;       // s: 上一轮交换后的【尾节点】（用来把前后的交换对连接起来）
        
        boolean isFirst = true;  // 状态机：判断是否是链表的头部第一次交换
        
        // 提前锁定最终要返回的新头节点。
        // 因为两两交换后，原来的第 2 个节点(q)必然会变成整个链表的新头节点。
        ListNode newHead = q;
        
        // 只要当前还有两个节点可以配对，就继续循环
        while (p != null && q != null) {
            
            // 【核心微操 1：局部翻转】
            // 此时 p(1) 要挂到 r(3) 上，q(2) 要挂到 p(1) 上。
            // 局部变成了： 2 -> 1 -> 3
            p.next = r;
            q.next = p;
            
            // 【核心微操 2：跨组缝合】
            if (!isFirst) {
                // 如果这不是第一组，说明前面有一组已经交换完了（比如 0 -> -1）。
                // 此时 s 就是上一组交换后的最后一个节点，必须让 s 的 next 指向当前这组的新头部 q
                s.next = q;
            } else {
                // 如果是第一组，不需要拿 s 去跨接，关掉标志位即可
                isFirst = false;
            }
            
            // 终止条件：如果后面没有节点了，或者只剩 1 个落单的节点无法配对，立刻打断
            if (r == null || r.next == null) {
                break;
            }
            
            // 【核心微操 3：整体指针后移，准备下一轮】
            // 此时当前的配对已经变成了 q -> p -> r
            s = p;       // 这一轮交换后的尾巴是 p，把它交给 s，留给下一轮去“缝合”
            p = r;       // 下一轮的节点 1
            q = r.next;  // 下一轮的节点 2
            r = q.next;  // 下下一轮的起点（保存现场）
        }
        
        // 返回最初锁定的那个新头节点
        return newHead;
    }
}