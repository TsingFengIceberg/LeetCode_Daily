# [0155. 最小栈 (Min Stack)]

## 💡 算法嗅觉与状态维护：从滑动窗口到栈的蜕变
在起手分析阶段，你敏锐地将本题与“滑动窗口最大值”（239题）联系了起来，这证明你已经具备了构建“辅助数据结构动态维护最值”的底层算法嗅觉。但我们迅速明确了两者在状态维护上的核心差异：
* **滑动窗口（双端队列）**：窗口两头通畅，老元素会随着时间流逝/窗口滑动而“过期”，必须动态清理无竞争力的元素。
* **栈（LIFO结构）**：一头堵死，出栈入栈严格逆序。这意味着**历史状态是严格回溯的**，只要元素不出栈，当时的全局最小值就永远固定。因此不需要复杂的双端队列，直接采用**空间换时间**的双栈法即可完美解决。

## 🚨 致命细节拷问：辅助栈的“非严格同步”与边界防御
在敲定双栈法（`dataStack` 和 `minStack`）后，我们探讨了常数级空间优化的“非严格同步压栈策略”。期间你遇到了一个极其经典的逻辑陷阱：
* **易错点**：认为新元素“严格小于”辅助栈顶时才压入辅助栈。
* **纠正过程**：如果入栈序列包含重复的最小值（如 `[-2, 0, -2]`），当主栈弹出第二个 `-2` 时，如果辅助栈没有备份这个相等的最小值，就会导致辅助栈提前被弹空，后续调用 `getMin()` 瞬间崩溃。
* **最终定论**：压入辅助栈的条件必须是 **小于等于 (`<=`)** 栈顶元素，做好历史记录的冗余备份。

## ☕ Java 工程化踩坑与“无心插柳”的拆箱黑科技
我们在编码落地前，重点排雷了 Java 集合类的选型与对象比较机制：
1. **容器选型**：明确废弃带有重量级同步锁 `synchronized` 的陈旧 `java.util.Stack`，转而使用现代且基于连续内存、性能极高的 `Deque` 接口及其实现类 `ArrayDeque`。
2. **包装类陷阱与绝妙的 Auto-unboxing**：
   * 在出栈比较时，如果直接用 `minStack.peek() == dataStack.peek()`，由于存储的是 `Integer` 包装类，一旦数值超出 `[-128, 127]` 的缓存范围，比较的就是对象引用地址，必定返回 `false` 导致严重 Bug。标准解法是使用 `.equals()`。
   * **你的高光时刻**：你在代码中写出了 `int top = dataStack.pop(); if (top == minStack.peek())`。这行代码极其巧妙地利用了 Java 的**自动拆箱（Auto-unboxing）**特性！将弹出的 `Integer` 赋值给基本类型 `int`，后续的 `==` 强制触发了 `minStack.peek()` 的拆箱操作，直接变成了基础数值的比对，完美、优雅地跨越了对象引用的深坑！

## 🏗️ 工业界真实工程实例：微服务 RPC 架构下的超时约束栈
跳出算法题，我们探讨了 `MinStack` 思想在工业界大厂后端中间件中的真实落地场景：**RPC 框架与嵌套事务的超时时间管理**。
* **工程痛点**：在高并发场景下（如电商大促），微服务之间的嵌套调用链路极长（A -> B -> C）。如果每次进入深层方法都要 $O(N)$ 向上回溯寻找“最严格的超时底线”，会引发剧烈的 CPU 性能损耗。
* **架构解法**：在线程上下文（ThreadLocal）中同步维护一个“最小约束栈（Min Constraint Stack）”。每一层方法压栈时，与当前的最小超时时间进行打擂台并同步压栈；方法执行完毕弹栈时，超时约束瞬间 $O(1)$ 回退到上一层的状态。拦截器只需 `peek()` 一次，即可实施精准熔断。利用 $O(N)$ 的空间换取高频状态查询的 $O(1)$ 耗时，这是高可用架构的核心基石。

## 💻 最终 Java 代码实现

```java
import java.util.Deque;
import java.util.ArrayDeque;

class MinStack {

    // 使用更高效的 ArrayDeque 替代老旧的 java.util.Stack
    Deque<Integer> dataStack;
    Deque<Integer> minStack;

    public MinStack() {
        dataStack = new ArrayDeque<>();
        minStack = new ArrayDeque<>();
    }
    
    public void push(int val) {
        // 注意核心边界条件：小于等于 (<=) 时必须压入，防止多个相同最小值被提前弹空
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
        dataStack.push(val);
    }
    
    public void pop() {
        if (dataStack.isEmpty()) {
            return;
        }
        // 极致巧妙的工程 Trick：通过赋给 int 类型触发自动拆箱（Auto-unboxing）
        // 从而安全地使用 == 进行数值比较，避开了 Integer 缓存范围外的引用地址对比陷阱
        int top = dataStack.pop();
        if (top == minStack.peek()) {
            minStack.pop();
        }
    }
    
    public int top() {
        if (dataStack.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return dataStack.peek();
    }
    
    public int getMin() {
        if (minStack.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return minStack.peek();
    }
}