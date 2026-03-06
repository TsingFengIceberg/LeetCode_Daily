# LeetCode 138. 随机链表的复制 (Copy List with Random Pointer) - 深度复盘笔记

## 💡 核心算法思想：图的深拷贝与空间优化
带有 `random` 指针的链表，其物理结构已经从线性的“链”退化成了复杂的“有向图”。深拷贝的核心难点在于：**如何通过老节点，以极低的时间成本瞬间找到对应的新节点？**
根据对额外空间（空间复杂度）的容忍度，本题演化出了从 $O(N)$ 到 $O(1)$ 的三套阶梯式打法。

---

## 🧠 核心疑问与思维推导全纪录

### 阶段一：初见端倪（直觉解法构想）
面对复杂的图拷贝，最初推导出了两种利用外部空间的直觉解法：
1. **DFS 递归法**：顺着 `next` 和 `random` 一直往下遍历，用外部空间（如 Map）记录已复制的节点，防止死循环。
2. **HashMap 迭代法**：遍历两遍，第一遍用哈希表建立 `<老节点, 新节点>` 的绝对映射；第二遍遍历原链表，利用哈希表的 $O(1)$ 查找，闭着眼睛把 `next` 和 `random` 连起来。

### 阶段二：终极挑战（挑战 $O(1)$ 空间）
面试官抛出追问：“能否不借助 HashMap，把空间复杂度压到 $O(1)$？”
* **灵光一闪的破局点（交织法）**：既然不能用 Map 记录映射，那就**利用物理位置进行硬绑定**。将新节点直接插入到对应的老节点之后，形成 `A -> A' -> B -> B'` 的交织结构。这样通过 `老节点.next` 就能瞬间找到 `新节点`。
* **致命陷阱（过河拆桥太早）**：构思时曾设想“第二遍遍历连好 random 后顺便拆分链表”。但经过推演发现：如果提早把 `A` 和 `A'` 拆开，走到 `B` 时，`B` 的 `random` 就无法通过原有的老链表路径准确找到目标了。
* **最终定型（经典三步走）**：
  1. **克隆交织**：打造新节点并塞入老节点之间。
  2. **连接 Random**：利用公式 `新.random = 老.random.next` 连线。
  3. **链表剥离**：彻底将两条链表拆解，恢复老链表，提取新链表。

---

## 🏭 工业界工程实例分析

1. **JVM 垃圾回收（GC）的 Forwarding Pointer（转发指针）**
   * **场景**：JVM 内存碎片整理，需要把存活对象搬运到新内存区。
   * **落地**：此时内存本就吃紧，绝无可能开辟巨大的 HashMap 来记录新老地址映射。JVM 会采用 $O(1)$ 空间解法的思想：把对象搬到新家后，直接**将新地址写在老对象的对象头（Mark Word）中**。后续遍历只要看一眼老对象，就能瞬间顺藤摸瓜找到新对象并更新引用。
2. **序列化框架（Dubbo/Fastjson）防循环引用**
   * **场景**：微服务间通过 RPC 传输复杂对象，对象间存在循环引用（如 `User -> Order -> User`），直接解析会导致 `StackOverflowError`。
   * **落地**：业界顶级序列化框架采用上述的**方案二（DFS + Visited Map）**。在解析对象前，先将其内存地址丢进 `IdentityHashMap`。一旦后续 `random` 属性又指回了这个对象，查 Map 发现已存在，则直接输出引用占位符（如 `{"$ref":"$"}`），完美打破死锁。

---

## 💻 最终定稿 Java 代码 (三套大厂标准模板)

### 方案一：$O(1)$ 空间原位交织法（最高性能，0ms 击败 100%）
```java
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

public class Solution {
    public Node copyRandomList(Node head) {
        if (head == null) return null;
        
        Node p = head, q, r, s = null, newHead = null;
        boolean isFirst = true;

        // Round 1: 节点交织克隆 (A -> A' -> B -> B')
        while (p != null) {
            q = p.next;
            Node copy = new Node(p.val);
            p.next = copy;
            copy.next = q;
            p = q;
        }

        // Round 2: 连接克隆节点的 random 指针
        p = head;
        while (p != null) {
            q = p.next; // q 为新节点
            if (p.random != null) {
                q.random = p.random.next; // 核心公式
            }
            p = q.next;
        }

        // Round 3: 链表剥离（提取新链表，恢复老链表）
        p = head;
        while (p != null) {
            q = p.next;
            r = q.next;
            
            if (r != null) q.next = r.next;
            else q.next = null;
            
            p.next = r; // 恢复老链表
            p = r;
            
            if (!isFirst) s.next = q;
            else { newHead = q; isFirst = false; }
            s = q;
        }
        return newHead;
    }
}

```

### 方案二：$O(N)$ 空间 HashMap 迭代法（业务开发最常用、最稳健）

```java
import java.util.HashMap;
import java.util.Map;

class Solution2 {
    public Node copyRandomList(Node head) {
        if (head == null) return null;
        Map<Node, Node> map = new HashMap<>();
        
        // 1. 造皮囊
        Node curr = head;
        while (curr != null) {
            map.put(curr, new Node(curr.val));
            curr = curr.next;
        }
        
        // 2. 连经脉
        curr = head;
        while (curr != null) {
            map.get(curr).next = map.get(curr.next);
            map.get(curr).random = map.get(curr.random);
            curr = curr.next;
        }
        return map.get(head);
    }
}

```

### 方案三：$O(N)$ 空间 DFS 递归法（序列化框架底层原型）

```java
import java.util.HashMap;
import java.util.Map;

class Solution3 {
    private Map<Node, Node> visitedHash = new HashMap<>();

    public Node copyRandomList(Node head) {
        if (head == null) return null;
        
        // 查字典，防死锁
        if (visitedHash.containsKey(head)) return visitedHash.get(head);

        Node newNode = new Node(head.val);
        // 先入字典，再递归！
        visitedHash.put(head, newNode);

        newNode.next = copyRandomList(head.next);
        newNode.random = copyRandomList(head.random);

        return newNode;
    }
}

```

