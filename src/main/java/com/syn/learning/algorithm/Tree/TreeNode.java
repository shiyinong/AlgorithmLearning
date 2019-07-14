package com.syn.learning.algorithm.Tree;



import sun.security.krb5.internal.crypto.KeyUsage;

import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName TreeNode
 * @Description TODO
 * @Date 2019/3/30 16:21
 **/
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;
    public TreeNode(int x) { val = x; }

    public static void main(String[] args){
        TreeNode t1 = new TreeNode(1);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(3);
        TreeNode t4 = new TreeNode(4);
        TreeNode t5 = new TreeNode(5);
        TreeNode t6 = new TreeNode(6);
        t1.left=t2;

        t2.left=t3;
        t2.right=t4;
        t3.right=t6;
        t4.right=t5;
    }



}





