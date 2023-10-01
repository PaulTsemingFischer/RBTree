package com.github.paultsemingfischer.rbtree.tree

class BinarySearchTree<E : Comparable<E>, N : BinarySearchTree<E,N>.BSTNode>(rootNode: N? = null) : BinaryTree<E, N>(rootNode) {

    //Precondition: list can not contain null values
    //Postcondition: if the list is sorted, the resulting binary tree will be perfectly balanced(as all things should be)
    constructor(inputList: List<N>) : this() {
        fun getChild(startIndex : Int, endIndex : Int, parent: N) : N{
            val currNode = inputList[(endIndex + startIndex)/2]

            if(endIndex - startIndex > 1) {//more than 1 node in the range
                currNode.parent = parent
                currNode.left = getChild(startIndex, ((endIndex + startIndex)/2), currNode)
                currNode.right = getChild((endIndex + startIndex)/2, endIndex, currNode)
            }

            return currNode
        }

        rootNode = inputList[inputList.size/2]
        rootNode!!.left = getChild(0, inputList.size/2, rootNode!!)
        rootNode!!.right = getChild(inputList.size/2, inputList.size, rootNode!!)
    }


    //Returns added node
    override fun add(node : N) : N {
        //Handle empty tree
        if(rootNode == null) {rootNode = node; return node}

        //Navigate down tree
        var next = rootNode
        var currentNode : N = next!!
        while (next != null){
            currentNode = next
            next = if(node.data >= currentNode.data) currentNode.right else currentNode.left //Tie goes to the right
        }

        //Add node at our determined location
        currentNode.run {  //some pretty cool syntax I came across
            if (node.data >= data) right = node
            else left = node
            node.parent = this
        }
        return node
    }

    //Returns node removed
    override fun remove(value: E): N? {
        val removedNode = findNodeOrNull(value) ?: return null
        return remove(removedNode)
    }
    override fun remove(node: N): N {
        //They overthrew the king of the wrong kingdom
        //if (!contains(node.data)) return null
        var currentNode : N
        /* Plot: The king is being overthrown, you will be taking the life of currentNode */

        if(node.right == null || node.left == null){ //Does the king have an only child (or no successors)?
            promote(node, node.left ?: node.right) //The kings only child takes the throne (or everyone is dead)
        } else { //There's competition for the throne!!
            //Objective: To become king you want to be the most similar to the old king, but still be better (greater or equal to) than him

            //Look at the kings right child; then you become to the leftmost node from there; check if you're the kings right child
            if(min(node.right!!).also{currentNode = it} == node.right) { //You're the kings right child with no children more similar to the king than you (no left children)
                promote(node, currentNode) //Become king since you have no child that's a closer match
                currentNode.left = node.left //Adopt the old kings left child
            } else {
                currentNode.parent!!.left = currentNode.right //Donate your right (and only) child to your parent; You must be a free man to become king
                promote(node, currentNode) //After your child is no longer your responsibility, you ascend the throne

                /* You need successors: steal the old kings children */
                //Legally adopt the children of the old king
                currentNode.left = node.left
                currentNode.right = node.right
                //Tell them that you're their daddy
                node.left?.parent = currentNode
                node.right?.parent = currentNode
            }
        }
        return node //Give them the old king to do what they want with
    }
    /*Continued plot from remove... Let's overthrow the king by messing with the kings father (we'll call him god)... You're `newNode`*/
    private fun promote(oldNode: N, newNode: N?){
        //if god exists... otherwise you are god by default
        if(oldNode.parent != null) {
            if (oldNode.isLeftChild()) { //if the king is seated at the left hand of the father
                oldNode.parent!!.left = newNode //steal the left chair and sit in it to make yourself king!!!
            } else { //if the king is seated at the right hand of the father
                oldNode.parent!!.right = newNode //steal the right chair and sit in it to make yourself king!!!
            }
        } else { //god doesn't exist... time to change that
            rootNode = newNode //welcome to godhood
        }
    }
    fun min(root: N) : N {
        var currentNode = root
        while (currentNode.left != null) currentNode = currentNode.left!!
        return currentNode
    }
    fun max(root: N) : N {
        var currentNode = root
        while (currentNode.right != null) currentNode = currentNode.right!!
        return currentNode
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


    inner class BSTNode(data: E, var parent: N?, left: N? = null, right: N? = null)
        : BinaryTree<E, N>.Node(data, left, right), Comparable<N> {

        fun isLeftChild() : Boolean { return parent?.left == this }

        fun isRightChild() : Boolean { return parent?.right == this }

        override fun compareTo(other: N): Int{
            return data.compareTo(other.data)
        }
    }
}