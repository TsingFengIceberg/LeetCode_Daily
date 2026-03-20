class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        // 记录最终需要返回的新链表头节点
        ListNode resultHead = head; 
        
        // Left 相当于“前驱节点”（上一个翻转组的尾巴），用于将当前翻转完的组拼接到前面的链表上
        ListNode Left = head; 
        // Right 相当于“后继节点”，记录下一组的起始位置，防止翻转当前组后丢失后面的链表
        ListNode Right = head; 
        
        // subHead 和 subTail 分别代表当前正在处理的这 k 个节点的头和尾
        ListNode subHead = head; 
        ListNode subTail = head; 
        
        // p, q, r 是翻转单链表经典的三个工作指针
        ListNode p, q, r = null; 
        
        // i 是计数器，用于探路，寻找第 k 个节点
        int i = k; 
        
        // first 标记是否是第一次翻转。因为第一组翻转后的头节点，就是整个大链表最终的头节点
        boolean first = true; 

        // 外层循环：不断让 subTail 探路前进
        while (subTail != null) {
            
            // 核心逻辑：当 i == 1 时，说明 subTail 刚好走到了当前组的第 k 个节点
            if (i == 1) {
                // 1. 保护现场：先把下一组的开头存到 Right 里
                Right = subTail.next; 
                // 2. 断开当前组：将当前组的尾部与后面的链表断开，形成一个独立的长度为 k 的链表
                subTail.next = null; 
                
                // 3. 准备翻转：p 指向当前组的头，r 巧妙地初始化为 Right（即未翻转部分的头）
                // 这样翻转后，当前组的新尾巴（原 subHead）就会自然而然地指向后面的链表！
                p = subHead; 
                r = Right; 
                
                // 4. 标准的单链表翻转模板
                while (p != null) {
                    q = p.next; // q 记住下一个节点
                    p.next = r; // p 掉头指向前一个节点 r
                    r = p;      // r 往前走
                    p = q;      // p 往前走
                }
                
                // 5. 组装拼链：判断是不是第一组
                if (!first) {
                    // 如果不是第一组，把上一组的尾巴（Left）连到当前组的新头（原 subTail）上
                    Left.next = subTail; 
                    // 更新 Left 为当前组的新尾巴（原 subHead），为下一轮组装做准备
                    Left = subHead; 
                } else {
                    // 如果是第一组，那当前组的新头（原 subTail）就是整个大链表的最终头节点
                    resultHead = subTail; 
                    first = false; // 标记第一组已处理完毕
                }
                
                // 6. 状态重置：为寻找下一组的 k 个节点做准备
                i = k; 
                subHead = Right; 
                subTail = Right; 
            }
            
            // 探路指针向前走：只要还没到底，subTail 就继续往下看，并递减计数器
            if (subTail != null) {
                subTail = subTail.next;
                i--;
            }
        }
        
        // 返回最终更新好的大头节点
        return resultHead; 
    }
}