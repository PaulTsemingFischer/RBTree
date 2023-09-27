package com.github.paultsemingfischer.rbtree.tree
//Add binary search tree
class RBTree<E : Comparable<E>> : BinaryTree<E, RBTree.RBNode<E>>(){
    override var rootNode: RBNode<E>? = null

    //fix this
    //you had better give me a red black node, or I will be sad
    override fun addNode(node: RBNode<E>) : RBNode<E>{
        TODO("Not yet implemented")
    }

    override fun removeNode(): RBNode<E> {
        TODO("Not yet implemented")
    }

    class RBNode<E : Comparable<E>>(data: E,
                                    private var color: NodeColor,
                                    var parent: RBNode<E>?,
                                    left: RBNode<E>? = null,
                                    right: RBNode<E>? = null
                                        ) : Node<E, RBNode<E>>(data, left, right){

        fun switchColor() : NodeColor{
            color = when (color) {
                NodeColor.RED -> NodeColor.BLACK
                else -> NodeColor.RED
            }
            return color
        }
        override fun isLeaf(): Boolean{
           TODO() //example
        }
    }

}

enum class NodeColor {
    RED,
    BLACK
}