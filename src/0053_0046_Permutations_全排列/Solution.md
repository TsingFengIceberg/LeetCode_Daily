# [0046. 全排列 (Permutations)]

## 💡 算法灵魂：回溯 (Backtracking) 的对称美学
“全排列”问题是大厂考察回溯算法的**绝对试金石**。它的本质是在一棵 N 叉决策树上进行深度优先搜索（DFS）。
* **核心动作闭环**：在决策树的每一层，我们面临多个候选分支。回溯的精髓在于**“做选择 -> 深入下一层 -> 撤销选择（恢复现场）”**。
* **为什么要恢复现场？** 因为我们在代码中复用的是同一个 `path` 列表和同一个 `used` 状态数组。如果走完一条路不把刚才加进去的元素踢出来，下一条路就会被上一条路的数据严重污染。这种“悔棋”操作正是回溯的核心。

## ☕ 大厂高频防坑指南：Java 引用陷阱与极致状态压缩
1. **致命的 Java 浅拷贝陷阱**：
   当 `path` 收集满一个排列时，绝不能直接 `res.add(path)`！因为加入结果集的是 `path` 的内存引用（指针），随着后续的回溯撤销操作，这个列表最终会被清空，导致结果集里全是一堆空数组。
   **大厂标准解药**：必须拍一张“快照”（深拷贝），即 `res.add(new ArrayList<>(path))`。
2. **极速状态记录**：
   对于“判断元素是否已被使用”这一需求，避免使用笨重的 `HashSet`。采用长度为 N 的 `boolean[] used` 数组，将判断和修改全部降维至绝对的 $O(1)$ 常数时间，且内存地址连续，极大提升 CPU 缓存命中率。

## 🏢 工业界真实工程实例：规则引擎与最优化搜索
回溯算法绝不仅仅是排数字的游戏，在阿里、美团等大厂架构中它是解决组合爆炸的最优解搜索器：
1. **电商优惠券叠加引擎**：双十一购物车勾选多张优惠券时，不同的扣减顺序会影响最终价格。底层通过类似全排列的回溯算法，模拟全部计算路径，配合风控剪枝，找出对用户最划算（或防资损）的扣减方案。
2. **关系型数据库的 CBO (基于成本的优化器)**：当一条复杂 SQL 需要 `JOIN` 5 张大表时，不同的关联顺序耗时天差地别。MySQL/TiDB 的优化器会在底层对表进行全排列枚举（回溯），估算每种排列的 CPU 和 IO 成本，从而生成最优执行计划。

## 🧮 复杂度剖析
* **时间复杂度：$O(N \times N!)$**。全排列的叶子节点总数为 $N!$ 个。对于每一个排列，我们需要花费 $O(N)$ 的时间将其深拷贝到结果集中。
* **空间复杂度：$O(N)$**。主要开销在于递归的调用栈深度（最大为 $N$），以及用于记录状态的 `path` 列表和 `used` 数组（长度均为 $N$）。返回所需的存储结果集通常不计入算法自身的空间复杂度。

## 💻 最终 Java 代码实现

```java
import java.util.List;
import java.util.ArrayList;

class Solution {
    // res 是全局的最终结果集
    List<List<Integer>> res = new ArrayList<>();
    
    public List<List<Integer>> permute(int[] nums) {
        // 初始状态：空路径，全为 false 的 used 数组
        backtrack(nums, new ArrayList<>(), new boolean[nums.length]);
        return res;
    }

    // path 是当前正在走的路径，used 记录元素是否被使用
    private void backtrack(int[] nums, List<Integer> path, boolean[] used) {
        // 1. 【触发结束条件】
        // 如果 path 里面装满数字了（长度等于 nums 的长度）
        if (path.size() == nums.length) {
            // 🚨 核心陷阱防御：深拷贝（拍快照）
            res.add(new ArrayList<>(path));
            return;
        }

        // 2. 【遍历所有候选节点】
        for (int i = 0; i < nums.length; i++) {
            // 剪枝：如果当前数字已经被用过了，直接跳过它
            if (used[i]) {
                continue;
            }
            
            // --- 核心动作 1：做选择 ---
            used[i] = true;         // 标记为已使用
            path.add(nums[i]);      // 将数字加入当前路径
            
            // --- 进入下一层决策树 ---
            backtrack(nums, path, used);
            
            // --- 核心动作 2：撤销选择 (回溯 / 悔棋) ---
            used[i] = false;              // 恢复为“未使用”状态
            path.remove(path.size() - 1); // 将数字从路径末尾弹出，恢复案发现场
        }
    }
}
