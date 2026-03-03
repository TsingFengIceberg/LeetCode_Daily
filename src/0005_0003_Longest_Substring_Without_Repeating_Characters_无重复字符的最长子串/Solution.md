# [3. 无重复字符的最长子串](https://leetcode.cn/problems/longest-substring-without-repeating-characters/)

## 1. 题目剖析与核心思路
* **问题模型**：在给定字符串中寻找一个最长的连续子串，要求该子串内没有任何重复字符。
* **核心算法：滑动窗口 (Sliding Window) + 哈希映射**。
    * 维护一个窗口 `[left, right]`，`right` 指针主动向右扩张，探索未知字符。
    * 使用哈希结构记录每个字符**最后一次出现的索引**。
    * 当 `right` 遇到一个在哈希结构中已存在的字符时，说明窗口内出现了重复字符。此时必须收缩左边界 `left`。
* **🚨 避坑指南（"abba" 陷阱）**：
    * 当遇到重复字符时，`left` 指针只能向前（右）走，**绝不能向后倒退**！
    * 状态转移方程必须是：`left = Math.max(left, map.get(currentChar) + 1);` 

## 2. 复杂度分析
* **时间复杂度**：`O(n)`。利用字符索引进行跳跃式滑动，左右指针最多各自遍历字符串一次。
* **空间复杂度**：`O(1)`。由于字符集仅限 ASCII 表（128 个字符），固定长度的数组占用常数级别内存。

## 3. 代码演进实录

### 🟢 V1.0 基础稳妥版 (基于 HashMap)
面试中最容易理解并写出的版本，利用 HashMap 记录字符与索引的映射。
```java
import java.util.HashMap;

public class Solution1 {
    public int lengthOfLongestSubstring(String s) {
        int left = 0, right = 0, maxSubLength = 0;
        HashMap<Character, Integer> charIndexMap = new HashMap<>();
        
        while (right < s.length()) {
            char currentChar = s.charAt(right);
            // 核心防御：遇到重复字符，利用 Math.max 确保 left 不回退
            if (charIndexMap.containsKey(currentChar)) {
                left = Math.max(left, charIndexMap.get(currentChar) + 1);
            }
            charIndexMap.put(currentChar, right);
            maxSubLength = Math.max(maxSubLength, right - left + 1);
            right++;
        }
        return maxSubLength;
    }
}

```

### 🔴 V2.0 极限压榨版 (1ms 极速版 - 面试加分项)

在大规模调用时，HashMap 的自动装箱/拆箱和哈希冲突会带来性能损耗。直接使用 128 长度的 `int[]` 模拟完美哈希，实现降维打击。

```java
public class Solution2 {
    public int lengthOfLongestSubstring(String s) {
        // 使用定长数组替代 HashMap，消除对象创建和 GC 压力
        int[] charIndexMap = new int[128];
        java.util.Arrays.fill(charIndexMap, -1);
        
        int left = 0, maxSubLength = 0, length = s.length();
        
        for (int right = 0; right < length; right++) {
            char currentChar = s.charAt(right);
            
            // 如果字符出现过，且在当前窗口内，则收缩左边界
            if (charIndexMap[currentChar] >= left) {
                left = charIndexMap[currentChar] + 1;
            }
            
            charIndexMap[currentChar] = right;
            
            int currentLength = right - left + 1;
            if (currentLength > maxSubLength) {
                maxSubLength = currentLength;
            }
        }
        return maxSubLength;
    }
}

```

## 4. 大厂真实工程应用场景 (Follow-up)

在高级研发岗面试中，滑动窗口的思想经常被平移到极其核心的架构场景中：

1. **API 网关滑动窗口限流**：限制相同 IP 在连续 $N$ 秒内的请求次数。遇到新请求入窗，过期时间戳出窗。
2. **分布式监控告警收敛**：在固定时间窗口内，拦截并过滤相同的报错堆栈，防止重复告警风暴。
3. **TCP 拥塞控制与乱序重排**：底层网络协议中极其经典的接收端窗口（Receive Window），确保按序将正确的数据包提交给应用层。

