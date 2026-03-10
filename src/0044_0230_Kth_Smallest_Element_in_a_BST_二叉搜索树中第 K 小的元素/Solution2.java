class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

class Solution {
    
    // 🧱 1. 架构师的降维设计：自定义一个带“势力范围(size)”的超级节点
    class MyBstNode {
        int val;
        int nodeCount; // 核心魔法字段：记录以当前节点为根的子树中，一共包含多少个节点
        MyBstNode left;
        MyBstNode right;

        public MyBstNode(int val) {
            this.val = val;
            this.nodeCount = 1; // 孤立的一个节点，势力范围初始为 1
        }
    }

    public int kthSmallest(TreeNode root, int k) {
        // 第一步：在系统初始化时，遍历原始树，构建出带有 nodeCount 的超级树
        // （在真实的数据库工程中，这一步是在数据每次插入/删除时动态维护的）
        MyBstNode myRoot = buildAugmentedBST(root);
        
        // 第二步：享受 O(log N) 的极速二分查找！
        return findKth(myRoot, k);
    }

    // --- 🛠️ 核心逻辑一：构建超级树 (预处理) ---
    private MyBstNode buildAugmentedBST(TreeNode root) {
        if (root == null) {
            return null;
        }
        MyBstNode node = new MyBstNode(root.val);
        node.left = buildAugmentedBST(root.left);
        node.right = buildAugmentedBST(root.right);
        
        // 向上汇报势力范围：当前节点的总节点数 = 1(自己) + 左边小弟总数 + 右边小弟总数
        int leftSize = (node.left != null) ? node.left.nodeCount : 0;
        int rightSize = (node.right != null) ? node.right.nodeCount : 0;
        node.nodeCount = 1 + leftSize + rightSize;
        
        return node;
    }

    // --- ⚡ 核心逻辑二：极速二分查找 (降维打击) ---
    private int findKth(MyBstNode node, int k) {
        // 先摸清左边到底有多少个小弟（左子树的节点总数）
        int leftSize = (node.left != null) ? node.left.nodeCount : 0;

        // 🎯 场景 A：命中靶心！
        // 如果左边刚好有 leftSize 个比我小的数，那我自己必定是第 leftSize + 1 小的数！
        if (k == leftSize + 1) {
            return node.val;
        } 
        // ⬅️ 场景 B：目标太小，肯定藏在左子树里
        else if (k <= leftSize) {
            // 目标 k 不变，直接去左子树里继续找第 k 小的
            return findKth(node.left, k);
        } 
        // ➡️ 场景 C：目标太大，左子树和自己加起来都不够，肯定在右子树里！
        else {
            // 极其致命的数学逻辑：
            // 既然我们要去右子树找，就必须把左子树的所有节点（leftSize 个）和当前节点（1 个）排除掉。
            // 所以，原来的第 k 小，在右子树的地盘里，就变成了第 (k - leftSize - 1) 小！
            return findKth(node.right, k - leftSize - 1);
        }
    }
}

class Solution2 {
    
    // 🧱 1. 架构师的降维设计：自定义一个带“势力范围(size)”的超级节点
    class MyBstNode {
        int val;
        int nodeCount; // 核心魔法字段：记录以当前节点为根的子树中，一共包含多少个节点
        MyBstNode left;
        MyBstNode right;

        public MyBstNode(int val) {
            this.val = val;
            this.nodeCount = 1; // 孤立的一个节点，势力范围初始为 1
        }
    }

    public int kthSmallest(TreeNode root, int k) {
        // 第一步：在系统初始化时，遍历原始树，构建出带有 nodeCount 的超级树
        // （在真实的数据库工程中，这一步是在数据每次插入/删除时动态维护的）
        MyBstNode myRoot = buildAugmentedBST(root);
        
        // 第二步：享受 O(log N) 的极速二分查找！
        return findKth(myRoot, k);
    }

    // --- 🛠️ 核心逻辑一：构建超级树 (预处理) ---
    private MyBstNode buildAugmentedBST(TreeNode root) {
        if (root == null) {
            return null;
        }
        MyBstNode node = new MyBstNode(root.val);
        node.left = buildAugmentedBST(root.left);
        node.right = buildAugmentedBST(root.right);
        
        // 向上汇报势力范围：当前节点的总节点数 = 1(自己) + 左边小弟总数 + 右边小弟总数
        int leftSize = (node.left != null) ? node.left.nodeCount : 0;
        int rightSize = (node.right != null) ? node.right.nodeCount : 0;
        node.nodeCount = 1 + leftSize + rightSize;
        
        return node;
    }

    // --- ⚡ 核心逻辑二：极速二分查找 (降维打击) ---
    private int findKth(MyBstNode node, int k) {
        // 先摸清左边到底有多少个小弟（左子树的节点总数）
        int leftSize = (node.left != null) ? node.left.nodeCount : 0;

        // 🎯 场景 A：命中靶心！
        // 如果左边刚好有 leftSize 个比我小的数，那我自己必定是第 leftSize + 1 小的数！
        if (k == leftSize + 1) {
            return node.val;
        } 
        // ⬅️ 场景 B：目标太小，肯定藏在左子树里
        else if (k <= leftSize) {
            // 目标 k 不变，直接去左子树里继续找第 k 小的
            return findKth(node.left, k);
        } 
        // ➡️ 场景 C：目标太大，左子树和自己加起来都不够，肯定在右子树里！
        else {
            // 极其致命的数学逻辑：
            // 既然我们要去右子树找，就必须把左子树的所有节点（leftSize 个）和当前节点（1 个）排除掉。
            // 所以，原来的第 k 小，在右子树的地盘里，就变成了第 (k - leftSize - 1) 小！
            return findKth(node.right, k - leftSize - 1);
        }
    }
}