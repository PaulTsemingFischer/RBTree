package com.github.paultsemingfischer.rbtree.tree

interface Node<E, N : Node<E, N>>{
    var data: E
    var left: N?
    var right: N?

    fun createNode(nodeData : E) : Node<E,N>
}