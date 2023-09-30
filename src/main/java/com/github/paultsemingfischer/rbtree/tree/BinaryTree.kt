package com.github.paultsemingfischer.rbtree.tree
abstract class BinaryTree<E, N : BinaryTree<E,N>.Node>(var rootNode: N? = null) {

    abstract fun add(node : N) : N

    abstract fun remove(value : E) : N?
    abstract fun remove(node : N) : N
    fun removeAll(value: E): List<N> {
        val removedNodes: MutableList<N> = mutableListOf()
        var removedNode: N?
        while (remove(value).also{removedNode = it} != null) {
            removedNodes.add(removedNode!!)
        }
        return removedNodes
    }

    abstract inner class Node(var data: E, var left: N?, var right: N?){
        open fun isLeaf(): Boolean{
            return (left == null && right == null)
        }
    }
}