package com.github.paultsemingfischer.rbtree.graphics

import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Text

class NodeObject(x: Double, y: Double, radius: Double, labelText: String?, color: Color?) : Circle(x, y, radius) {
    private val label: Text

    init {
        fill = color
        label = Text(x - radius / 2, y + radius + 15, labelText)
        label.fill = Color.GREY
        label.toFront()
    }
}