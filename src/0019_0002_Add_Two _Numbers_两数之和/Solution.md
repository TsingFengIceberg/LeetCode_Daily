# 算法复盘笔记：LeetCode 2. 两数相加 (Add Two Numbers)

## 一、 题目剖析与大厂考点
* **考察频次**：⭐⭐⭐⭐⭐ (大厂最经典的“一面第一题”，破冰神题)
* **核心标签**：链表、数学模拟、大数加法
* **时间/空间复杂度要求**：Time: $O(\max(M, N))$, Space: $O(1)$ (不计返回值空间)。
* **题目痛点与易错点**：
  1. **长短不一**：两个链表长度不同时，极易抛出 `NullPointerException`。
  2. **最高位遗漏**：当遍历完所有节点后，如果最后一次加法产生了进位（如 `[5] + [5] = [0, 1]`），容易漏掉链表最末尾新追加的 `1`。
  3. **头节点初始化**：不使用 Dummy Node 的话，需要写一堆恶心的 `if-else` 来判断结果链表的头部。

## 二、 核心解法沉淀

### 最优解：伪头节点 + 全局进位符遍历 (大厂标准写法)
利用 `Dummy Node` 极其优雅地开启新链表。循环条件设为 `l1 != null || l2 != null`，只要还有数字没加完就继续。利用三元运算符优雅地把 `null` 节点视为 `0` 处理。

* **个人巧思**：使用 `boolean flag` (或常规的 `int carry = 0`) 来记录进位，因为两个一位数相加加进位最大为 19，进位必然是 0 或 1。
* **逆序存储的妙用**：由于是从个位开始向高位加，链表头节点正好是个位，这完美契合了人类手写竖式加法“从低到高不断进位”的物理逻辑。

**核心 Java 代码定稿 (1ms 击败 100%)**：
```java
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1); // 伪头节点站岗
        ListNode tail = dummy;
        boolean carry = false; // 进位标志
        
        // 核心：用 || 完美兼容不等长链表
        while (l1 != null || l2 != null) {
            int val1 = (l1 != null) ? l1.val : 0;
            int val2 = (l2 != null) ? l2.val : 0;
            
            int sum = val1 + val2 + (carry ? 1 : 0);
            carry = sum >= 10; // 判断是否产生进位
            
            tail.next = new ListNode(sum % 10); // 剥离出个位数
            tail = tail.next; // 游标后移
            
            if (l1 != null) l1 = l1.next;
            if (l2 != null) l2 = l2.next;
        }
        
        // 致命边界：千万别忘了最后的最高位进位！
        if (carry) {
            tail.next = new ListNode(1);
        }
        
        return dummy.next;
    }
}

```

## 三、 工业界真实场景映射 (工程实例)

1. **任意精度算术 (大数加法引擎)**：在金融量化、核心结算系统中，数字长度经常超过 64位 (`long` 的极限)。Java 的 `BigInteger` 底层就是用整型数组分块存储，其 `add()` 逻辑与本题模拟的按位加法进位机制如出一辙。
2. **区块链以太坊 EVM 虚拟机**：Solidity 智能合约支持 `uint256`（256位整数），而底层物理 CPU 通常只有 64位 ALU。EVM 会在底层软件层面，将 256 位拆成多个 64 位的“节点”进行链式累加和溢出传递（carry propagation）。

---

*复盘日期：2026-03-05 | 导师：LeetCode 助手 (大厂面试官) | 战绩：1ms (100.00%) / 45.79MB (37.32%)*
