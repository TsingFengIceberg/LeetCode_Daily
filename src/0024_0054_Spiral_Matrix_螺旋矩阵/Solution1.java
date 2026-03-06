import java.util.List;
import java.util.ArrayList;

class Solution1 {
    public List<Integer> spiralOrder(int[][] matrix) {
        // 1. 定义四面墙（边界）。分别代表上、下、左、右的当前可遍历范围
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        
        // 【面试官点评】：这里如果写成 new ArrayList<>(matrix.length * matrix[0].length) 会极其加分，避免扩容开销！
        List<Integer> result = new ArrayList<>();
        
        // 2. 核心大循环：只要上下墙没交错，且左右墙没交错，就继续“剥洋葱”
        while (top <= bottom && left <= right) {
            
            // 步骤一：向右走（贴着上墙 top 遍历）
            for (int i = left; i <= right; i++) {
                result.add(matrix[top][i]);
            }
            // 上墙往下压，剥离最上面一行
            top++;
            
            // 步骤二：向下走（贴着右墙 right 遍历）
            // 【自带防御】：如果刚才 top++ 导致 top > bottom，这里的 i <= bottom 就不成立，完美跳过
            for (int i = top; i <= bottom; i++) {
                result.add(matrix[i][right]);
            }
            // 右墙向左推，剥离最右侧一列
            right--;
            
            // 步骤三：向左走（贴着下墙 bottom 遍历）
            // 【防雷达】：因为向左走只看 left 和 right，不知道 top 和 bottom 是否已交错，所以必须加 if 判断防重复！
            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    result.add(matrix[bottom][i]);
                }
                // 下墙往上推，剥离最底下一行
                bottom--;
            }
            
            // 步骤四：向上走（贴着左墙 left 遍历）
            // 【防雷达】：同理，向上走只看 top 和 bottom，必须判断左右墙是否已交错！
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    result.add(matrix[i][left]);
                }
                // 左墙向右推，剥离最左侧一列
                left++;
            }
        }
        
        // 所有元素遍历完毕，返回结果
        return result;
    }
}