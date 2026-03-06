import java.util.HashMap;
import java.util.Map;

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


class Solution {
    // 外部空间记录已经复制过的节点。
    // 极其重要：为了防止 A.random -> B, B.random -> A 这种环状结构导致死递归栈溢出！
    private Map<Node, Node> visitedHash = new HashMap<>();

    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        
        // 【递归出口 1】：如果这个节点已经被克隆过了，直接返回克隆好的新节点，停止顺藤摸瓜
        if (this.visitedHash.containsKey(head)) {
            return this.visitedHash.get(head);
        }

        // 打造一个全新节点
        Node newNode = new Node(head.val);
        
        // 【核心防死锁】：在进行向下递归之前，必须先把当前节点放进 HashMap 里！
        // 告诉后面的遍历者：“我虽然还没连好线，但我的人已经造出来了，不要重复造我了！”
        this.visitedHash.put(head, newNode);

        // 递归去克隆我的 next 节点
        newNode.next = this.copyRandomList(head.next);
        // 递归去克隆我的 random 节点
        newNode.random = this.copyRandomList(head.random);

        return newNode;
    }
}