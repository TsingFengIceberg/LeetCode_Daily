# [0034. 在排序数组中查找元素的第一个和最后一个位置 (Find First and Last Position of Element in Sorted Array)]

## 💡 算法灵魂：Fail-Fast 机制与三段式二分搜索
面对数组中存在大量重复元素的情况，普通的二分查找在踩中目标值时会直接停下，无法获取左右边界。
如果使用 `while` 循环向两边扩散查找，时间复杂度会瞬间退化为 $O(n)$。因此，大厂的标准解法是：**踩中目标值时，不要停，继续强行逼迫边界收缩！**



本代码采用了一种极具工程优化思维的**“三段式二分法”**：
1. **破局初筛 (Fail-Fast 机制)**：先用一次标准的二分查找，在全局寻找**任意一个** `target`。如果找不到，直接拦截并返回 `[-1, -1]`，极其干脆地拒绝后续两段无效的边界计算，极大节省了系统资源。
2. **极速压缩空间 (寻左边界)**：既然找到了任意一个 `target`（记为 `mid`），那么**最左边界一定在 `[0, mid]` 之间**。我们在这段极小的区间内继续二分，只要踩中目标值，就强行令 `right = leftMid - 1`，不断向左“得寸进尺”。
3. **极速压缩空间 (寻右边界)**：同理，**最右边界一定在 `[mid, nums.length - 1]` 之间**。在这段区间内二分，只要踩中目标值，就强行令 `left = rightMid + 1`，不断向右“得寸进尺”。

## 🏢 工业界真实工程实例：海量数据的区间锁定 (Range Query)
在海量有序、且存在大量重复元素的数据集中精准锁定连续区间，是所有存储系统的底层基础：
1. **MySQL 非唯一索引范围检索**：在 B+ 树的叶子节点中，非唯一索引（如用户 ID）会连续出现。引擎利用此算法极速锁定某用户第一笔和最后一笔订单的物理内存偏移量，然后直接执行一次**连续的磁盘顺序读 (Sequential Read)**，瞬间拉取千百条记录，避免磁盘 I/O 爆炸。
2. **Kafka / 飞书日志系统的高并发瞬时截取**：双十一零点，同一毫秒涌入上万条异常日志。日志系统以此算法寻找目标时间戳的左右边界，瞬间圈定这上万条打包在一起的数据，利用零拷贝技术推送到前端。
3. **防御性编程的本质**：代码中的第一段 `while` 实现了“快速失败”。在真实的分布式系统中，**最耗费资源的往往是注定失败的恶意查询**。在最外层用极低成本的初筛将其拦截，是保护底层沉重搜索引擎的绝对防线。

## 🧮 复杂度剖析
* **时间复杂度：$O(\log n)$**。最坏情况下（整个数组全是 `target`），需要进行三次完整的二分查找，时间开销为 $O(3 \log n)$，即严格的 $O(\log n)$。但在绝大多数情况下，第一阶段会极快地将后续的搜索空间压缩到极小。
* **空间复杂度：$O(1)$**。全程只使用了几个整型指针变量，没有任何额外内存开辟。

## 💻 最终 Java 代码实现 (0ms 极致三段压缩版)

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        int leftBound = -1, rightBound = -1;
        int mid = 0, leftMid = 0, rightMid = 0;
        
        // 【第一阶段：破局点扫描 (Fail-Fast 机制)】
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                break; // 抓到一个 target，立刻停下！
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        // Fail-Fast：提早拦截，直接返回，拒绝后续无效计算
        if (left > right) {
            return new int[]{-1, -1};
        }
        
        // 【第二阶段：寻找左边界 (搜索空间被压缩至 [0, mid])】
        left = 0;
        right = mid - 1;
        leftBound = mid; 
        while (left <= right) {
            leftMid = left + (right - left) / 2;
            if (nums[leftMid] == target) {
                leftBound = leftMid;
                right = leftMid - 1; // 命中！继续向左“得寸进尺”
            } else {
                left = leftMid + 1;
            }
        }
        
        // 【第三阶段：寻找右边界 (搜索空间被压缩至 [mid, nums.length - 1])】
        left = mid + 1;
        right = nums.length - 1;
        rightBound = mid; 
        while (left <= right) {
            rightMid = left + (right - left) / 2;
            if (nums[rightMid] == target) {
                rightBound = rightMid;
                left = rightMid + 1; // 命中！继续向右“得寸进尺”
            } else {
                right = rightMid - 1;
            }
        }
        
        return new int[]{leftBound, rightBound};
    }
}
