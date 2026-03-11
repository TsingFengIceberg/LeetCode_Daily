import java.util.Deque;
import java.util.ArrayDeque;

class MinStack {

    // 1. 声明成员变量（全局可见）
    // 注意：泛型必须用包装类 Integer，不能用基本类型 int
    Deque<Integer> dataStack;
    Deque<Integer> minStack;

    public MinStack() {
        // 2. 在构造函数中进行初始化
        dataStack = new ArrayDeque<>();
        minStack = new ArrayDeque<>();
    }
    
    public void push(int val) {
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
        dataStack.push(val);
    }
    
    public void pop() {
        if (dataStack.isEmpty()) {
            return; // 或者抛出异常，根据需求决定
        }
        int top = dataStack.pop();
        if (top == minStack.peek()) {
            minStack.pop();
        }
    }
    
    public int top() {
        if (dataStack.isEmpty()) {
            throw new RuntimeException("Stack is empty"); // 或者返回一个特殊值，根据需求决定
        }
        return dataStack.peek();
    }
    
    public int getMin() {
        if (minStack.isEmpty()) {
            throw new RuntimeException("Stack is empty"); // 或者返回一个特殊值，根据需求决定
        }
        return minStack.peek();
    }
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(val);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */