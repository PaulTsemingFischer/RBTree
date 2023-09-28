package com.github.paultsemingfischer.rbtree.tree

class BinarySearchTree<E : Comparable<E>, N : BinarySearchTree<E,N>.BSTNode>(rootNode: N? = null) : BinaryTree<E, N>(rootNode) {

    override fun add(node: N): N {
        TODO("Not yet implemented")
    }
    override fun remove(): N {
        TODO("Not yet implemented")
    }
    fun findOrNull(value : E) : N? {
        TODO() //Binary search for value. Return Node or null
    }
    fun exists(value : E) : Boolean {
        return findOrNull(value) != null
    }
    inner class BSTNode(data: E, val parent: N?, left: N? = null, right: N? = null)
        : BinaryTree<E, N>.Node(data, left, right), Comparable<N> {
        fun isLeftChild() : Boolean { return parent?.left == this }
        fun isRightChild() : Boolean { return parent?.right == this }
        override fun compareTo(other: N): Int{
            return data.compareTo(other.data)
        }
    }
}