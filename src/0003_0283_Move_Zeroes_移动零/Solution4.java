public class Solution4 {
    /**
     * 解法 4：大厂底层硬件调优版 (迎合 CPU 分支预测，冲刺 0ms)
     * 核心逻辑回归“两次遍历”，但它是现代 CPU 执行效率最高的方式：
     * 1. 没有任何多余的 swap 或是嵌套的 if 判断，消灭分支预测失败的开销。
     * 2. 纯粹的连续内存顺序写操作，最大化利用 CPU 缓存行 (Cache Line)。
     */
    public void moveZeroes(int[] nums) {
        int slow = 0;
        
        // 第一遍：极速筛选非 0 元素。只有一个极简的 if，CPU 跑得飞快
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                nums[slow++] = nums[fast]; // 直接赋值并自增，连成一句指令
            }
        }
        
        // 第二遍：剩下的坑位，无脑填 0。没有 if 判断，纯粹的内存擦除
        while (slow < nums.length) {
            nums[slow++] = 0;
        }
    }
}