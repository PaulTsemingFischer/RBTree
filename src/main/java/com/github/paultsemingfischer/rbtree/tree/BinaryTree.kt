//TODO: Decide what to do with the allAdd and addAll and fix the horrendous naming

package com.github.paultsemingfischer.rbtree.tree
open class BinaryTree<E, N : BinaryTree.BTNode<E, N>>(var rootNode: N? = null) {

    open fun add(node : N) : N{
        TODO()
    }


    /* Adds the nodes to the tree in the order of the List
    Precondition: List has at least 1 element */
    open fun addAll(nodesToAdd: List<N>){
        for(node in nodesToAdd){
            add(node)
        }
    }

    open fun remove(value : E) : N?{
        TODO()
    }

    open fun remove(node : N) : N {
        TODO()
    }

    fun removeAll(value: E): List<N> {
        val removedNodes: MutableList<N> = mutableListOf()
        var removedNode: N?
        while (remove(value).also{removedNode = it} != null) {
            removedNodes.add(removedNode!!)
        }
        return removedNodes
    }

    open class BTNode<E, N : BTNode<E,N>>(
                       override var data: E,
                       override var left: N? = null,
                       override var right: N? = null
    ) : Node<E, N>{
        fun isLeaf(): Boolean{
            return (left == null && right == null)
        }

        override fun createNode(nodeData: E): BTNode<E, N> {
            return BTNode(nodeData)
        }
    }
}