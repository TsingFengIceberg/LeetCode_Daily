class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int left = 0;
        int right = matrix.length * matrix[0].length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int mid_i = mid / matrix[0].length;
            int mid_j = mid % matrix[0].length;
            if (matrix[mid_i][mid_j] == target) {
                return true;
            } else if (matrix[mid_i][mid_j] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return false;
    }
}