package com.github.paultsemingfischer.rbtree.tree

fun main() {
    println("RedBlackTree(listOf(8, 3, 4, 9))")
    val rbt = RedBlackTree(listOf(8, 3, 4, 9))
    println("Final tree")
    rbt.printTree()
}