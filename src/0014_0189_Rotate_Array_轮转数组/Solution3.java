public class Solution3 {
    /**
     * 方法三：使用额外数组
     * 时间复杂度：$O(N)$
     * 空间复杂度：$O(N)$ -> 开辟了相同大小的新数组
     * 评语：最直观的做法。在业务开发中，如果不要求极致的内存，这种写法最不易出错。
     */
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        if (k == 0) return;

        int[] newArr = new int[n];
        // 核心公式：原索引 i 的元素，轮转 k 步后的新索引是 (i + k) % n
        for (int i = 0; i < n; i++) {
            newArr[(i + k) % n] = nums[i];
        }
        
        // 使用 JDK 提供的 System.arraycopy，底层是 C++ 的 memmove，性能极高
        System.arraycopy(newArr, 0, nums, 0, n);
    }
}