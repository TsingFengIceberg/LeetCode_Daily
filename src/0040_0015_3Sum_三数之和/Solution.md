# 算法复盘笔记：LeetCode 15. 三数之和 (3Sum)


---

## 🚫 摸索与踩坑实录 (一字不落的思维轨迹)

### 1. 我的神级直觉 (剪枝利器)
**我的原话**：“我没看你的提示，我只想到第一步，就是先排序，然后就能划分为小于0和大于0的两部分”
**我的疑惑**：“那么我那个判断大于0的逻辑还需要吗”

**面试官点评**：不仅需要，而且是**神来之笔**！因为数组已经排好序，如果外层循环的“老大” `nums[i]` 已经 $> 0$，那他身后的所有数字必然全为正数。三个正数相加绝无可能等于 0。这行 `if (nums[i] > 0) break;` 极其优雅地阻断了无数次毫无意义的后续计算，是大厂非常看重的**性能剪枝**操作。

### 2. 我的初版代码与致命漏洞
**我的原代码 (一字不落)**：
```java
import java.util.ArrayList;
import java.util.Arrays;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            if (nums[i] > 0) {
                break;  
            }
            for (int j = i + 1, k = nums.length - 1; j < k; ) {
                if (nums[i] + nums[j] + nums[k] == 0) {
                    result.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    while (j < k && nums[j] == nums[j + 1]) {
                        j++;  
                    }
                    while (j < k && nums[k] == nums[k - 1]) {
                        k--;  
                    }
                    j++;
                    k--;
                } else if (nums[i] + nums[j] + nums[k] < 0) {
                    j++;
                } else {
                    k--;
                }
            }
        }
        return result;
    }
}

```

**我的提问**：“我的代码你觉得到底哪里出错了”
**核心 Bug 诊断**：内层双指针的 `j` 和 `k`（小弟）去重做得天衣无缝。但是**忘记了给外层循环的 `i`（老大）去重**！
例如数组 `[-1, -1, 0, 1, 2]`，当第一个 `-1` 找到组合 `[-1, 0, 1]` 后，循环走到第二个 `-1`，如果没有拦截，它会再次找出一模一样的 `[-1, 0, 1]`，导致结果包含重复三元组，被系统直接判死刑。

---

## 💡 终极解法：排序 + 双指针夹逼 (附大厂满分模板)

面对乱序数组，必须先建立秩序，然后使用**“定一移二”**的战术，并在每一层严格防范重复元素。

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 1. 建立秩序：排序是降维和去重的基础
        Arrays.sort(nums);
        
        // 2. 遍历“老大” nums[i]
        for (int i = 0; i < nums.length - 2; i++) {
            // 🌟 性能剪枝：如果老大已经大于0，后面不可能凑出0，直接结束战斗
            if (nums[i] > 0) {
                break;  
            }
            
            // 🎯 核心修复（老大去重）：如果和前一个老大长得一样，直接跳过，防止产生重复组合
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // 3. 双指针夹逼寻找两个“小弟”
            for (int j = i + 1, k = nums.length - 1; j < k; ) {
                int sum = nums[i] + nums[j] + nums[k];
                
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    
                    // 🎯 小弟去重：跳过连续相同的左小弟和右小弟
                    while (j < k && nums[j] == nums[j + 1]) j++;
                    while (j < k && nums[k] == nums[k - 1]) k--;
                    
                    // 左右指针同时向内收缩
                    j++;
                    k--;
                } else if (sum < 0) {
                    j++; // 整体太小，左小弟右移找更大的
                } else {
                    k--; // 整体太大，右小弟左移找更小的
                }
            }
        }
        return result;
    }
}

```

### 📊 复杂度分析

* **时间复杂度**: $O(N^2)$。数组排序的时间复杂度是 $O(N \log N)$。外层循环遍历一次为 $O(N)$，内部的双指针相向交替移动最多遍历整个剩余数组也是 $O(N)$，综合下来为 $O(N^2)$。极大优于暴力穷举的 $O(N^3)$。
* **空间复杂度**: $O(\log N)$ 到 $O(N)$。主要取决于排序算法底层的空间开销（Java 中对于基本类型通常是双轴快速排序，空间复杂度为 $O(\log N)$）。除了返回结果的集合外，只用了常数级别的指针变量。

---

## 🏗️ 真实大厂工程实例深度剖析

1. **支付宝/微信日终对账系统**：金融系统排查异常挂账时，需要在几万条正负抵消的流水中找出总和为 0 的特定组合来平账。暴力计算会严重超时。利用排序加双指针算法，能将 $O(N^3)$ 的指数级灾难降维到秒级出结果。去重逻辑更是保证了不会对同一笔款项进行多次错误平账。
2. **高频量化交易引擎 (Delta中性对冲)**：在寻找总风险敞口（Delta）加起来刚好等于 0 的三种金融衍生品组合时，由于必须在开盘前几毫秒内得出所有备选策略，底层算法完全依赖这套带极其严苛剪枝（提前 Break）的 $O(N^2)$ 搜索架构，以节省宝贵的算力。

