import java.util.HashMap;
import java.util.PriorityQueue;

class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // 1. 完美的第一步：用 HashMap 统计所有数字的出现频次
        HashMap<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // 2. 修复点一：给小顶堆注入“自定义裁判”
        // (a, b) 代表堆里的任意两个数字。
        // frequencyMap.get(a) - frequencyMap.get(b) 意思是：比较它们的频次！频次低的排在堆顶（准备被踢）。
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            (a, b) -> frequencyMap.get(a) - frequencyMap.get(b)
        );
        
        // 3. 完美的末位淘汰逻辑：遍历所有不重复的数字 (keySet)
        for (int key : frequencyMap.keySet()) {
            minHeap.offer(key); // 把数字塞进堆里
            // 如果堆里的人数超标了 (大于 k)，立刻触发末位淘汰！
            if (minHeap.size() > k) {
                minHeap.poll(); // poll() 会无情地踢走堆顶那个频次最低的数字
            }
        }
        
        // 4. 修复点二：把留在堆里的 k 个精英，打包成数组返回
        int[] res = new int[k];
        for (int i = 0; i < k; i++) {
            res[i] = minHeap.poll(); // 把堆里的精英一个个拔出来放进数组
        }
        
        return res;
    }
}