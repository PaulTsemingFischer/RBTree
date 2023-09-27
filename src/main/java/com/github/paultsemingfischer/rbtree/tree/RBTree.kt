package com.github.paultsemingfischer.rbtree.tree

class RBTree<E : Comparable<E>> : BinaryTree<E>(){
    override val rootNode: RBNode<E> = TODO()

    //you had better give me a red black node, or I will be sad
    override fun addNode(node: Node<E>) {
        TODO("Not yet implemented")
    }

    override fun removeNode(): Node<E> {
        TODO("Not yet implemented")
    }

    inner class RBNode<E>(data: E, var color: NodeColor, var parent: RBNode<E>?, left: RBNode<E>? = null, right: RBNode<E>? = null) : Node<E>(data, left, right){ //is there going to be a problem here where RBNode isn't a Node, so it won't be able to be supered
        fun switchColor() : NodeColor{
            color = when (color) {
                NodeColor.RED -> NodeColor.BLACK
                else -> NodeColor.RED
            }
            return color
        }
    }

}

enum class NodeColor {
    RED,
    BLACK
}