import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();
            row.add(1); // First element of each row is always 1

            for (int j = 1; j < i; j++) {
                row.add(result.get(i - 1).get(j - 1) + result.get(i - 1).get(j)); // Each element is the sum of the two elements above it
            }
            if (i > 0) {
                row.add(1); // Last element of each row is always 1
            }
            result.add(row);
        }

        return result;
    }
}