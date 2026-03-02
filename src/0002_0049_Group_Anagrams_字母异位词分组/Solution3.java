import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution3 {
    /**
     * 解法 3：极限常数优化版 (超越 99% 的神仙写法)
     * * 【优化核心】：
     * 1. 抛弃 int[]，改用 char[] 做计数器。因为题目限制单词长度 <= 100，
     * 一个字母最多出现 100 次，而 char 类型能存 0~65535 的数字，完全够用且更省内存。
     * 2. 抛弃耗时的 Arrays.toString()。直接 new String(char[])！
     * 在 Java 底层，这是一次极速的连续内存拷贝，生成的 String 永远只有 26 个字符长，
     * 完美避开了拼接 "[1, 0, 0...]" 这种长达 70 多个字符的垃圾开销。
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        // 核心容器依然不变
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            // 1. 极限操作：用 char 数组代替 int 数组
            char[] counts = new char[26];
            
            // 2. 统计频次
            for (int i = 0; i < s.length(); i++) {
                // Java 允许对 char 进行自增操作，相当于底层的 ASCII 数值 + 1
                counts[s.charAt(i) - 'a']++;
            }
            
            // 3. 降维打击：直接把 char 数组塞进 String 的构造函数里
            // 这样生成的暗号，内存极其紧凑，且哈希碰撞率极低
            String key = new String(counts); 

            // 4. 查表并分组
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());
    }
}