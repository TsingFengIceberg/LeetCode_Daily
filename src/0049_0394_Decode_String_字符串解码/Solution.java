import java.util.Stack;

class Solution {
    public String decodeString(String s) {
        // k 用于临时记录当前解析到的倍数
        // count 用于在出栈时接收具体的重复次数
        int k = 0, count = 0;
        
        // currentStr 永远指向【当前所在嵌套层级】正在构建的局部字符串
        StringBuilder currentStr = new StringBuilder();
        
        // 双栈：一个存进入深层嵌套前的外层倍数，一个存进入深层嵌套前的外层已拼接字符串
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