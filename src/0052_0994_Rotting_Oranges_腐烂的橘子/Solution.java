import java.util.Queue;
import java.util.LinkedList;

class Solution {
    public int orangesRotting(int[][] grid) {
        // count 用于记录【新鲜橘子】的总数，也是我们 O(1) 终局审判的核心
        int count = 0;
        // time 用于记录耗费的分钟数，即 BFS 扩散的层数
        int time = 0;
        // queueSize 用于在 while 循环中控制按层（按分钟）遍历
        int queueSize = 0;
        // 核心数据结构：队列，用于存储【当前正在腐烂的橘子坐标】
        Queue<int[]> queue = new LinkedList<>();
        
        // 1. 【初始化阶段】：双重循环扫一遍网格，寻找超级源点
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    // 顺手统计新鲜橘子的数量
                    count++;
                }
                if (grid[i][j] == 2) {
                    // 发现初始的烂橘子！将它们全部入队，作为多源 BFS 的第一层“火种”
                    queue.offer(new int[]{i, j});
                }
            }
        }
        
        // 2. 【多源 BFS 扩散阶段】
        // 核心剪枝：不仅要求队列不为空，还要求新鲜橘子数 count > 0。
        // 如果 count 已经为 0，说明所有橘子都烂了，直接提前结束，避免 time 多加 1 分钟！
        while (!queue.isEmpty() && count > 0) {
            // 拿到当前队列的大小，这代表了【当前这一分钟】有多少烂橘子准备向外放毒
            queueSize = queue.size();
            
            // 必须严格使用初始的 queueSize 进行循环，保证只处理当前层的橘子
            for (int i = 0; i < queueSize; i++) {
                // 弹出一个当前层的烂橘子
                int[] cell = queue.poll();
                int x = cell[0];
                int y = cell[1];
                
                // --- 开始向四周扩散，并执行“原地修改” ---
                
                // 向上扩散：如果不越界，且上面是个新鲜橘子
                if (x > 0 && grid[x - 1][y] == 1) {
                    grid[x - 1][y] = 2; // 立刻让它腐烂（原地修改状态，防止重复入队）
                    queue.offer(new int[]{x - 1, y}); // 把新腐烂的橘子推入队列，参与下一分钟的扩散
                    count--; // 新鲜橘子数减 1
                }
                // 向下扩散
                if (x < grid.length - 1 && grid[x + 1][y] == 1) {
                    grid[x + 1][y] = 2;
                    queue.offer(new int[]{x + 1, y});
                    count--;
                }
                // 向左扩散
                if (y > 0 && grid[x][y - 1] == 1) {
                    grid[x][y - 1] = 2;
                    queue.offer(new int[]{x, y - 1});
                    count--;
                }
                // 向右扩散
                if (y < grid[0].length - 1 && grid[x][y + 1] == 1) {
                    grid[x][y + 1] = 2;
                    queue.offer(new int[]{x, y + 1});
                    count--;
                }
            }
            // 当前这批烂橘子全部向四周毒害了一圈，时间过去 1 分钟
            time++;
        }
        
        // 3. 【终局审判】
        // 如果 count == 0，说明成功感染所有橘子，返回耗时 time。
        // 如果 count 还不为 0，说明有橘子处于绝对安全岛中，永远烂不掉，返回 -1。
        return count == 0 ? time : -1;
    }
}