# [0131. 分割回文串 (Palindrome Partitioning)]

## 💡 核心底层架构：回溯算法（DFS + 剪枝）的“切西瓜”模型
面对“求所有可能的组合/分割方案”且数据规模极小（如 $N \le 16$）的题目，大厂的标准解法毫无疑问是**回溯算法（Backtracking）**。
我们把分割字符串具象化为“切西瓜”：这把刀从左向右试探性地切。每一次切分都会截取出一个前缀子串，如果这个子串合法（是回文串），我们就把它装进篮子（加入 `path`），然后继续递归去切剩下的部分；如果这个子串不合法，我们就直接**剪枝（Pruning）**，不再深入，尝试换个位置切。这种“横向遍历（for 循环尝试切割点），纵向递归（dfs 深入剩下的字符串）”构成了回溯的核心骨架。

## 🚨 踩坑记录与 Java 工程化避坑指南
在手写回溯模板时，我们排查出了几个极其容易在白板面试中让你“一铁锹挖断大动脉”的致命点：

1. **集合引用的深拷贝（对象快照）陷阱**
   当 `startIndex == s.length()` 触发 Base Case 时，绝不能写 `res.add(path)`！因为 `path` 在 Java 中是一个对象引用，后续回溯撤销选择时会把它清空，导致最后 `res` 里装的全是空列表。正确的工程写法是利用构造函数做一次快照深拷贝：`res.add(new ArrayList<>(path))`。
2. **死循环噩梦：递归起点的参数传递**
   在截取了 `s[startIndex...i]` 之后，下一次递归去切剩下的字符串时，新的起点**必须是 `i + 1`**，绝不能是 `i`！如果写成 `dfs(s, i)`，会导致程序永远在原地切同一个字符，引发无限死循环直到栈溢出（StackOverflowError）。
3. **Java `String` API 盲区**
   在 Java 中操作字符串绝不能像 C++ 那样用 `s[left]` 取值。在双指针判断回文串时，必须老老实实调用 `s.charAt(index)` 来对比字符。
4. **时间复杂度的指数级膨胀**
   在最坏情况下（例如字符串全为同一字符 `aaaaa`），$N-1$ 个字符间隙每个位置都有“切”与“不切”两种状态，加上拼接字符串的 $O(N)$ 耗时，极限时间复杂度高达 **$O(N \cdot 2^N)$**。这也是为什么题目严格限制 $N \le 16$ 的原因。

## 🏭 工业界真实场景：披着算法外衣的大厂底层核心
在真实的工业界，大厂不会让你切回文串，但这套**“基于合法性校验的字符串回溯分割”**骨架，却是两大核心中间件的基石：
1. **搜索引擎与 NLP 中文分词引擎**：用户输入“北京大学生”，分词引擎底层用的就是这套回溯代码。只不过把判断回文的 `isPalindrome()` 替换成了判断是否在词典中的 `isInDictionary()`。如果切出来的词不合法就回溯剪枝，最终找出所有合法的中文切分组合，再交给隐马尔可夫模型（HMM）去选出最高概率的正确语义。
2. **数据库内核（如 MySQL/TiDB）的基于成本优化器 (CBO)**：当出现多张表的大型 `JOIN` 查询时，可能的连表顺序是阶乘级的。优化器正是利用**带剪枝的 DFS 回溯搜索**来探索最优执行计划。如果在某一层递归探查时发现当前分支的预估扫描成本已经过高，优化器会立刻“剪枝（continue）”，回溯并去尝试下一种连表顺序。

## 💻 最终 Java 代码实现
> *注：本次提交执行用时 7ms（击败 99.95%），内存消耗 64.20MB（击败 40.09%）。逻辑严密，剪枝果断，完美契合回溯模板，是一份高质量的大厂通关代码。*

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    // 全局变量：存放最终所有合法分割方案的集合
    List<List<String>> res = new ArrayList<>();
    // 全局变量：存放当前正在尝试的、从根节点到当前节点的“切割路径”
    List<String> path = new ArrayList<>();

    public List<List<String>> partition(String s) {
        // 触发回溯搜索，最开始这把刀停在索引 0 的位置
        dfs(s, 0);
        return res;
    }

    /**
     * 核心回溯逻辑
     * @param s 原始字符串
     * @param startIndex 我们当前这把刀要从哪个位置开始切
     */
    private void dfs(String s, int startIndex) {
        // 1. Base Case (终止条件)：刀切到了字符串末尾
        if (startIndex == s.length()) {
            // 必须 new 一个新的 ArrayList 做深拷贝快照，防止后续 path 的变动影响 res
            res.add(new ArrayList<>(path));
            return;
        }

        // 2. 单层搜索逻辑（横向遍历所有可能的切割线位置 i）
        for (int i = startIndex; i < s.length(); i++) {
            // 判断当前截取出来的子串 s[startIndex...i] 是否是回文串
            if (isPalindrome(s, startIndex, i)) {
                // 如果是，把它截取出来，加入当前路径 path
                String substring = s.substring(startIndex, i + 1);
                path.add(substring);
            } else {
                // 如果不是，直接剪枝，连递归都不用进，尝试下一个切割位置
                continue;
            }

            // 递归：基于当前合法的切割，去切剩下的那部分字符串
            // ⚠️ 极其关键：下一次切割的起点必须是 i + 1，否则会无限死循环！
            dfs(s, i + 1);

            // 撤销选择（回溯的核心逻辑）
            // 移除 path 中的最后一个元素，退回上一步的状态，以便尝试其他切割方案
            path.removeLast(); // 注意：低版本 Java 需替换为 path.remove(path.size() - 1);
        }
    }

    /**
     * 辅助 API：使用双指针，判断字符串 s 在闭区间 [left, right] 内是否是回文串
     */
    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            // Java 必须使用 charAt API 来获取对应索引的字符
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            // 指针向中间逼近
            left++;
            right--;
        }
        return true; 
    }
}