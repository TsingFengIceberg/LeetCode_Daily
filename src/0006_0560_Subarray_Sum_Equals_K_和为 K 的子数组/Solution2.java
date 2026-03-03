import java.util.Arrays;

/**
 * 前缀和 + 手写原生类型哈希表 (Primitive Hash Map)
 * 时间复杂度: O(N) 常数项被极限压缩
 * 空间复杂度: O(N) 无 GC 压力
 */
public class Solution2 {
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int preSum = 0;
        
        // 优化点 A：手动实现哈希表，彻底消除 Integer 装箱带来的对象创建和 GC 停顿
        // 数组长度最大 20000，我们开辟 65536 (2的16次方) 的空间，保证负载因子极低，减少冲突
        int capacity = 65536; 
        int mask = capacity - 1; // 用于位与运算，等价于 % capacity，但速度极快
        
        // keys 存前缀和，values 存出现的次数
        int[] keys = new int[capacity];
        int[] values = new int[capacity];
        
        // 优化点 B：由于前缀和可能为 0，我们用 Integer.MAX_VALUE 作为一个特殊的占位符，表示该位置“为空”
        Arrays.fill(keys, Integer.MAX_VALUE);
        
        // 初始化 map.put(0, 1) 的手写实现
        put(keys, values, mask, 0, 1);
        
        for (int num : nums) {
            preSum += num;
            
            // 相当于 map.get(preSum - k)
            count += get(keys, values, mask, preSum - k);
            
            // 相当于 map.put(preSum, map.getOrDefault(...) + 1)
            int currentCount = get(keys, values, mask, preSum);
            put(keys, values, mask, preSum, currentCount + 1);
        }
        
        return count;
    }
    
    // --- 以下为手写哈希表的核心逻辑 (开放寻址法 - 线性探测) ---
    
    // 简易且极速的 Hash 函数，打乱 bits 减少冲突
    private int hash(int key) {
        return key ^ (key >>> 16);
    }
    
    private void put(int[] keys, int[] values, int mask, int key, int val) {
        int idx = hash(key) & mask;
        // 如果位置被占了，且不是我们要找的 key，就顺延找下一个位置 (线性探测)
        while (keys[idx] != Integer.MAX_VALUE && keys[idx] != key) {
            idx = (idx + 1) & mask;
        }
        keys[idx] = key;
        values[idx] = val;
    }
    
    private int get(int[] keys, int[] values, int mask, int key) {
        int idx = hash(key) & mask;
        while (keys[idx] != Integer.MAX_VALUE) {
            if (keys[idx] == key) {
                return values[idx];
            }
            idx = (idx + 1) & mask;
        }
        return 0; // 没找到返回 0
    }
}