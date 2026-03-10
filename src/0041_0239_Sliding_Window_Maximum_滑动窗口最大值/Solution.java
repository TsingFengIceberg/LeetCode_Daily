import java.util.Deque;
import java.util.LinkedList;

class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        // 核心武器：双端队列。注意：里面存的是员工的“工牌号（下标 Index）”，代表寿命！
        Deque<Integer> deque = new LinkedList<>();
        
        // 记录每个窗口最大值的“工资单”
        int[] result = new int[nums.length - k + 1];
        
        for (int i = 0; i < nums.length; i++) {
            
            // 🔪 1. 职场大清洗（新员工屠杀）：
            // 当新员工 i 准备从队尾入场时，看看队尾的老员工（deque.peekLast()）
            // 只要老员工的战斗力（nums[老员工]）不如新员工（nums[i]）
            // 新员工就会毫不犹豫地把他从队尾踢走（pollLast），直到碰到比自己强的真大佬为止。
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            // 👔 2. 新员工正式入座：
            // 杀完弱者后，新员工堂堂正正地排在大佬后面（或者自己成了队首的大佬）
            deque.offerLast(i);
            
            // 💰 3. 窗口成型，开始发工资：
            // 只有当 i 走到 k-1 时（比如 k=3，i走到2），第一个窗口才算真正填满，以后每次滑动都要记录
            if (i >= k - 1) {
                // 队首（peekFirst）永远是经过大清洗后存活下来的当前最强者！直接发最高工资！
                result[i - k + 1] = nums[deque.peekFirst()];
                
                // ⏳ 4. 神级预判（老国王按时退休）：
                // i - k + 1 正好是当前窗口的“最左侧边界”
                // 如果当前的最强者（队首下标）刚好等于这个左边界，
                // 说明等下一次 i++ 窗口往右滑动时，他就彻底过期（出局）了！
                // 所以我们在这里提前办理退休手续，把他从左侧无情踢走。
                if (deque.peekFirst() == i - k + 1) {
                    deque.pollFirst();
                }
            }
        }
        
        return result;
    }
}