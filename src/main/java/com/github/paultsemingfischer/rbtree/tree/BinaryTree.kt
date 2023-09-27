package com.github.paultsemingfischer.rbtree.tree

abstract class BinaryTree<E : Comparable<E>>() {

    abstract val rootNode: Node<E>

    abstract fun addNode(node : Node<E>)

    abstract fun removeNode() : Node<E>

    abstract inner class Node<E : Comparable<E>>(
        private val data: E,
        private val left: Node<E>? = null,
        private val right: Node<E>? = null) : Comparable<Node<E>> {

        fun isLeaf(): Boolean{
            return (left == null && right == null)
        }
        override fun compareTo(other: Node<E>): Int {
            return data.compareTo(other.data)
        }
    }
}