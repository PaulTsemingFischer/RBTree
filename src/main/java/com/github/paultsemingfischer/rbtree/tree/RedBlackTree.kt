package com.github.paultsemingfischer.rbtree.tree

class RedBlackTree<E : Comparable<E>>(inputList: List<E> = emptyList()) : BinarySearchTree<E>(inputList) {
    override var rootNode: BSTNode<E>? //This can't be an RBNode (it's a BSTNode), so we mask it with getter and setter modifications
        get() = super.rootNode as RBNode<E>?
        set(value) {value as RBNode?}

    constructor(root : E) : this(listOf(root))

    override fun add(element : E) : RBNode<E> {
        val addedNode = add(RBNode(element, null)) as RBNode
        insertFixUp(addedNode)
        return addedNode
    }

    private fun insertFixUp(n : RBNode<E>){
        var node = n
        if(node == rootNode){
            node.color = RBNode.RBColor.BLACK
            return
        }
        while (node.getParent()?.color == RBNode.RBColor.RED){ //While parent is red
            if(node.getParent()!!.isLeftChild()){ //If parent is left child
                val uncle = node.getParent(2)?.getRight()
                if (uncle != null && uncle.color == RBNode.RBColor.RED) { //case 1: uncle is red
                    uncle.color = RBNode.RBColor.BLACK //set uncle to black
                    node = node. //set node to grandparent
                    getParent()!!.also{it.color = RBNode.RBColor.BLACK}. //as we're accessing parent, set it's color to black
                    getParent()!!.also{it.color = RBNode.RBColor.BLACK} //as we're accessing grandparent, set it's color to black
                } else {//case 2+3: uncle is black or null
                    node.run {
                        if (isRightChild()) {//case 2: you are right child
                            node = getParent()!!
                            parent = node.getParent()!!
                            rotateLeft(node)
                        }
                        //case 3: you are left child
                        getParent()!!.color = RBNode.RBColor.BLACK
                        getParent(2)!!.color = RBNode.RBColor.RED
                        rotateRight(getParent(2)!!)
                    }
                }
            }
        }
    }

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
        fun getParent(generationsBack: Int = 1) : RBNode<E>? {
            if(generationsBack == 1){
                return this.parent as RBNode?
            }
            return getParent(generationsBack-1)?.parent as RBNode?
        }

        enum class RBColor {
            RED,
            BLACK
        }
    }

}