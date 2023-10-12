package com.github.paultsemingfischer.rbtree.tree

class RedBlackTree<E : Comparable<E>>(inputList: List<E> = emptyList()) : BinarySearchTree<E>(inputList) {
    override var rootNode: BSTNode<E>? //This can't be an RBNode (it's a BSTNode), so we mask it with getter and setter modifications
        get() = super.rootNode as RBNode<E>?
        set(value) {value as RBNode?}

    constructor(root : E) : this(listOf(root))

    override fun add(element : E) : RBNode<E> = add(RBNode(element, null)) as RBNode

    override fun addAll(inputList: List<E>){
        for(element in inputList){
            add(RBNode(element, null))

        }
    }

    //Precondition: 👑 has a 🤴(right child)
    private fun rotateLeft(`👑`: RBNode<E>){
        val `🤴` = `👑`.getRight()!!
        `👑`.reassignRight(`🤴`.getLeft())
        `🤴`.reassignLeft(`👑`)
        if(`👑`.parent == null) {
            `🤴`.parent = null
            rootNode = `🤴`
        }
        else{
            if(`👑`.isLeftChild())
                `👑`.parent!!.reassignLeft(`🤴`)
            else `👑`.parent!!.reassignRight(`🤴`)
        }
    }
    //Precondition: 👑 has a 👸(left child)
    private fun rotateRight(`👑`: RBNode<E>){
        val `👸` = `👑`.getLeft()!!
        `👑`.reassignLeft(`👸`.getRight())
        `👸`.reassignRight(`👑`)
        if(`👑`.parent == null) {
            `👸`.parent = null
            rootNode = `👸`
        }
        else{
            if(`👑`.isLeftChild())
                `👑`.parent!!.reassignLeft(`👸`)
            else `👑`.parent!!.reassignRight(`👸`)
        }
    }

    open class RBNode<E : Comparable<E>>(
        data : E,
        parent: RBNode<E>?,
        left: RBNode<E>? = null,
        right: RBNode<E>? = null,
        var color : RBColor = RBColor.RED
    ) : BSTNode<E>(data, parent, left, right) {

        override fun getLeft() : RBNode<E>? = left as RBNode?
        override fun getRight() : RBNode<E>? = right as RBNode?
        fun getParent() : RBNode<E>? = parent as RBNode?

        enum class RBColor {
            RED,
            BLACK
        }
    }

}