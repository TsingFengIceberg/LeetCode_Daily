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