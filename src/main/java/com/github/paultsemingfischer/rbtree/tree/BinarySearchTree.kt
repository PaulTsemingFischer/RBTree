package com.github.paultsemingfischer.rbtree.tree

class BinarySearchTree<E : Comparable<E>, N : BinarySearchTree<E,N>.BSTNode>(rootNode: N? = null) : BinaryTree<E, N>(rootNode) {
    override fun add(node : N) : N {
        //Handle empty tree
        if(rootNode == null) {rootNode = node; return node}

        //Navigate down tree
        var next = rootNode
        var currentNode : N = next!!
        while (next != null){
            currentNode = next
            next = if(node.data >= currentNode.data) currentNode.right else currentNode.left
        }
        //some pretty cool syntax I came across
        //Add node at our determined location
        currentNode.run {
            if (node.data >= data) right = node
            else left = node
        }
        return node;
    }

    override fun remove(value: E): N? {
        TODO("Not yet implemented")
    }

    override fun remove(node: N): N {
        TODO("Not yet implemented")
    }

    fun findNodeOrNull(value : E) : N? {
        var currentNode = rootNode
        while (currentNode != null){
            if(currentNode.data == value) return currentNode
            currentNode = if(value >= currentNode.data) currentNode.right else currentNode.left
        }
        return null
    }

    fun contains(value : E) : Boolean {
        return findNodeOrNull(value) != null
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