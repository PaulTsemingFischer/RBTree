package com.github.paultsemingfischer.rbtree.tree

abstract class BinaryTree<E, N : BinaryTree.BTNode<E>> {
    open var rootNode: N? = null
    abstract fun add(element : E) : N
    protected abstract fun add(node : N) : N
    abstract fun addAll(inputList: List<E>)

    /* Adds the nodes to the tree in the order of the List
    Precondition: List has at least 1 element */
    protected fun addAll(nodesToAdd: List<N>){
        for(node in nodesToAdd){
            add(node)
        }
    }

    abstract fun remove(value : E) : N?
    protected abstract fun remove(inputNode : N) : N
    fun removeAll(value: E): List<N> {
        val removedNodes: MutableList<N> = mutableListOf()
        var removedNode: N?
        while (remove(value).also{removedNode = it} != null) {
            removedNodes.add(removedNode!!)
        }
        return removedNodes
    }

    open class BTNode<E>(
        override var data: E,
        private var left: BTNode<E>? = null,
        private var right: BTNode<E>? = null
    ) : Node<E, BTNode<E>>{
        fun isLeaf(): Boolean = (left == null && right == null)

        override fun getLeft(): BTNode<E>? = left
        override fun getRight(): BTNode<E>? = right
        override fun setRight(newRight: BTNode<E>?) { right = newRight }
        override fun setLeft(newLeft: BTNode<E>?) { left = newLeft }

        override fun toString() : String{
            return "[$data]"
        }
    }
}