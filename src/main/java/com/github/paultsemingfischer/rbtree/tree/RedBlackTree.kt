package com.github.paultsemingfischer.rbtree.tree

open class RedBlackTree<E : Comparable<E>>(inputList: List<E> = emptyList()) : BinarySearchTree<E>(inputList) {
    const val RED = "\u001b[31m"


    override var rootNode: BSTNode<E>? = null//This can't be an RBNode (it's a BSTNode), so we mask it with getter and setter modifications
    fun getRoot() = super.rootNode as RBNode<E>?
    protected fun setRoot(value:RBNode<E>) {rootNode = value}

    constructor(root : E) : this(listOf(root))

    override fun add(element : E) : RBNode<E> {
        val addedNode = add(RBNode(element, null)) as RBNode
        insertFixUp(addedNode)
        return addedNode
    }

    private fun insertFixUp(node : RBNode<E>){
        //Welcome into the world, you are an unimportant red child, already (probably) causing chaos within the kingdom
        var you = node
        //If you were born from nothing, and you have no parents you are the king of the entire realm
        //The king is important and thus must have the famous well-known black status
        if(you.parent == null) {you.color = RBNode.RBColor.BLACK; return}

        //If your parent is red then we got a problem because you can't have 2 straight generations go unremembered
        while (you.getParent()?.color == RBNode.RBColor.RED){
            //this is your dad
            var dad = you.getParent()!!
            //this is your dads dad
            val grandpa = dad.getParent()!!
            //this is your uncle; your dads only sibling (hopefully he exists)
            val uncle = if(dad.isRightChild()) grandpa.getRight() else grandpa.getLeft()

            //is your uncle unimportant?
            //if your uncle doesn't exist... well then he is not red, and would probably be famous if he existed, so we're pretend he is
            if (uncle?.color == RBNode.RBColor.RED) {
                //Now lets make him and your dad both to famous so that you and your grandparent can be an unimportant red
                //CASE 1
                uncle.color = RBNode.RBColor.BLACK //force your uncle to be famous
                dad.color = RBNode.RBColor.BLACK //dad gets famous with him
                grandpa.color = RBNode.RBColor.RED //force grandpa to red, he has such successful children that he's forgotten about
                you = grandpa //well now you possess the poor soul of your grandpa
                //Lets see if you're a problem in your family...
            }
            //Alright, so your uncle is famous or dead, we treat them the same
            else {
                //If you are a middle child that's not good, we want you to be an edge child
                if(dad.isRightChild() xor you.isRightChild()){
                    //CASE 2: Uncle is black or null, you are a middle child
                    //Switch bodies with your dad
                    you = dad.also{dad = you}
                    //Now you, in the body of your dad, want to move to the edge (the same side to your dad as your dad is to his dad)
                    //Formally make yourself a child of your dad again but on the edge
                    if(you.isRightChild()) rotateLeft(you) else rotateRight(you) //rotates you the edge (same side of your dad)
                    //Well we didn't quite solve the problem, you and your dad is still both unimportant but now that an easy fix
                }
                //CASE 3
                //make dad famous like his brother
                dad.color = RBNode.RBColor.BLACK
                //force grandpa to become red, he has such successful children that he's forgotten about
                grandpa.color = RBNode.RBColor.RED
                //you and your grandpa have so much in common you might as well be brothers...
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


    fun printTree() {
        val nodes: ArrayList<Pair<RBNode<E>, Int>> = ArrayList()
        storeAllNodes(getRoot()!!, 0, nodes)
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

    private fun storeAllNodes(node: RBNode<E>, level: Int = 0, storage: ArrayList<Pair<RBNode<E>, Int>>): ArrayList<Pair<RBNode<E>, Int>> {
        storage.add(Pair(node, level))
        if(node.getLeft() != null) {
            storeAllNodes(node.getLeft()!!, level + 1, storage)
        }
        if(node.getRight() != null){
            storeAllNodes(node.getRight()!!, level + 1, storage)
        }
        return storage
    }
}