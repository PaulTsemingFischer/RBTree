//TODO: Decide what to do with the allAdd and addAll and fix the horrendous naming

package com.github.paultsemingfischer.rbtree.tree
open class BinaryTree<E>(private var rootNode: BTNode<E>? = null) {

    fun getRoot() : BTNode<E>?{
        return rootNode
    }

    fun setRoot(newRoot : BTNode<E>) {
        rootNode = newRoot
    }


    open fun add(node : BTNode<E>) : BTNode<E>{
        TODO()
    }


    /* Adds the nodes to the tree in the order of the List
    Precondition: List has at least 1 element */
    open fun addAll(nodesToAdd: List<BTNode<E>>){
        for(node in nodesToAdd){
            add(node)
        }
    }

    open fun remove(value : E) : BTNode<E>?{
        TODO()
    }

    open fun remove(node : BTNode<E>) : BTNode<E> {
        TODO()
    }

    fun removeAll(value: E): List<BTNode<E>> {
        val removedNodes: MutableList<BTNode<E>> = mutableListOf()
        var removedNode: BTNode<E>?
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
    }
}