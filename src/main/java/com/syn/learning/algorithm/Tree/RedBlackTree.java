package com.syn.learning.algorithm.Tree;

/**
 * @author shiyinong
 * @version 1.0
 * @ClassName RedBlackTree
 * @Description 红黑树
 * @Date 2019/5/27 15:27
 **/
public class RedBlackTree {
    private final static boolean RED=true;
    private final static boolean BLACK=false;
    private class Node{
        private boolean color;
        private int val;
        private Node left;
        private Node right;

        private Node(boolean color, int val) {
            this.color = color;
            this.val = val;
        }
    }

    private Node root;

    private boolean isRed(Node node){
        return null!=node&&node.color==RED;
    }

    public void insert(int val){
        root=insertHelper(root,val);
        root.color =BLACK;
    }

    private Node insertHelper(Node node,int val){
        if(null==node) return new Node(RED,val);
        //插入新值
        if(val>node.val) {
            node.right=insertHelper(node.right,val);
        }else{
            node.left=insertHelper(node.left,val);
        }
        //自底向上的平衡树结构，以下三步的顺序很重要
        if(isRed(node.right)&&!isRed(node.left)){ //左孩子为黑节点，右孩子为红节点，左旋
            node=rotateLeft(node);
        }
        if(isRed(node.left)&&isRed(node.left.left)){ //左孩子为红节点，左孙子为红节点，右旋
            node=rotateRight(node);
        }
        if(isRed(node.left)&&isRed(node.right)){ //左右孩子均为红节点，改变颜色
            flipColor(node);
        }
        return node;
    }

    private Node rotateLeft(Node node){
        Node right=node.right;
        node.right=right.left;
        right.left=node;
        right.color =node.color;
        node.color =RED;
        return right;
    }

    private Node rotateRight(Node node){
        Node left=node.left;
        node.left=left.right;
        left.right=node;
        left.color =node.color;
        node.color =RED;
        return left;
    }

    private void flipColor(Node node){
        node.left.color =BLACK;
        node.right.color =BLACK;
        node.color =RED;
    }

    public boolean contains(int val){
        return containsHelper(root,val);
    }

    private boolean containsHelper(Node node,int val){
        if(node==null) return false;
        if(node.val==val) return true;
        if(val>node.val) return containsHelper(node.right,val);
        return containsHelper(node.left,val);
    }


}
