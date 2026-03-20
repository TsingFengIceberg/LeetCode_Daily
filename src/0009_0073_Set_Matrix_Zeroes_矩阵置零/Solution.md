# 📘 LeetCode 73. 矩阵置零 (Set Matrix Zeroes) - 面试复盘笔记

## 1. 题目核心痛点与思维误区
* **直觉陷阱**：遇到 0 就立刻把整行整列变成 0。这会产生“污染”，导致原本不是 0 的元素也被波及，最终整个矩阵全变 0。
* **空间复杂度的演进**：
    * **$O(mn)$**：深拷贝整个矩阵（极差）。
    * **$O(m+n)$**：开辟两个一维布尔数组 `row[]` 和 `col[]` 记录哪一行/哪一列需要置零（普通解法）。
    * **$O(1)$**：借用矩阵自身的第一行和第一列作为“备忘录”（大厂核心考点）。

## 2. 核心状态转移逻辑：O(1) 空间四步走战略
为了实现 $O(1)$ 空间，我们将矩阵的 `matrix[0][..]` 和 `matrix[..][0]` 作为标记位。


* **致命踩坑点（行列倒置）**：在遍历第一行时，变动的是列指针（长度 $n$）；遍历第一列时，变动的是行指针（长度 $m$）。千万不能搞混，否则必定触发 `ArrayIndexOutOfBoundsException`！

* **标准四步走**：
    1. **设保镖**：用 `row0HasZero` 和 `col0HasZero` 两个廉价的 `boolean` 变量，提前记录第一行和第一列**原本**是否包含 0。
    2. **打草稿**：从 `i=1, j=1` 开始遍历矩阵，如果遇到 `matrix[i][j] == 0`，则将同行行首 `matrix[i][0]` 和同列列首 `matrix[0][j]` 标记为 0。
    3. **落笔修改**：再次从 `i=1, j=1` 开始遍历，若行首或列首为 0，则将 `matrix[i][j]` 设为 0。
    4. **过河拆桥**：根据第一步的两个保镖变量，决定是否要把整条第一行/第一列全部置 0。

## 3. Java 核心代码实现 (O(1) 终极版)
```java
public void setZeroes(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return;
    int m = matrix.length, n = matrix[0].length;
    boolean row0HasZero = false, col0HasZero = false;

    // 1. 记录第一行和第一列的原始状态
    for (int j = 0; j < n; j++) {
        if (matrix[0][j] == 0) { row0HasZero = true; break; }
    }
    for (int i = 0; i < m; i++) {
        if (matrix[i][0] == 0) { col0HasZero = true; break; }
    }

    // 2. 利用第一行和第一列作为标记空间 (跳过第0行/列)
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            if (matrix[i][j] == 0) {
                matrix[i][0] = 0;
                matrix[0][j] = 0;
            }
        }
    }

    // 3. 根据标记位，置零内部矩阵 (跳过第0行/列)
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                matrix[i][j] = 0;
            }
        }
    }

    // 4. 单独处理第一行和第一列
    if (row0HasZero) {
        for (int j = 0; j < n; j++) matrix[0][j] = 0;
    }
    if (col0HasZero) {
        for (int i = 0; i < m; i++) matrix[i][0] = 0;
    }
}

```

## 4. 大厂真实业务映射与变形 (Follow-ups)

* **工程场景**：超大在线电子表格（飞书/钉钉）的局部状态批量刷新、游戏引擎二维网格中十字形爆炸区域的判定、机房断电保护的行列级联触发。
* **高频变种题**：
* **LeetCode 289. 生命游戏**：也是矩阵原地修改，但状态更复杂，需要引入“复合状态机”（如：用数字 2 代表“由生转死”）。

