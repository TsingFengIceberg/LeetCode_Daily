import java.util.Random;

public class Test {
    public static void main(String[] args) {
        // 实例化你的三个解法
        Solution1 s1 = new Solution1();
        Solution2 s2 = new Solution2();
        Solution3 s3 = new Solution3();

        System.out.println("====== 准备 100 万级海量压测数据 ======");
        int size = 1_000_000;
        int[][] originData = new int[size][2];
        Random random = new Random();
        
        // 构造原始乱序数据
        for (int i = 0; i < size; i++) {
            int start = random.nextInt(50000); // 制造大量重叠区间
            int end = start + random.nextInt(100);
            originData[i] = new int[]{start, end};
        }

        // 必须进行深拷贝！保证三个算法起跑线完全一致，面对的都是同一份乱序数据
        int[][] dataForS1 = deepCopy(originData);
        int[][] dataForS2 = deepCopy(originData);
        int[][] dataForS3 = deepCopy(originData);

        System.out.println("数据构造完成，开始预热 JVM...");
        // 预热 JVM：排除 JIT 即时编译的冷启动时间误差
        for(int i = 0; i < 20; i++) {
            int[][] warmup = {{1, 3}, {2, 6}, {8, 10}};
            s1.merge(deepCopy(warmup));
            s2.merge(deepCopy(warmup));
            s3.merge(deepCopy(warmup));
        }

        System.out.println("====== 开始极限性能对决 ======");
        
        // 1. 测试 Solution1 (ArrayList + Lambda 对象排序)
        long start1 = System.nanoTime();
        s1.merge(dataForS1);
        long end1 = System.nanoTime();
        double time1 = (end1 - start1) / 1_000_000.0;
        System.out.println("Solution1 (ArrayList + Lambda): " + String.format("%.2f", time1) + " ms");

        // 2. 测试 Solution2 (原生二维数组 + Lambda 对象排序)
        long start2 = System.nanoTime();
        s2.merge(dataForS2);
        long end2 = System.nanoTime();
        double time2 = (end2 - start2) / 1_000_000.0;
        System.out.println("Solution2 (原生二维数组 + Lambda): " + String.format("%.2f", time2) + " ms");

        // 3. 测试 Solution3 (一维基本类型降维拆分 + Dual-Pivot Quicksort)
        long start3 = System.nanoTime();
        s3.merge(dataForS3);
        long end3 = System.nanoTime();
        double time3 = (end3 - start3) / 1_000_000.0;
        System.out.println("Solution3 (终极降维一维数组):   " + String.format("%.2f", time3) + " ms");
        
        System.out.println("---------------------------------------");
        System.out.println("性能分析总结：");
        System.out.println("从 S1 到 S2：省去了 ArrayList 动态扩容和拆装箱的开销。");
        System.out.println("从 S2 到 S3：彻底抛弃对象引用数组，利用基本数据类型连续内存的特征，触发底层极致优化的双轴快排，形成了降维打击！");
    }

    /**
     * 辅助方法：二维数组的深拷贝
     */
    private static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][2];
        for (int i = 0; i < original.length; i++) {
            copy[i][0] = original[i][0];
            copy[i][1] = original[i][1];
        }
        return copy;
    }
}