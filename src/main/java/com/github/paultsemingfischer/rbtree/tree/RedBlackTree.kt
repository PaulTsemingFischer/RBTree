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

    private fun insertFixUp(node : RBNode<E>){
        //we can directly access parent if there's no need to access RB specific properties like color
        var you = node
        if(you.parent == null) {you.color = RBNode.RBColor.BLACK; return} //you are root

        while (you.getParent()?.color == RBNode.RBColor.RED){ //While parent is red
            var dad = you.getParent()!!
            val grandpa = dad.getParent()!!

            val uncle = if(dad.isRightChild()) grandpa.getRight() else grandpa.getLeft()
            //If parent is left child
            if (uncle?.color == RBNode.RBColor.RED) { //case 1: uncle is red if null it will return false which is fine because nodes that don't exist are treated as black
                uncle.color = RBNode.RBColor.BLACK //set uncle to black
                dad.color = RBNode.RBColor.BLACK
                grandpa.color = RBNode.RBColor.RED
                you = grandpa
            } else {//case 2+3: uncle is black or null
                if(dad.isRightChild() xor you.isRightChild()){ //true if you are on the opposite side of your dad
                    you = dad.also{dad = you}
                    if(dad.isRightChild()) rotateLeft(you) else rotateRight(you) //rotates you the opposite side of your dad
                }
                //case 3: you are left child
                dad.color = RBNode.RBColor.BLACK
                grandpa.color = RBNode.RBColor.RED
                if(dad.isRightChild()) rotateRight(grandpa) else rotateLeft(grandpa)
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