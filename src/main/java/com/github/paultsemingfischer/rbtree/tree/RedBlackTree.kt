package com.github.paultsemingfischer.rbtree.tree

import kotlin.math.pow
import kotlin.text.StringBuilder

open class RedBlackTree<E : Comparable<E>>(inputList: List<E> = emptyList()) : BinarySearchTree<E>() {
    init {
        addAll(inputList)
    }
    override fun getRoot() = super.getRoot() as RBNode? //This says that it overrides the return type but not sure I believe it
    override fun setRoot(root : BSTNode<E>?) {super.setRoot(root as RBNode?)}

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
        while (you.getParent()!!.color == RBNode.RBColor.RED){
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
            add(element)
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

        override fun toString(): String {
            return (if(color == RBColor.RED) "\u001b[41m${super.toString()}" else "\u001b[40m${super.toString()}") + "\u001B[0m"
        }
    }

    //precondition root is not null
    //could be optimized to keep track of index in level while recurring but this is only for the printing method right now so there's not too much of a point
    private fun to2dArray(node: RBNode<E>?, arr: ArrayList<Array<RBNode<E>?>>, lvl: Int) : ArrayList<Array<RBNode<E>?>>{
        if (arr.size == lvl) {
            arr.add(Array(1 shl lvl) { null })
        }

        var curPar = node
        var indexInLvl = 0
        var curLvl = arr.size - lvl - 1
        while (curPar != null) {
            if (curPar.isRightChild()) {
                indexInLvl += 1 shl curLvl
            }
            curPar = curPar.getParent(); curLvl++
        }


        arr[lvl][indexInLvl] = node

        if (node != null && (node.getLeft() != null || node.getRight() != null)) {
            to2dArray(node.getLeft(), arr, lvl + 1)
            to2dArray(node.getRight(), arr, lvl + 1)
        }
        return arr
    }

    fun print(){
        val arr = to2dArray(getRoot(), ArrayList(),0 )
        //this is a stupid way to get the maximum size that a node will be printed at, but it works sooo
        val len = arr.flatMap{it.asIterable()} .maxBy{it.toString().length} .toString().length -9 //-9 cause dumb colors
        for(lvl in 0 until arr.size){
            for(indx in 0 until arr[lvl].size){
                val node = if(arr[lvl][indx] != null) arr[lvl][indx].toString() else "[ ]"
                val spacer = " ".repeat(len)
                val leadingSpacing = spacer.repeat(1 shl (arr.size-lvl-1))
                val middleSpacing = spacer.repeat((1 shl (arr.size-lvl))-1)
                val trailingSpacing = " ".repeat(len - (node.length - if(arr[lvl][indx] != null) 9 else 0)) //check if we need to ignore color indicators
                print((if(indx==0) leadingSpacing else middleSpacing) + node + trailingSpacing)
            }
            println()
        }
    }
}