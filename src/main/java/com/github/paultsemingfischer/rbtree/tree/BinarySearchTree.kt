package com.github.paultsemingfischer.rbtree.tree

class BinarySearchTree<E : Comparable<E>>(private var rootNode: BSTNode<E>? = null) : BinaryTree<E, BinarySearchTree.BSTNode<E>>(rootNode) {
    //Precondition: list can not contain null values
    //Post-condition: if the list is sorted, the resulting binary tree will be perfectly balanced(as all things should be)
    constructor(inputList: List<E>) : this() {
        fun getChild(startIndex : Int, endIndex : Int, parent: BSTNode<E>) : BSTNode<E>{
            val currNode = BSTNode(inputList[(endIndex + startIndex)/2], null)

            if(endIndex - startIndex > 1) {//more than 1 node in the range
                currNode.setParent(parent)
                currNode.setLeft(getChild(startIndex, ((endIndex + startIndex)/2), currNode))
                currNode.setRight(getChild((endIndex + startIndex)/2, endIndex, currNode))
            }

            return currNode
        }

        rootNode = BSTNode(inputList[inputList.size/2], null)
        rootNode!!.setLeft(getChild(0, inputList.size/2, rootNode!!))
        rootNode!!.setRight(getChild(inputList.size/2, inputList.size, rootNode!!))
    }

    override fun add(element : E) : BSTNode<E> = add(BSTNode(element, null)) //will this work?? hopefully

    //Returns added node
    override fun add(node : BSTNode<E>) : BSTNode<E> {
        //Handle empty tree
        if(rootNode == null) {rootNode = node; return node}

        //Navigate down tree
        var next = rootNode
        var currentNode : BSTNode<E> = next!!
        while (next != null){
            currentNode = next
            next = if(node.data >= currentNode.data) currentNode.getRight() else currentNode.getLeft() //Tie goes to the right
        }

        //Add node at our determined location
        currentNode.run {  //some pretty cool syntax I came across
            if (node.data >= data) setRight(node)
            else setLeft(node)
            node.setParent(this)
        }
        return node
    }

    override fun addAll(inputList: List<E>): List<BSTNode<E>> {
        val addedNodes = ArrayList<BSTNode<E>>()
        for(element in inputList){
            var myNode : BSTNode<E>
            add(BSTNode(element, null).also { myNode = it })
            addedNodes.add(myNode)
        }
        return addedNodes
    }

    //Returns node removed
    override fun remove(value: E): BSTNode<E>? {
        val removedNode = findNodeOrNull(value) ?: return null
        return remove(removedNode)
    }
    override fun remove(inputNode: BSTNode<E>): BSTNode<E> {
        val node = inputNode as BSTNode<E>
        //They overthrew the king of the wrong kingdom
        //if (!contains(node.data)) return null
        var currentNode : BSTNode<E>
        /* Plot: The king is being overthrown, you will be taking the life of currentNode */

        if(node.getRight() == null || node.getLeft() == null){ //Does the king have an only child (or no successors)?
            promote(node, node.getLeft() ?: node.getRight()) //The kings only child takes the throne (or everyone is dead)
        } else { //There's competition for the throne!!
            //Objective: To become king you want to be the most similar to the old king, but still be better (greater or equal to) than him

            //Look at the kings right child; then you become to the leftmost node from there; check if you're the kings right child
            if(min(node.getRight()!!).also{currentNode = it} == node.getRight()) { //You're the kings right child with no children more similar to the king than you (no left children)
                promote(node, currentNode) //Become king since you have no child that's a closer match
                currentNode.setLeft(node.getLeft()!!) //Adopt the old kings left child
            } else {
                currentNode.getParent()!!.setLeft(currentNode.getRight()) //Donate your right (and only) child to your parent; You must be a free man to become king
                promote(node, currentNode) //After your child is no longer your responsibility, you ascend the throne

                /* You need successors: steal the old kings children */
                //Legally adopt the children of the old king
                currentNode.setLeft(node.getLeft())
                currentNode.setRight(node.getRight())
                //Tell them that you're their daddy
                node.getLeft()?.setParent(currentNode)
                node.getLeft()?.setParent(currentNode)
            }
        }
        return node //Give them the old king to do what they want with
    }
    /*Continued plot from remove... Let's overthrow the king by messing with the kings father (we'll call him god)... You're `newNode`*/
    private fun promote(oldNode: BSTNode<E>, newNode: BSTNode<E>?){
        //if god exists... otherwise you are god by default
        if(oldNode.getParent() != null) {
            if (oldNode.isLeftChild()) { //if the king is seated at the left hand of the father
                oldNode.getParent()!!.setLeft(newNode) //steal the left chair and sit in it to make yourself king!!!
            } else { //if the king is seated at the right hand of the father
                oldNode.getParent()!!.setRight(newNode) //steal the right chair and sit in it to make yourself king!!!
            }
        } else { //god doesn't exist... time to change that
            rootNode = newNode //welcome to godhood
        }
    }
    fun min(root: BSTNode<E>) : BSTNode<E> {
        var currentNode = root
        while (currentNode.getLeft() != null) currentNode = currentNode.getLeft()!!
        return currentNode
    }
    fun max(root: BSTNode<E>) : BSTNode<E> {
        var currentNode = root
        while (currentNode.getRight() != null) currentNode = currentNode.getRight()!!
        return currentNode
    }

    fun findNodeOrNull(value : E) : BSTNode<E>? {
        var currentNode = rootNode
        while (currentNode != null){
            if(currentNode.data == value) return currentNode
            currentNode = if(value >= currentNode.data) currentNode.getRight() else currentNode.getLeft()
        }
        return null
    }

    fun contains(value : E) : Boolean { return findNodeOrNull(value) != null }

    open class BSTNode<E : Comparable<E>>(
        override var data : E,
        private var parent: BSTNode<E>?,
        private var left: BSTNode<E>? = null,
        private var right: BSTNode<E>? = null
    ) : BinaryTree.BTNode<E>(data, left, right), Comparable<BSTNode<E>> {
        override fun getLeft(): BSTNode<E>? = super.getLeft() as BSTNode<E>?
        override fun getRight(): BSTNode<E>? = super.getRight() as BSTNode<E>?
        override fun setLeft(newLeft: BinaryTree.BTNode<E>?) = super.setLeft(newLeft as BSTNode<E>?)
        override fun setRight(newRight: BinaryTree.BTNode<E>?) = super.setRight(newRight as BSTNode<E>?)
        fun getParent() : BSTNode<E>? = parent
        fun setParent(newParent : BSTNode<E>) { parent = newParent }



        fun isLeftChild() : Boolean { return parent?.left == this }
        fun isRightChild() : Boolean { return parent?.right == this }
        override fun compareTo(other: BSTNode<E>): Int{ return data.compareTo(other.data) }
    }
}