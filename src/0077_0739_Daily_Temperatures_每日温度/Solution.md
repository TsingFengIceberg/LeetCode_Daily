# [0739. 每日温度 (Daily Temperatures)]

## 💡 核心思想：单调栈 (Monotonic Stack) 的降维打击
这道题是典型的“寻找下一个更大元素”问题。如果使用双重 `for` 循环暴力查找，时间复杂度将达到 $O(N^2)$，在 $10^5$ 的数据规模下必然超时（TLE）。
破局的核心在于**后进先出 (LIFO)** 的思维：将尚未找到更高温度的日子的**下标（索引）**暂存在栈中（这被称为“挂起”）。当遇到一个高温日时，它就像一把镰刀，回头去收割栈里所有温度比它低的日子，计算天数差。因为每个元素最多进栈一次、出栈一次，所以均摊时间复杂度被完美降维到了 $O(N)$。

## 🚨 踩坑实录与面试官灵魂拷问（一字不差的答疑错误记录）
在初次构建单调栈框架时，你的大方向是完全正确的，但代码中潜伏了几个足以导致程序崩溃（Crash）和逻辑全错的致命 Bug。以下是你当时的原始错误记录与纠正过程：

* **致命错误 1：“苹果比橘子”的数值错位陷阱**
    * 你的原代码：`while(temperatures[i] > result.peek() ...)`
    * **纠偏**：你在栈 `result` 里存入的是**天数（下标 i）**，但你却直接拿当天的**温度**去和栈顶的**下标**作比较！这就好比拿苹果和橘子比大小。必须通过下标去原数组里取值，正确写法是：`temperatures[i] > temperatures[result.peek()]`。
* **致命错误 2：逻辑运算符与短路求值的灾难**
    * 你的原代码条件：`while(temperatures[i] > ... || !result.isEmpty())`
    * **纠偏**：
        1. **符号写反**：你使用了 `||` (或)，这意味着只要栈不为空，不管今天温度高不高，都会把过去的日子无脑出栈，导致逻辑全盘崩溃。应该使用 `&&` (与)。
        2. **顺序写反（直接导致崩溃）**：Java 的逻辑运算具有**短路特性**。你把比较温度放在了判空的前面，如果当前栈已经是空的，程序会直接执行 `result.peek()`，当场抛出 `EmptyStackException` 异常。必须把 `!result.isEmpty()` 放在 `&&` 的最左侧充当护城河。
* **代码坏味道 1：冗余的无效变量**
    * 你的原代码：`int currenrMaxTemperatures = 29;`
    * **纠偏**：不仅单词拼写错误，而且整个方法中根本没有用到这个变量。在真实的极客/大厂代码评审中，这是非常扣分的无用代码。
* **代码坏味道 2：对 Java 数组基础特性不熟**
    * 你的原代码在末尾加了兜底：`while(!result.isEmpty()){ int day = result.pop(); answer[day] = 0; }`
    * **纠偏**：在 Java 中，`new int[temperatures.length]` 刚创建时，所有元素的默认值就已经是 `0` 了。所以对于那些一直没等到升温的日子，根本不需要再去显式地赋值为 `0`，这段代码是完全多余的。

## 🏗️ 架构师的 Java 最佳实践科普
虽然你用了 `java.util.Stack` 通过了测试（执行用时 120ms，击败 16.04%），但在现代 Java 大厂开发中，**强烈不推荐使用 `Stack` 类**。因为它继承自 `Vector`，所有方法都加了 `synchronized` 锁，导致单线程刷题时性能开销极大，这也是你这道题耗时偏高的原因之一。
* **大厂最佳实践**：推荐使用双端队列 `Deque<Integer> stack = new ArrayDeque<>();` 来代替 `Stack`，能显著提升执行效率。

## 🏭 工业界真实工程实例：量化交易的高频实时行情流与“价格突破”监控
单调栈在真实的金融高频交易（HFT）和实时监控系统中是不可或缺的底层基石。
* **业务痛点**：在亿级并发的股票实时行情流（Tick Data）中，几百万用户订阅了“当价格突破近期震荡高点时立刻推送预警”。如果每来一个新价格都往回遍历历史数据，流式计算集群会瞬间瘫痪。
* **架构落地**：在 Flink 算子内部维护一个单调栈。当价格下跌或震荡时，不产生实际计算，只把时间戳压栈“挂起”；当一根“大阳线”瞬间拉高时，触发事件驱动（Event-Driven），用 `while` 循环迅速向下收割所有被突破的历史点位，并立即推送到 Kafka。不仅把时间复杂度压榨到极致，还因为及时 `pop` 废弃数据，使得栈深度极浅，为公司节省了海量的内存资源。

## 最终 Java 代码实现
```java
import java.util.Stack;

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        // 1. 核心数据结构：单调栈 (Monotonic Stack)
        // 栈里存储的是“数组的索引（天数）”，而不是具体的温度值！
        // 这样既能计算时间差，又能通过 temperatures[index] 拿到温度。
        Stack<Integer> result = new Stack<>();
        int[] answer = new int [temperatures.length];
        
        // 2. 将第 0 天的索引先压入栈中，作为第一个“等待升温”的日子
        result.add(0);
        
        // 3. 遍历后续的每一天
        for (int i = 1; i < temperatures.length; i++){
            // 4. 核心结算逻辑（注意短路机制的完美应用）：
            // 条件 1: !result.isEmpty() 确保栈不为空，防止 EmptyStackException 崩溃。
            // 条件 2: temperatures[i] > temperatures[result.peek()] 
            // 如果今天的温度，大于栈顶那一天（最近一个还在等待的）的温度，说明栈顶等到了升温！
            while(!result.isEmpty() && temperatures[i] > temperatures[result.peek()]){
                // 弹出等到了升温的这一天
                int day = result.pop();
                // 结算天数差：今天 (i) 减去 过去那一天 (day)
                answer[day] = i - day;
            }
            // 结算完过去所有能被今天打破记录的日子后，今天自己也需要入栈，等待未来更热的一天
            result.push(i);
        }
        
        // 5. 兜底逻辑：处理那些一直到最后都没等到升温的日子
        // 【面试官的善意提醒】：虽然这段逻辑完全正确，但在 Java 中其实是多余的。
        // 因为 new int[] 初始化的数组，默认值就全是 0。你不填，它也是 0。
        // 不过在真实的 C++ 或 C 中，如果不初始化，内存里是脏数据，这段逻辑就是必须的！
        while(!result.isEmpty()){
            int day = result.pop();
            answer[day] = 0;
        }
        
        return answer;
    }
}
