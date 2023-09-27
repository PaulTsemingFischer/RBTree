package com.github.paultsemingfischer.rbtree.tree

abstract class BinaryTree<E : Comparable<E>, N : BinaryTree.Node<E, N>> {

    abstract var rootNode: N?

    abstract fun addNode(node : N) : N

    abstract fun removeNode() : N

    abstract class Node<E : Comparable<E>, N : Node<E, N>>( //N is the specific Node type that you are
        var data: E,
        var left: N? = null,
        var right: N? = null) : Comparable<N> {

        open fun isLeaf(): Boolean{
            return (left == null && right == null)
        }
        override fun compareTo(other: N): Int{
            return data.compareTo(other.data)
        }
        //we can create abstract functions here
    }
}