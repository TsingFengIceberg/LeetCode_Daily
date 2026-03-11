class Solution {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;
        
        // 1. 外层雷达扫描：遍历整个二维网格
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 如果发现了新大陆 '1'
                if (grid[i][j] == '1') {
                    // 岛屿数量加 1
                    count++;
                    // 派谴登陆部队，去把这座岛连着的陆地全部“沉掉”！
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }
    
    // 2. 核心递归函数：沉岛战术
    private void dfs(char[][] grid, int r, int c) {
        // 【第一步：写递归的终止条件 (Base Case)】
        // 想一想：什么情况下，我们的登陆部队必须立刻撤退（return）？
        // 提示：越界了怎么办？遇到海水了怎么办？
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] == '0') {
            return; // 立刻撤退
        }
        
        // 【第二步：执行沉岛】
        // 把当前这块陆地变成海水 '0' (或者你说的 '2')
        grid[r][c] = '0'; // 沉岛了！
        
        // 【第三步：四面八方蔓延】
        // 向 上、下、左、右 四个方向继续发起 DFS 进攻
        dfs(grid, r - 1, c); // 向上
        dfs(grid, r + 1, c); // 向下
        dfs(grid, r, c - 1); // 向左
        dfs(grid, r, c + 1); // 向右
    }
}