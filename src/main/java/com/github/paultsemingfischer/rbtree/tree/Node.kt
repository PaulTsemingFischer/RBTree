package com.github.paultsemingfischer.rbtree.tree

interface Node<E, N : Node<E, N>>{
    var data: E

    fun getLeft() : Node<E,N>?
    fun setLeft(newLeft : N?)

    fun getRight() : Node<E,N>?
    fun setRight(newRight : N?)
}