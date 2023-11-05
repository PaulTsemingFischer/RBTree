package com.github.paultsemingfischer.rbtree.tree

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

        var you = node

        while (you.getParent()?.color == RBNode.RBColor.RED){
            var dad = you.getParent()!!
            val grandpa = dad.getParent()!!
            val uncle = if(dad.isRightChild()) grandpa.getLeft() else grandpa.getRight()

            if (uncle?.color == RBNode.RBColor.RED) {
                //CASE 1
                uncle.color = RBNode.RBColor.BLACK
                dad.color = RBNode.RBColor.BLACK
                grandpa.color = RBNode.RBColor.RED
                you = grandpa
            }
            else {

                if(dad.isRightChild() xor you.isRightChild()){
                    //CASE 2: Uncle is black or null, you are a middle child
                    you = dad.also{dad = you}
                    if(dad.isRightChild()) rotateLeft(you) else rotateRight(you)
                    //You are now an edge child
                }
                //CASE 3: You are an edge child
                dad.color = RBNode.RBColor.BLACK
                grandpa.color = RBNode.RBColor.RED
                if(dad.isRightChild()) rotateLeft(grandpa) else rotateRight(grandpa)
            }
        }

        if(you.getParent() == null) {//Flipping root to Black
            you.color = RBNode.RBColor.BLACK;
            return
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
        val parent = `👑`.getParent()
        `👑`.reassignRight(`🤴`.getLeft())
        `🤴`.reassignLeft(`👑`)
        if(parent == null) {
            `🤴`.parent = null
            rootNode = `🤴`
        }
        else{
            if(parent.getLeft() == `👑`)
                parent.reassignLeft(`🤴`)
            else parent.reassignRight(`🤴`)
        }
    }
    //Precondition: 👑 has a 👸(left child)
    private fun rotateRight(`👑`: RBNode<E>){
        val `👸` = `👑`.getLeft()!!
        val parent = `👑`.getParent()
        `👑`.reassignLeft(`👸`.getRight())
        `👸`.reassignRight(`👑`)
        if(parent == null) {
            `👸`.parent = null
            rootNode = `👸`
        }
        else{
            if(parent.getLeft() == `👑`)
                parent.reassignLeft(`👸`)
            else parent.reassignRight(`👸`)
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

        fun recursivePrint(){
            print(this)
            getLeft()?.recursivePrint()
            getRight()?.recursivePrint()
        }
    }

    //Precondition: root is not null
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


        if(node != null) arr[lvl][indexInLvl] = node

        if (node != null && (node.getLeft() != null || node.getRight() != null)) {
            to2dArray(node.getLeft(), arr, lvl + 1)
            to2dArray(node.getRight(), arr, lvl + 1)
        }
        return arr
    }

    fun printTree(){
        println("----------------------------------------------------------------------------------------------")
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
        println("----------------------------------------------------------------------------------------------")

    }
}