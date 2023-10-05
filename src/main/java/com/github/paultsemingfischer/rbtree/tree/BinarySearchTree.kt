package com.github.paultsemingfischer.rbtree.tree

class BinarySearchTree<E : Comparable<E>>(rootNode: BSTNode<E>? = null) : BinaryTree<E, BinarySearchTree.BSTNode<E>>(rootNode) {
    //Precondition: list can not contain null values
    //Post-condition: if the list is sorted, the resulting binary tree will be perfectly balanced(as all things should be)
    constructor(inputList: List<E>) : this() {
        fun setChildren(startIndex: Int, endIndex: Int): BSTNode<E>? {
            if (startIndex > endIndex) { //middle index in previous call was 0 meaning that your leaf node has been added... therefore
                return null //it has no children
            }

            val middleIndex = (startIndex + endIndex) / 2 //middle of all nodes startIndex to endIndex inclusive on both sides
            val child = BSTNode(inputList[middleIndex], null) //make our parent node the middle

            child.setLeft(setChildren(startIndex, middleIndex - 1).also { it?.setParent(child) }) //set the children on the left from beginning up until but not including us (this is inclusive because we use end index in our middle index calculation
            child.setRight(setChildren(middleIndex + 1, endIndex).also { it?.setParent(child) }) //set the children on the right from the one right of the middle until the end (this is also inclusive because we use end index in the middle index calculation

            return child //return the node added which will return the parent of the subtree of children you created
        }
        setRoot(setChildren(0, inputList.size - 1))//set the children of your subtree which in this case is the whole tree
    }

    override fun add(element : E) : BSTNode<E> = add(BSTNode(element, null)) //will this work?? hopefully

    //Returns added node
    override fun add(node : BSTNode<E>) : BSTNode<E> {
        //Handle empty tree
        if(getRoot() == null) {setRoot(node); return node}

        //Navigate down tree
        var next = getRoot()
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
        //They overthrew the king of the wrong kingdom
        //if (!contains(node.data)) return null
        var currentNode : BSTNode<E>
        /* Plot: The king is being overthrown, you will be taking the life of currentNode */

        if(inputNode.getRight() == null || inputNode.getLeft() == null){ //Does the king have an only child (or no successors)?
            promote(inputNode, inputNode.getLeft() ?: inputNode.getRight()) //The kings only child takes the throne (or everyone is dead)
        } else { //There's competition for the throne!!
            //Objective: To become king you want to be the most similar to the old king, but still be better (greater or equal to) than him

            //Look at the kings right child; then you become to the leftmost node from there; check if you're the kings right child
            if(min(inputNode.getRight()!!).also{currentNode = it} == inputNode.getRight()) { //You're the kings right child with no children more similar to the king than you (no left children)
                promote(inputNode, currentNode) //Become king since you have no child that's a closer match
                currentNode.setLeft(inputNode.getLeft()!!) //Adopt the old kings left child
            } else {
                currentNode.getParent()!!.setLeft(currentNode.getRight()) //Donate your right (and only) child to your parent; You must be a free man to become king
                promote(inputNode, currentNode) //After your child is no longer your responsibility, you ascend the throne

                /* You need successors: steal the old kings children */
                //Legally adopt the children of the old king
                currentNode.setLeft(inputNode.getLeft())
                currentNode.setRight(inputNode.getRight())
                //Tell them that you're their daddy
                inputNode.getLeft()?.setParent(currentNode)
                inputNode.getLeft()?.setParent(currentNode)
            }
        }
        return inputNode //Give them the old king to do what they want with
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
            setRoot(newNode) //welcome to godhood
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
        var currentNode = getRoot()
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