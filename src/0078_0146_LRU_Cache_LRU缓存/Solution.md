# [0146. LRU 缓存 (LRU Cache)]

## 💡 核心底层架构：哈希表与双向链表的缝合
在确定需要 `O(1)` 的查询和 `O(1)` 的插入/删除操作后，我们敲定了大厂标准的底层骨架：**HashMap + 双向链表**。
- **HashMap**：负责统筹寻址，通过 Key 能够以 `O(1)` 的时间复杂度直接定位到内存中的 Node 节点。
- **双向链表**：负责维护“最近最少使用”的严格时序。它允许我们在已知某个节点的情况下，以 `O(1)` 的时间将其提拔到头部，或者将尾部最久未使用的节点踢出。

## 🚨 核心踩坑记录与大厂工程化规范

### 1. 为什么 Node 里面除了 value，必须存 key？（高频追问）
最初在设计 `DLinkedNode` 时容易漏掉 `key` 属性。在 LRU 容量满载、触发淘汰机制时，我们不仅要从链表尾部干掉最后一个节点，**还必须同步清理 HashMap 中的对应记录**。如果节点里没有存 `key`，我们就无法拿着它去调用 `cache.remove(key)`，导致哈希表发生内存泄漏。

### 2. 哨兵机制（Dummy Node）的降维打击
为了彻底消灭链表在插入头部、删除尾部时繁琐的 `if (node.prev == null)` 判空逻辑（极易引发 `NullPointerException`），我们在初始化时强行塞入了两个**虚拟前置头节点（head）**和**虚拟后置尾节点（tail）**。有了这两个门神，链表中的任何真实节点永远都有前后驱，所有增删操作变成了极其干脆的纯指针替换。

### 3. `get` 方法背叛 LRU 语义的致命 Bug
在初代代码中，`get` 方法仅仅实现了“查出 value 并返回”，**却忘记了刷新节点在链表中的位置**。既然该节点被重新访问（临幸）了，它就应该被拔出来并重新插入到链表的绝对头部。没有这一步操作，被高频访问的热点数据反而会被当成冷数据在 `put` 时误杀。

### 4. 拒绝手抖：抽取 Helper API (DRY 原则)
在双向链表中，涉及多个节点的前后指针断开与重连，裸写极易出错。我们按照大厂规范，提取了两个见名知意的私有方法：`remove(DLinkedNode node)` 和 `movetoHead(DLinkedNode node)`。这使得 `get` 和 `put` 的主干逻辑变得极其清爽和具有自解释性。

## 🏭 工业界真实工程实例：大厂如何魔改 LRU？
在真实的千万级并发后端架构中，标准的 LRU 会面临严峻挑战，我们探讨了三种工业界真实解法：
1. **防范缓存污染（MySQL InnoDB 的妥协）**：面对全表扫描带来的海量冷数据涌入，MySQL 将 LRU 截断为“热数据区”和“冷数据区”（Midpoint Insertion）。新数据只进冷区，停留足够长且被再次访问才能晋升热区，完美保护了核心热点数据。
2. **高并发下的锁竞争（Guava / Caffeine 的本地缓存演进）**：标准 LRU 的 `get` 操作因为要挪动链表，居然需要加写锁，这在高并发下是灾难。现代 Java 缓存框架通过引入“分段锁（Sharding）”或直接使用异步 RingBuffer 消费访问事件，实现了真正的无锁读。
3. **极致内存压榨（Redis 的近似 LRU）**：在 64 位机器下，双向链表的 `prev` 和 `next` 指针极度消耗内存。Redis 直接废弃了双向链表，改用在对象头里记录 24-bit 的“最后访问时间戳”，并在淘汰时通过**随机抽样**对比时间戳，用极小的精度牺牲换回了海量的内存空间。

## 💻 最终 Java 代码实现
> *注：本次提交执行用时 67ms（击败 8.01%），内存消耗 127.60MB（击败 90.76%）。用时偏高是因为大量的对象创建 (`new DLinkedNode`) 和 GC 耗时。在极其变态的算法竞赛中，有人会用一维数组充当链表指针来优化到 10ms 以内，但在大厂业务面试中，当前的面向对象写法（OOP）具备极强的可读性和扩展性，属于绝对的满分标准答案。*

```java
import java.util.HashMap;
import java.util.Map;

// 内部类：双向链表节点
class DLinkedNode {
    int key;    
    int value;
    DLinkedNode prev;
    DLinkedNode next;
    
    public DLinkedNode() {}
    
    public DLinkedNode(int _key, int _value) {
        key = _key;
        value = _value;
    }
}

class LRUCache {
    private Map<Integer, DLinkedNode> cache = new HashMap<>();
    private int size;
    private int capacity;
    // 虚拟头尾节点（哨兵）
    private DLinkedNode head, tail;

    public LRUCache(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        
        head = new DLinkedNode();
        tail = new DLinkedNode();
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        if (cache.containsKey(key)) {
            DLinkedNode node = cache.get(key);
            // 命中缓存：刷新到头部
            remove(node);
            movetoHead(node);
            return node.value;
        } else {
            return -1;
        }
    }
    
    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            DLinkedNode node = cache.get(key);
            node.value = value;
            remove(node);
            movetoHead(node);
        } else {
            if (size == capacity) {
                // 容量满：淘汰尾部的最久未使用节点
                DLinkedNode last = tail.prev;
                cache.remove(last.key);
                last.prev.next = tail;
                tail.prev = last.prev;
                size--;
            }
            DLinkedNode node = new DLinkedNode(key, value);
            movetoHead(node);
            cache.put(key, node);
            size++;
        }
    }

    // 将一个节点从链表中摘除
    private void remove(DLinkedNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // 将一个节点挂载到虚拟头节点之后
    private void movetoHead(DLinkedNode node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
}