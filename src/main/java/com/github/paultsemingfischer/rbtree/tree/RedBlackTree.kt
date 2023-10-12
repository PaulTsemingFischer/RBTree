package com.github.paultsemingfischer.rbtree.tree

class RedBlackTree<E : Comparable<E>>(inputList: List<E> = emptyList()) : BinarySearchTree<E>(inputList) {
    override var rootNode: BSTNode<E>? //This can't be an RBNode, so we mask it with getter and setter modifications
        get() = super.rootNode as RBNode<E>?
        set(value) {value as RBNode<E>?}
    constructor(root : E) : this(listOf(root))

    override fun add(element : E) : RBNode<E> = add(RBNode(element, null)) as RBNode

    override fun addAll(inputList: List<E>){
        //val addedNodes = ArrayList<BSTNode<E>>()
        for(element in inputList){
            //var myNode : BSTNode<E>
            add(RBNode(element, null))//.also { myNode = it })
            //addedNodes.add(myNode)
        }
        //return addedNodes
    }

    //Precondition: 👑 has a 🤴(right child)
    private fun rotateLeft(`👑`: RBNode<E>){
        val `🤴` = `👑`.getRight()!!
        `👑`.reassignRight(`🤴`.getLeft())
        `🤴`.reassignLeft(`👑`)
        if(`👑`.parent == null) rootNode = `🤴`
        else{
            if(`👑`.isLeftChild())
                `👑`.parent!!.reassignLeft(`🤴`)
            else `👑`.parent!!.reassignRight(`🤴`)
        }
    }

    open class RBNode<E : Comparable<E>>(
        override var data : E,
        parent: RBNode<E>?,
        left: RBNode<E>? = null,
        right: RBNode<E>? = null,
        var color : RBColor = RBColor.RED
    ) : BSTNode<E>(data, parent, left, right) {
        override fun getLeft(): RBNode<E>? = super.getLeft() as RBNode<E>?
        override fun getRight(): RBNode<E>? = super.getRight() as RBNode<E>?
        override fun setLeft(newLeft: BTNode<E>?) = super.setLeft(newLeft as RBNode<E>?)
        override fun setRight(newRight: BTNode<E>?) = super.setRight(newRight as RBNode<E>?)
        override fun getParent() : RBNode<E>? = super.getParent() as RBNode<E>?
        override fun setParent(newParent: BSTNode<E>?) { setParent(newParent)}
        enum class RBColor {
            RED,
            BLACK
        }
    }

}