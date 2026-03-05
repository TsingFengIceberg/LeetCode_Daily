public class Solution4 {
    /**
     * 方法四：环状替换法
     * 时间复杂度：$O(N)$ -> 每个元素只会被遍历并交换一次
     * 空间复杂度：$O(1)$
     * 评语：极其考验逻辑严密性。需要处理“成环”的情况（当 n 和 k 有最大公约数时，会陷入死循环，需要外层移动 start 突破环）。
     */
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        if (k == 0) return;

        int count = 0; 
        // start 代表每次抢座游戏的“第一个发起人”
        for (int start = 0; count < n; start++) {
            int current = start;        // 当前拿着入场券找座位的人的当前位置
            int prev = nums[start];     // current 这个人本身（他准备去挤别人）
            
            do {
                int next = (current + k) % n; // 计算他该去哪个新坑位
                
                // 核心：鸠占鹊巢（把 next 坑位的人挤出来存到 temp 里，自己 prev 坐进去）
                int temp = nums[next];
                nums[next] = prev;
                prev = temp; // 手里换成了那个刚刚被挤出来的倒霉蛋
                
                current = next; // 当前位置移动到新坑位
                count++;        // 成功安排了一个人入座！
                
            } while (start != current); // 只要被挤出来的倒霉蛋不是最开始空出座位的那个坑（还没形成闭环），就继续挤！
        }
    }
}