class Solution2 {
    public int lengthOfLIS(int[] nums) {
       // tail 数组的物理意义（幻觉数组）：
       // 长度为 i+1 的所有递增子序列中，结尾【最小】的那一个数字（潜力无限！）
       int [] tail = new int [nums.length];
       int size = 0; // 记录 tail 数组当前的有效长度，也是最终的最长长度结果
       
       for (int x : nums) {
           // 掏出二分查找模板！在 tail 数组的有效区间 [0, size) 内寻找 x 的归宿
           int i = 0, j = size;
           while (i != j) {
               int m = (i + j) / 2;
               if (tail [m] < x) {
                   i = m + 1; // x 比中间大，向右边逼近
               } else {
                   j = m;     // x 没那么大，向左边逼近（寻找第一个大于等于 x 的位置）
               }
           }
           
           // 【核心物理动作：强行上位】
           // 无论如何，把 x 放到二分找到的 i 位置上。
           // 1. 如果 i 在内部，这就是“暗中换血”：用更小的 x 替换掉原来的数字，释放更大潜力！
           // 2. 如果 i 等于 size，这就是追加！
           tail [i] = x;
           
           // 【破纪录判定】
           // 如果找到的位置刚好等于当前的有效长度 size，说明 x 比前面所有人都大！
           if (i == size) {
               size++; // 成功开疆扩土，最长记录 +1！
           }
       }
       return size;
    }
}