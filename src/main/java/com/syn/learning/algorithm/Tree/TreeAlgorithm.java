package com.syn.learning.algorithm.Tree;

import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName TreeAlgorithm
 * @Description 树相关的算法
 * @Date 2019/3/30 16:19
 **/


public class TreeAlgorithm {

    /**
     * @description 二叉树的中序遍历，迭代版
     * @date 16:36 2019/3/30
     * @Param [root]
     * @return java.util.List<java.lang.Integer>
    **/
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();

        Stack<TreeNode> sta=new Stack<>();
        TreeNode tn=root;
        while(tn!=null||!sta.isEmpty()){
            if(tn!=null){
                sta.add(tn);
                tn=tn.left;
            }else{
                TreeNode tn2=sta.pop();
                ans.add(tn2.val);
                if(tn2.right!=null){ //如果tn2有右孩子，则继续遍历他的右孩子
                    tn=tn2.right;
                }
            }
        }
        return ans;
    }

    /**
     * @description 二叉树的前序遍历，迭代版
     * @date 16:39 2019/3/30
     * @Param [root]
     * @return java.util.List<java.lang.Integer>
    **/
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        Stack<TreeNode> sta=new Stack<>();
        TreeNode tn=root;
        while(tn!=null||!sta.isEmpty()){
            if(tn!=null){
                sta.add(tn);
                ans.add(tn.val);
                tn=tn.left;
            }else{
                TreeNode tn2=sta.pop();
                if(tn2.right!=null){ //如果tn2有右孩子，则继续遍历他的右孩子
                    tn=tn2.right;
                }
            }
        }
        return ans;
    }

    /**
     * @description 二叉树的后序遍历，迭代版
     * @date 17:18 2019/3/30
     * @Param [root]
     * @return java.util.List<java.lang.Integer>
    **/
    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        Stack<TreeNode> sta=new Stack<>();
        TreeNode tn=root;
        TreeNode pre=null;
        while(tn!=null||!sta.isEmpty()){
            if(tn!=null){
                sta.add(tn);
                tn=tn.left;
            }else{
                TreeNode tn2=sta.peek();
                if(tn2.right==null||tn2.right==pre){ //如果tn2没有右孩子或者已经遍历过他的右孩子，则tn2出栈
                    ans.add(tn2.val);
                    pre=sta.pop();
                }else{
                    tn=tn2.right;
                }
            }
        }
        return ans;
    }

    /**
     * @description 依据前序遍历构造BST
     * @date 17:20 2019/3/30
     * @Param [preorder]
     * @return com.syn.Algorithm.Tree.TreeNode
    **/
    public static TreeNode bstFromPreorder(int[] preorder) {
        return bstFromPreorderHelper(preorder,0,preorder.length-1);
    }
    private static TreeNode bstFromPreorderHelper(int[] preorder,int left,int right){
        if(left>right) return null;
        int idx=right+1;
        for(int i=left+1;i<=right;i++){
            if(preorder[i]>preorder[left]){
                idx=i;
                break;
            }
        }
        TreeNode tn=new TreeNode(preorder[left]);
        tn.left=bstFromPreorderHelper(preorder,left+1,idx-1);
        tn.right=bstFromPreorderHelper(preorder,idx,right);
        return tn;
    }
}
