import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution2 {
    /**
     * 解法 2：频次计数法（大厂高分优化解）
     * 时间复杂度：O(N * K)，N 是字符串数组长度，K 是字符串最大长度。彻底消除了排序的 logK 耗时！
     * 空间复杂度：O(N * K)，同样用于存储哈希表及里面的结果。
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        // 1. 初始化哈希表，和解法一的作用完全相同
        Map<String, List<String>> map = new HashMap<>();

        // 2. 遍历输入的每一个字符串
        for (String s : strs) {
            // --- 第一步：制作暗号 (计数频次) ---
            // 创建一个长度为 26 的数组，专门用来记录 a-z 每个字母出现的次数
            // 默认情况下，int 数组里的元素全是 0
            int[] counts = new int[26];
            
            // 遍历当前单词的每一个字符，统计频次
            for (int i = 0; i < s.length(); i++) {
                // ASCII 码运算魔法：'a' - 'a' = 0 (对应数组下标 0)
                // 'b' - 'a' = 1 (对应数组下标 1)... 以此类推
                counts[s.charAt(i) - 'a']++;
            }
            
            // --- 第二步：把频次数组变成哈希表能识别的 Key ---
            // ⚠️ 重点大坑：在 Java 中，绝不能直接用 int[] 作为 HashMap 的 Key！
            // 因为数组比较的是内存地址，不是里面的内容。
            // 解决办法：用 Arrays.toString() 把数组转成长字符串，例如 "[1, 0, 0, ..., 1]"
            // 只要字母频次相同，生成的字符串就绝对相同！
            String key = Arrays.toString(counts); 

            // --- 第三步：查表并分组 ---
            // 逻辑同解法一，找到暗号对应的队伍，把原单词加进去
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        // 3. 将 Map 中所有的分组列表打包返回
        return new ArrayList<>(map.values());
    }
}