# [0207. 课程表 (Course Schedule)]

## 💡 破除直觉陷阱：邻接矩阵与单状态布尔数组的死穴
在最初面对这道图论题时，最容易踩进两个极其致命的工程陷阱：
1. **拒用边集列表裸奔**：题目给出的 `prerequisites` 是一个边集列表。如果直接在上面跑 DFS，每次查找后继节点都需要 $O(E)$ 的时间，整体复杂度会急剧劣化为 $O(V \times E)$，导致必定 TLE（超时）。必须老老实实花时间将其转化为**邻接表 (Adjacency List)**，实现 $O(1)$ 的后继节点查找。
2. **警惕单状态 `boolean[] visited` 误判**：在有向图中检测环，单纯的 `true/false` 会导致严重误判（例如 $A \to B$, $A \to C$, $B \to C$ 的无环合规路径会被误报为有环）。

## 🧠 大厂标准解法：三色标记法与 $O(V+E)$ 极限推演
为了精准检测有向图中的环，我们引入了经典的**三色标记法（三状态状态机）**：
* **状态 `0`（未访问）**：节点尚未被触碰。
* **状态 `1`（访问中）**：节点正在当前 DFS 的调用栈/路径上。**如果在深搜时碰到了状态为 1 的节点，说明我们绕回了老路，这是唯一的“发现环”的铁证！**
* **状态 `2`（已安全）**：节点及其所有下游节点均已完成 DFS 且确认无环。后续从其他路径再走到该节点时，直接放行，实施极速剪枝。
**复杂度分析**：
* **时间复杂度**：建图耗时 $O(E)$；DFS 遍历时，由于状态 `2` 的剪枝存在，每个顶点 $V$ 最多被访问一次，每条边 $E$ 最多被遍历一次，总时间复杂度严格控制在 $O(V + E)$。
* **空间复杂度**：邻接表占用 $O(V + E)$，状态数组和系统递归调用栈（最坏情况下成链）占用 $O(V)$，总空间复杂度为 $O(V + E)$。

## 🏭 工业界真实工程实例：框架与中台的“防雪崩”基石
在高级软件工程中，有向无环图（DAG）的环检测是极其核心的底层基础设施：
1. **Spring 框架启动时的“循环依赖”拦截**：当业务代码出现 `OrderService` 和 `PaymentService` 互相 `@Autowired` 时，为了防止 JVM 栈溢出死机，Spring 底层维护了一个 `singletonsCurrentlyInCreation` 集合（本质就是我们的 `visited[current] = 1`）。一旦发现要创建的 Bean 已经在创建中了，立刻抛出异常终止启动，这正是三色标记法的工业级微缩版。
2. **大数据中台的离线任务调度（Airflow / DataWorks）**：在千万级数据清洗任务中，如果用户将任务流配置出了死循环（如 A $\to$ B $\to$ C $\to$ A），会导致整个计算集群死锁挂起。调度引擎在接受发布前，必先将其抽象为图，在主节点跑一遍环检测（DFS 或拓扑排序）。一旦检测到环则直接拒绝发布，从源头掐断集群雪崩的可能。

## 最终 Java 代码实现
```java
import java.util.List;
import java.util.ArrayList;

class Solution {
    // 核心：三色标记法状态定义
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

        // 4. 遍历每一门课，作为起点进行 DFS 查环
        for (int i = 0; i < numCourses; i++) {
            // 只有未被访问过的节点才需要启动 DFS
            if (visited[i] == 0) {
                if (hasCycle(graph, visited, i)) {
                    return false; // 一旦发现死循环，直接宣告无法毕业
                }
            }
        }
        // 5. 如果所有的课都查过了且没环，说明存在合法的拓扑排序，可以毕业
        return true;
    }

    // 辅助函数：深度优先搜索找环
    private boolean hasCycle(List<Integer>[] graph, int[] visited, int current) {
        // 1. 将当前节点压入调用栈，标记为“访问中 (1)”
        visited[current] = 1;
        
        // 2. 遍历当前节点的所有邻居（后置课程）
        for (int neighbor : graph[current]) {
            // 3. 如果邻居还没访问过，顺藤摸瓜继续 DFS
            if (visited[neighbor] == 0) {
                if (hasCycle(graph, visited, neighbor)) {
                    return true; // 下游发现了环，逐层向上汇报
                }
            }
            // 4. 【核心撞车点】如果邻居正在访问中，说明绕了一圈又撞见了自己人，确诊有环！
            else if (visited[neighbor] == 1) {
                return true; 
            }
            // 注意：如果 visited[neighbor] == 2，说明那条分支早就排查过且安全了，直接无视即可（剪枝）
        }
        
        // 5. 当前节点的所有出边都安全排查完毕，标记为“已安全 (2)”并出栈
        visited[current] = 2;
        return false;
    }
}
