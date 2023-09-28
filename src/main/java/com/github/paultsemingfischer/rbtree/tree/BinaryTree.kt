package com.github.paultsemingfischer.rbtree.tree
abstract class BinaryTree<E, N : BinaryTree<E,N>.Node>(var rootNode: N? = null) {
    abstract fun add(node : N) : N
    abstract fun remove() : N
    abstract inner class Node(var data: E, var left: N? = null, var right: N? = null){
        open fun isLeaf(): Boolean{
            return (left == null && right == null)
        }
    }
}