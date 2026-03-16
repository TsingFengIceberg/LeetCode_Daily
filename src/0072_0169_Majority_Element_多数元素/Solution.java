class Solution {
    public int majorityElement(int[] nums) {
        // 初始化第一个数字为“擂主”（boss），并给他分配 1 点血量（count）
        int boss = nums[0];
        int count = 1;
        
        // 因为 nums[0] 已经登台了，所以直接从索引 1 开始遍历，减少一次无意义的循环
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == boss) {
                // 遇到“自己人”（同国家的士兵），血量/阵地人数 +1
                count++;
            } else {
                // 遇到“敌人”，强行一换一同归于尽，血量 -1
                count--;
                // 如果同归于尽后，擂主的血量被耗尽归零，说明阵地暂时失守
                if (count == 0) {
                    // 刚刚把你干掉的那个“敌人”立刻占领阵地，成为新的擂主
                    boss = nums[i];
                    // 新擂主上台，初始血量恢复为 1
                    count = 1;
                }
            }
        }
        // 遍历结束，因为题目保证多数派人数 > n/2，所以最后死死守在阵地上的 boss 必定是多数派
        return boss;
    }
}