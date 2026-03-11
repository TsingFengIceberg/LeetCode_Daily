# [0394. 字符串解码 (Decode String)]

## 💡 算法破局：从“括号栈”到“双栈状态机”
面对嵌套结构的字符串解析（如 `3[a2[c]]`），最初直觉是维护一个“括号栈”来检查嵌套深度，但在处理多层复杂嵌套时，手动维护拼接边界极易出错。
经过点拨，我们将思维升级为**“状态保存与恢复”**的标准大厂解法——**双栈法**：
1. **数字栈 (`countStack`)**：遇到 `[` 时，封存外层已统计到的倍数 $k$。
2. **字符串栈 (`stringStack`)**：遇到 `[` 时，封存外层已经拼好的局部前缀字符串。
每当遇到右括号 `]`，即宣告“当前子世界”解析结束。此时直接出栈，将当前层级构建的字符串翻倍，并无缝拼接回外层环境。全程无需判断栈空，逻辑极致清晰，时空复杂度均为 $O(N)$（$N$ 为解码后的总长度）。

## ☕ Java 工程化特训与底层避坑指南
在本次解题中，我们重点纠正并沉淀了以下 Java 工业级核心规范：
1. **彻底摒弃 `String` 的 `+=` 拼接**：Java 中的 `String` 是不可变对象（Immutable）。如果在循环中频繁拼接，会在堆内存中引发灾难性的对象拷贝与频繁 GC，使时间复杂度退化至 $O(N^2)$。**必须使用 `StringBuilder` 实现 $O(1)$ 的原地追加。**
2. **多位数字字符的极速解析**：不能仅仅解析单个字符，必须使用高频面试套路公式：`k = k * 10 + (ch - '0')` 来实现位数的动态进位累加，且必须在遇到 `[` 压栈后**立即将 $k$ 归零**。
3. **`Stack` 类的时代眼泪**：虽然使用了 `java.util.Stack` 顺利 AC，但在现代 Java 工程中，`Stack` 继承自 `Vector`，其底层所有核心方法均被 `synchronized` 关键字修饰。这种无意义的同步锁会造成性能损耗。**在后续的大厂实战中，应统一使用双端队列 `Deque<Integer> stack = new ArrayDeque<>();` 来作为栈的标准替代品。**

## 🏢 工业界真实工程实例：微型解析器与高并发“防弹衣”
这道题不仅是算法，更是后端核心中间件的基石：
1. **微服务配置中心的占位符解析（Spring Boot / Nacos / Apollo）**：
   在处理形如 `${MYSQL_URL:${DEFAULT_HOST:localhost}}` 的嵌套动态配置时，底层源码正是利用了同样的双栈解析思想。若使用正则表达式，面对极深嵌套会引发指数级回溯（ReDoS 攻击），而栈式扫描能保证 $O(N)$ 的稳定极速。
2. **风控引擎的 AST（抽象语法树）解析**：
   在解析 `(A > B AND (C == D OR E < F))` 这种规则链时，使用双栈可以毫秒级合并逻辑子树。
3. **为什么大厂偏爱“显式栈”而非“递归 (DFS)”？**
   在处理未知深度的嵌套文本时，递归会导致方法调用栈极速加深。JVM 线程栈空间极小（通常 1MB），极易触发致命的 `StackOverflowError`，导致整台服务器宕机。而使用分配在堆内存（Heap）中的显式栈，结合 Try-Catch 和熔断限流，即使遇到恶意攻击也能保证微服务的高可用性（HA）。

## 💻 最终 Java 代码实现

```java
import java.util.Stack;

class Solution {
    public String decodeString(String s) {
        // k 用于临时记录当前解析到的倍数
        // count 用于在出栈时接收具体的重复次数
        int k = 0, count = 0;
        
        // currentStr 永远指向【当前所在嵌套层级】正在构建的局部字符串
        StringBuilder currentStr = new StringBuilder();
        
        // 双栈：一个存进入深层嵌套前的外层倍数，一个存进入深层嵌套前的外层已拼接字符串
        // 面试进阶提示：实际工程中建议替换为 Deque<Integer> countStack = new ArrayDeque<>();
        Stack<Integer> countStack = new Stack<>();
        Stack<StringBuilder> stringStack = new Stack<>();
        
        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch)) {
                // 【状态 1：遇到数字】
                // 核心技巧：将连续的字符数字转化为整型。例如 "12" -> 1 * 10 + 2 = 12
                k = k * 10 + (ch - '0');
            }
            else if (ch == '[') {
                // 【状态 2：遇到左括号，准备进入子世界 (更深层级)】
                // 1. 封存当前倍数，并立刻清零，防止影响内层的新数字
                countStack.push(k);
                k = 0;
                
                // 2. 封存外层已经构建好的局部字符串
                stringStack.push(currentStr);
                
                // 3. 极其关键：将 currentStr 重新初始化，准备从零开始接收内层的新字符
                currentStr = new StringBuilder();
            }
            else if (ch == ']') {
                // 【状态 3：遇到右括号，子世界结束，开始与外层合并】
                // 1. 弹出刚才封存的倍数
                count = countStack.pop();
                
                // 2. 弹出外层的“前半部分”字符串。注意：这里弹出的底层对象直接拿来做后续拼接
                StringBuilder decodedStr = stringStack.pop();
                
                // 3. 将当前层级解析出的 currentStr，重复 count 次，追加到外层字符串后面
                for (int i = 0; i < count; i++) {
                    decodedStr.append(currentStr);
                }
                
                // 4. 合并完成！当前层级彻底终结，环境恢复为外层状态，继续向后遍历
                currentStr = decodedStr;
            }
            else {
                // 【状态 4：遇到普通字母】
                // 直接追加到【当前所在层级】的 StringBuilder 中
                currentStr.append(ch);
            }
        }
        
        // 整个字符串遍历完毕，所有括号一定全部闭合，直接返回最终拼好的长字符串
        return currentStr.toString();
    }
}
