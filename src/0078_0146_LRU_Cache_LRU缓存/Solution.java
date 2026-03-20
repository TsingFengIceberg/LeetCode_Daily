import java.util.HashMap;
import java.util.Map;

// 内部类：双向链表节点
class DLinkedNode {
    int key;    // 核心考点：必须存 key，用于容量满时反向去哈希表中删除记录
    int value;
    DLinkedNode prev;
    DLinkedNode next;
    
    // 空参构造
    public DLinkedNode() {}
    
    // 带参构造
    public DLinkedNode(int _key, int _value) {
        key = _key;
        value = _value;
    }
}

class LRUCache {
    // 哈希表：用于实现 O(1) 的查询
    private Map<Integer, DLinkedNode> cache = new HashMap<>();
    // 当前缓存中的节点数量
    private int size;
    // 缓存的最大容量
    private int capacity;
    // 虚拟头尾节点（哨兵），用于无脑处理边界问题，避免 NullPointerException
    private DLinkedNode head, tail;

    public LRUCache(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        
        // 初始化虚拟头尾节点，并将它们连接起来
        head = new DLinkedNode();
        tail = new DLinkedNode();
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        if (cache.containsKey(key)) {
            DLinkedNode node = cache.get(key);
            // 命中缓存！必须将其从原位置拔出，并重新插入到头部，刷新其“最近使用”的状态
            remove(node);
            movetoHead(node);
            return node.value;
        } else {
            return -1;
        }
    }
    
    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            // 1. 如果 key 存在，更新 value
            DLinkedNode node = cache.get(key);
            node.value = value;
            // 2. 刷新到链表头部
            remove(node);
            movetoHead(node);
        } else {
            // 1. 如果 key 不存在，先判断容量是否已满
            if (size == capacity) {
                // 找出尾部节点的前一个节点（即真实的最久未使用节点）
                DLinkedNode last = tail.prev;
                // 核心：同步从哈希表中移除该 key
                cache.remove(last.key);
                // 从链表中彻底干掉该节点
                last.prev.next = tail;
                tail.prev = last.prev;
                size--;
            }
            // 2. 创建新节点，放入头部，并加入哈希表
            DLinkedNode node = new DLinkedNode(key, value);
            movetoHead(node);
            cache.put(key, node);
            size++;
        }
    }

    // 辅助 API：将一个节点从双向链表中彻底摘除
    private void remove(DLinkedNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // 辅助 API：将一个节点挂载到虚拟头节点之后（表示最新使用）
    private void movetoHead(DLinkedNode node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
}