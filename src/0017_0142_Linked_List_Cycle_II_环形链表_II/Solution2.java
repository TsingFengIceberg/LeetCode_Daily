import java.util.HashSet;
import java.util.Set;

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
     * 方法二：哈希表标记法 (空间换时间)
     * 时间复杂度：$O(N)$
     * 空间复杂度：$O(N)$ -> 开辟了额外的 Set 集合来存储节点引用
     * 评语：工程中最稳妥、最易读的做法。利用了 Java 集合的去重特性。
     */
    public ListNode detectCycle(ListNode head) {
        // 使用 HashSet 记录我们走过的每一个节点
        // 注意：这里存的是 ListNode 对象本身（基于内存地址），而不是存 val 的值
        Set<ListNode> visited = new HashSet<>();
        
        ListNode curr = head;
        
        // 顺着链表一直往下走
        while (curr != null) {
            // 如果这个节点已经在 HashSet 里了，说明我们绕了一圈又回来了！
            // 那么当前这个节点，就是我们第一次重新踏入的节点，即“入环点”。
            if (visited.contains(curr)) {
                return curr;
            }
            
            // 如果还没见过这个节点，就把它加入“已访问”名单
            visited.add(curr);
            
            // 继续往前走
            curr = curr.next;
        }
        
        // 如果顺顺利利走到了 null，说明是一条单行道，没有环
        return null;
    }
}