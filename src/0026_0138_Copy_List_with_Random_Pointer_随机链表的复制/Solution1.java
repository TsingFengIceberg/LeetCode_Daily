class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}

public class Solution1 {
    public Node copyRandomList(Node head) {
        // 边界条件防御：空链表直接返回
        if (head == null) {
            return null;
        }
        
        Node p = head; // p 用于遍历原链表
        Node q = head.next; // q 用于暂时保存下一个老节点
        Node r, s = null; // r 用于第三步探路，s 相当于新链表的 subTail
        Node newHead = null; // 记录最终新链表的头节点
        boolean isFirst = true; // 标记是否是新链表的第一个节点

        // ==========================================
        // Round 1: 节点交织克隆
        // 目标将 A -> B 变成 A -> A' -> B -> B'
        // ==========================================
        while (p != null) {
            q = p.next; // 保护现场，记住下一个老节点
            Node copy = new Node(p.val); // 打造全新克隆节点
            
            // 开始交织：老节点的 next 指向新节点，新节点的 next 指向下一个老节点
            p.next = copy; 
            copy.next = q; 
            
            p = q; // p 指针向前移动到下一个老节点，继续循环
        }

        // ==========================================
        // Round 2: 连接克隆节点的 random 指针
        // 核心公式：新节点的 random = 老节点的 random.next
        // ==========================================
        p = head;
        while (p != null) {
            q = p.next; // q 此时就是克隆出来的新节点（A'）
            
            // 注意防空指针：如果老节点的 random 为空，新节点默认就是空，不用连
            if (p.random != null) {
                // p.random 是老节点，p.random.next 就是老节点对应的新克隆节点！
                q.random = p.random.next; 
            }
            
            // 跨过新节点，直接跳到下一个老节点（A -> A' -> B 中的 B）
            p = q.next; 
        }

        // ==========================================
        // Round 3: 链表剥离（过河拆桥）
        // 目标：将交织链表恢复为原链表 A -> B，并提取出新链表 A' -> B'
        // ==========================================
        p = head;
        while (p != null) {
            q = p.next; // q 是新节点（A'）
            r = q.next; // r 是下一个老节点（B）
            
            // 组装新链表的 next 指针
            if (r != null) {
                q.next = r.next; // A'.next = B'
            } else {
                q.next = null; // 尾节点收尾
            }
            
            // 恢复老链表的 next 指针（A.next = B）
            p.next = r; 
            p = r; // p 移动到下一个老节点
            
            // 维护新链表的头节点和前后拼接（你的逻辑完全正确，虽然能用 Dummy Node 优化掉 isFirst）
            if (!isFirst) {
                s.next = q; 
            } else {
                newHead = q; 
                isFirst = false; 
            }
            s = q; // s 始终作为新链表的尾部指针往前推进
        }
        
        return newHead; // 完美收工
    }
}