import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution1 {
    /**
     * 解法 1：排序法
     * 时间复杂度：O(N * K log K)，N 是字符串数组的长度，K 是字符串的最大长度。
     * 空间复杂度：O(N * K)，用于存储哈希表及里面的结果。
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        // 1. 初始化核心数据结构：哈希表
        // Key (String): 排序后的“标准暗号”，例如 "aet"
        // Value (List<String>): 对应这个暗号的所有原单词集合，例如 ["eat", "tea", "ate"]
        Map<String, List<String>> map = new HashMap<>();

        // 2. 遍历输入的每一个字符串
        for (String s : strs) {
            // --- 第一步：制作暗号 ---
            // Java 中的 String 是不可变的，不能直接原地排序，必须先转成字符数组
            char[] chars = s.toCharArray();
            // 对字符数组进行按字典序（a-z）排序
            Arrays.sort(chars);
            // 将排好序的字符数组重新拼回字符串，得到该单词的“标准暗号”
            String key = new String(chars);

            // --- 第二步：查表并分组 ---
            // computeIfAbsent 是 Java 8 引入的神器：
            // 语义：去 map 里找 key，如果找不到，就按后面的规则 (new ArrayList<>()) 创一个空的放进去。
            // 最后，它会返回这个 key 对应的 List，我们紧接着调用 .add(s) 把当前原单词塞进这个组里。
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        // 3. 提取结果
        // map.values() 返回的是所有 Value 的集合（即所有的 List<String>），
        // 题目要求返回 List<List<String>>，所以我们用 ArrayList 把它们包装一下返回。
        return new ArrayList<>(map.values());
    }
}