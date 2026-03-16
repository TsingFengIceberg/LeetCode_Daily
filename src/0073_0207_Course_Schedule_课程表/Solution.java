import java.util.List;
import java.util.ArrayList;

class Solution {
    // 定义我们刚刚说好的三种状态
    // 0 = 未访问, 1 = 访问中(在当前路径上), 2 = 已安全(无环)

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 1. 初始化邻接表 (List<Integer>[]) 
        List<Integer>[] graph = new List[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }

        // 2. 初始化 visited 数组
        int[] visited = new int[numCourses];

        // 3. 遍历 prerequisites，填充邻接表 (注意谁指向谁：[ai, bi] 是 bi -> ai)
        for (int[] prerequisite : prerequisites) {
            int ai = prerequisite[0];
            int bi = prerequisite[1];
            graph[bi].add(ai);
        }

        // 4. 遍历每一门课，如果还没访问过 (visited[i] == 0)，就丢进 DFS 去查环
        //    如果某次 DFS 发现了环，直接 return false;
        for (int i = 0; i < numCourses; i++) {
            if (visited[i] == 0) {
                if (hasCycle(graph, visited, i)) {
                    return false; // 发现环了
                }
            }
        }
        // 5. 如果所有的课都查过了没环，return true;
        return true;
    }

    // 辅助函数：深度优先搜索找环
    private boolean hasCycle(List<Integer>[] graph, int[] visited, int current) {
        // 1. 标记当前节点为访问中 (1)
        visited[current] = 1;
        // 2. 遍历当前节点的所有邻居
        for (int neighbor : graph[current]) {
            // 3. 如果邻居还没访问过，递归 DFS 看看邻居有没有环
            if (visited[neighbor] == 0) {
                if (hasCycle(graph, visited, neighbor)) {
                    return true; // 发现环了
                }
            }
            // 4. 如果邻居正在访问中，说明我们又回到了这个节点，发现了环
            else if (visited[neighbor] == 1) {
                return true; // 发现环了
            }
        }
        // 5. 如果遍历完所有邻居都没发现环，标记当前节点为已安全 (2)
        visited[current] = 2;
        return false;
    }
}