# [0347. 前 K 个高频元素 (Top K Frequent Elements)]

## 💡 算法灵魂：海量热榜的底层基石 —— K 容量小顶堆
面对“必须优于 $O(N \log N)$”的进阶要求，直接全量排序的思路会被当场淘汰。大厂的标准破局点在于：**我们只需要前 K 个精英，剩下的数据根本不需要排序！**



1. **频次统计**：首先遍历一遍数组，利用 `HashMap` 统计每个数字出现的频次。
2. **容量锁定 (核心)**：建立一个容量严格限制为 $K$ 的**小顶堆 (Min-Heap)**。注意，这里的比较规则不是比较数字本身的大小，而是比较**数字在 Map 中的出现频次**。频次最低的永远垫底排在堆顶。
3. **末位淘汰制**：遍历所有不同的数字，将它们塞入小顶堆。一旦堆的容量超过 $K$，立刻触发 `poll()` 操作，将堆顶那个频次最少、最没有资格留在榜单上的元素一脚踢出堆。遍历结束后，稳稳留在堆里的 $K$ 个元素，就是绝对的王者！

## 🚀 架构师深潜：17ms vs 1ms 的 Trade-off (取舍)
本次提交耗时 17ms（击败 15.09%）。排在前面 1ms 的解法，使用的是**桶排序 (Bucket Sort)**：以频率作为数组下标，将数字丢进对应的桶里，然后从后往前倒序遍历收集。
* **桶排序的局限**：桶排序是严格的 $O(N)$，速度极快。但在真实业务中，它要求你必须**全量掌握**所有数据才能建桶，极耗内存，且无法处理实时数据流。
* **小顶堆的统治力**：虽然小顶堆需要频繁进行对象装箱拆箱和堆调整操作（导致耗时上升至十几毫秒），但在真实的**流式计算 (Streaming Data)** 中，你的堆永远只占用 $K$ 个大小的极小内存。即使面对百亿级的数据流，它依然能从容不迫地实时输出 Top K，这才是架构设计的真正魅力。

## 🏢 工业界真实工程实例：Heavy Hitters (海量热点发现)
这种“频次统计 + 小顶堆”的模型，是当今所有主流互联网公司的底层基石：
1. **微博/抖音的实时热搜榜**：1 分钟内产生 10 亿次搜索，后端在 Redis 或 Flink 中利用类似小顶堆的逻辑，极速淘汰千万个只搜过几次的冷门词，用仅仅几十个元素的微小内存，实时维持 Top 50 的热搜榜单。
2. **阿里云 DDoS 攻击实时防御系统**：每秒涌入上千万 HTTP 请求，安全网关边统计边往容量为 10 的小顶堆里送。几秒钟内，小顶堆里留下的就是频发发包的 Top 10 恶意肉鸡 IP，防火墙直接拉黑阻断。
3. **极简扩容方案**：当数据大到连单机 `HashMap` 都存不下时，大厂会利用分布式 MapReduce 打散数据求局部 Top K，或者使用 **Count-Min Sketch** 概率算法在极小内存下完成百亿级频次估算，再配合小顶堆完成终极输出。

## 🧮 复杂度剖析
* **时间复杂度：$O(N \log K)$**。统计频次耗时 $O(N)$。遍历 Map 将不重复的元素压入和弹出堆，由于堆的大小严格限制为 $K$，每次堆操作的时间复杂度为 $O(\log K)$，整体堆操作耗时 $O(N \log K)$。完美优于 $O(N \log N)$ 的进阶要求。
* **空间复杂度：$O(N)$**。主要开销在于存储所有不重复元素的 `HashMap`。而堆的空间仅仅占用了 $O(K)$。

## 💻 最终 Java 代码实现 (大厂原生小顶堆 API 修复版)

```java
import java.util.HashMap;
import java.util.PriorityQueue;

class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // 1. 频次统计图谱
        HashMap<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // 2. 注入“自定义裁判”的小顶堆
        // (a, b) -> frequencyMap.get(a) - frequencyMap.get(b) 
        // 意味着：对比两个数字的频次，频次低的永远漂浮在堆顶，准备被淘汰
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            (a, b) -> frequencyMap.get(a) - frequencyMap.get(b)
        );
        
        // 3. 残酷的末位淘汰赛
        for (int key : frequencyMap.keySet()) {
            minHeap.offer(key); 
            // 堆里的人数超过限额 k，立刻踢走堆顶频次最垫底的数字
            if (minHeap.size() > k) {
                minHeap.poll(); 
            }
        }
        
        // 4. 将留在堆里的 K 个精英打包成数组返回
        int[] res = new int[k];
        for (int i = 0; i < k; i++) {
            res[i] = minHeap.poll(); 
        }
        
        return res;
    }
}