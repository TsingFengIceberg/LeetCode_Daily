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

class Solution3 {
    public Node copyRandomList(Node head) {
        if (head == null) return null;
        
        // Map 充当外界记录空间：Key 是原节点，Value 是克隆出的新节点
        Map<Node, Node> map = new HashMap<>();
        
        // 第一遍：纯粹地打造全新节点，不管指针，先把“皮囊”造出来并放进字典里
        Node curr = head;
        while (curr != null) {
            map.put(curr, new Node(curr.val));
            curr = curr.next;
        }
        
        // 第二遍：有了字典，闭着眼睛连线
        curr = head;
        while (curr != null) {
            // 从字典里掏出当前老节点对应的新节点
            Node newNode = map.get(curr);
            
            // 新节点的 next，就等于原节点 next 对应的新节点
            newNode.next = map.get(curr.next);
            // 新节点的 random，就等于原节点 random 对应的新节点
            newNode.random = map.get(curr.random);
            
            curr = curr.next;
        }
        
        // 返回原头节点对应的新头节点
        return map.get(head);
    }
}