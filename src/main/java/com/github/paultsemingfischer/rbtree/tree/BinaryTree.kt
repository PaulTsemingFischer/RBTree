package com.github.paultsemingfischer.rbtree.tree
abstract class BinaryTree<E : Comparable<E>, N : BinaryTree<E,N>.Node>(var rootNode: N? = null) {

    abstract fun addNode(node : N) : N

    abstract fun removeNode() : N

    abstract inner class Node(var data: E, var left: N? = null, var right: N? = null) : Comparable<N> {
        open fun isLeaf(): Boolean{
            return (left == null && right == null)
        }
        override fun compareTo(other: N): Int{
            return data.compareTo(other.data)
        }
    }
}