# [0017. 电话号码的字母组合 (Letter Combinations of a Phone Number)]

## 💡 算法灵魂：回溯三剑客之“多集合组合”
如果说“全排列”看重顺序，“子集”看重防倒车，那么这道题就是标准的**“在多个不同的集合中，各挑一个元素进行组合”**（数学上的笛卡尔积）。

我们通过三个极致的工程微操，构建了这台 0ms 的回溯引擎：
1. **$O(1)$ 查表法字典**：直接抛弃带有哈希计算与扩容开销的 `HashMap`，采用极度轻量级的 `String[]` 数组。利用 `char - '0'` 的 ASCII 码偏移量作为天然下标，实现绝对物理级别的 $O(1)$ 映射。
2. **深度指南针 `index`**：在多集合跳转中，我们需要一个指针来记录“当前正在拨弄第几个密码轮”。`index` 完美充当了向下递归的深度计，当 `index == digits.length()` 时，触发快照收集。
3. **零垃圾对象的 `StringBuilder`**：在极高频的“做选择（追加字符）”和“撤销选择（删除末尾字符）”操作中，坚决不使用 `String` 的 `+=` 拼接，避免了海量临时对象的生成与 GC 停顿。

## 🏢 工业界真实工程实例：笛卡尔积与状态生成
这套看似古老的“九宫格”拨号逻辑，在真实的工业架构中是处理“多维度条件交叉”的绝对核心引擎：
1. **Android/iOS 智能拨号盘与 T9 预测搜索**：当你在拨号盘输入 `5426` 时，底层跑的正是这套组合算法，瞬间生成几十种拼音可能。大厂为了做到毫秒级响应，还会结合 **Trie 树（字典树）** 进行极限剪枝——如果在树里发现当前的前缀根本不存在对应联系人，立刻 `break` 放弃该分支的后续回溯。
2. **电商中台的商品 SKU 自动生成器**：淘宝/京东商家在上架商品时，勾选了 3 种颜色、4 种尺码、2 种款式。电商后台点击“生成 SKU”的瞬间，跑的就是这套回溯模板。系统通过递归将不同的属性集合进行交叉碰撞，一行冗余代码都不写，自动把这 24 个变体（SKU）全部生成并写入数据库。

## 🧮 复杂度剖析
* **时间复杂度：$O(4^N \times N)$**。其中 $N$ 是电话号码的长度。因为数字 7 和 9 最多对应 4 个字母，所以决策树最多有 $4^N$ 个叶子节点（组合数）。每次拿到一个完整组合后，将其转化为 `String` 拍快照需要 $O(N)$ 的时间。
* **空间复杂度：$O(N)$**。抛开返回的结果集不谈，算法的额外空间开销主要来自于递归调用栈的最大深度 $N$，以及用于动态拼接字符串的 `StringBuilder`（长度最大为 $N$）。

## 💻 最终 Java 代码实现 (0ms 大厂查表法极速版)

```java
import java.util.List;
import java.util.ArrayList;

class Solution {
    // 【降维打击 1：查表法字典】
    // 放弃笨重的 HashMap，利用数组天然的连续下标进行绝对的 O(1) 映射。
    private String[] phoneMap = {
        "",     // 0 号位空着
        "",     // 1 号位空着
        "abc",  // 2 号位
        "def",  // 3 号位
        "ghi",  // 4 号位
        "jkl",  // 5 号位
        "mno",  // 6 号位
        "pqrs", // 7 号位
        "tuv",  // 8 号位
        "wxyz"  // 9 号位
    };
    
    // 全局结果集，档案室
    List<String> res = new ArrayList<>();
    
    public List<String> letterCombinations(String digits){
        // 🚨 边界防御：如果输入的电话号码是空的，直接返回空列表
        if (digits == null || digits.length() == 0) {
            return res;
        }
        
        // 启动回溯引擎：传入原始输入，当前拨号的深度 0，以及极其高效的拼接器
        backtrack(digits, 0, new StringBuilder());
        return res;
    }

    // 回溯核心引擎：
    // index: 当前正在处理 digits 里的第几个数字（控制深度的指南针）
    // path: 当前拼装出来的字母组合
    private void backtrack(String digits, int index, StringBuilder path) {
        // 【触发结束条件】
        if (path.length() == digits.length()) {
            // 【降维打击 2：快照收集】
            // 把 StringBuilder 转化为不可变的 String 对象，存入档案室
            res.add(path.toString());
            return;
        }
        
        // 【提取当前拨轮的数字】
        char s = digits.charAt(index);
        
        // 【横向遍历：当前拨轮上的所有字母】
        // s - '0'：利用 ASCII 码将字符 '2' 转换成真正的整数 2，取出 "abc" 并转为字符数组
        for (char c : phoneMap[s - '0'].toCharArray()) {
            // --- 核心动作 1：做选择 ---
            path.append(c);
            
            // --- 核心动作 2：递归进入下一层（下一个数字拨轮）---
            backtrack(digits, index + 1, path);
            
            // --- 核心动作 3：撤销选择 (回溯 / 悔棋) ---
            path.deleteCharAt(path.length() - 1);
        }
    }
}