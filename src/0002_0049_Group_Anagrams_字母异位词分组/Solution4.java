import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution4 {
    /**
     * 解法 4：大厂底层微操版 (专治 LeetCode 跑分不服)
     * * 【终极优化点】：
     * 1. 消除 Rehash（扩容）开销：直接给 HashMap 指定初始容量为 strs.length。
     * 默认 HashMap 只有 16 个坑位，装满会不断申请新内存并重新计算 Hash（极度耗时）。
     * 一次性给足空间，彻底消灭扩容产生的 CPU 和内存垃圾损耗。
     * 2. 消除 Lambda 匿名内部类开销：在 10000 次的高频 for 循环中，
     * map.computeIfAbsent(..., k -> new ArrayList<>()) 每次都会产生极其微小的动态调用开销。
     * 回归最原始的 get() 和 put()，把指令精简到极致。
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        // 【微操 1】预分配最大可能的容量。
        // 即便每个单词都不一样，最多也就 strs.length 个组。
        // 这样 HashMap 在运行期间绝对不会触发耗时的 resize() 扩容操作。
        Map<String, List<String>> map = new HashMap<>(strs.length);
        
        for (String s : strs) {
            // 依然保持最高效的特征提取：用 char[] 代替 int[]
            char[] counts = new char[26];
            for (int i = 0; i < s.length(); i++) {
                counts[s.charAt(i) - 'a']++;
            }
            
            // 极速内存拷贝，生成长度固定为 26 的 String 暗号
            String key = new String(counts);
            
            // 【微操 2】抛弃优雅的 computeIfAbsent，使用最底层的原始写法
            // 避开 Lambda 表达式在高频循环中的微小性能惩罚
            List<String> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }
            // 将当前单词加入队伍
            list.add(s);
        }
        
        // 返回最终的所有分组
        return new ArrayList<>(map.values());
    }
}