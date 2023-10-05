package com.github.paultsemingfischer.rbtree.tree

fun main() {
    val bst = BinarySearchTree(listOf(1,2,3,4,5,6,7,8,9))
    printTree(bst)
    bst.remove(4)

    println("\nremoved 4")
    printTree(bst)
}


fun<E : Comparable<E>> printTree(tree : BinarySearchTree<E>) {
    //Pair consists of the node and it's level in the tree
    val nodes: ArrayList<Pair<BinarySearchTree.BSTNode<E>?, Int>> = ArrayList()
    storeAllNodes(tree.getRoot(), 0, nodes)
    nodes.sortBy {it.second}

    var prevLevel = 0
    for (i in nodes) {
        if(i.second > prevLevel){
            println()
            prevLevel = i.second
        }
        print("${i.first}   ")
    }
}
private fun<E : Comparable<E>> storeAllNodes(node: BinarySearchTree.BSTNode<E>?, level: Int = 0, storage: ArrayList<Pair<BinarySearchTree.BSTNode<E>?, Int>>): ArrayList<Pair<BinarySearchTree.BSTNode<E>?, Int>> {
    storage.add(Pair(node, level))
    if(node?.getLeft() != null) {
        storeAllNodes(node.getLeft()!!, level + 1, storage)
    }
    if(node?.getRight() != null){
        storeAllNodes(node.getRight()!!, level + 1, storage)
    }
    return storage
}