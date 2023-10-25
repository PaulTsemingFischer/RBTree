package com.github.paultsemingfischer.rbtree.graphics

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.Pane

class Controller {
    @FXML
    private val welcomeText: Label? = null

    @FXML
    private val nodePane: Pane? = null // This is a Pane in your FXML file where you want to draw the nodes.
    @FXML
    protected fun onHelloButtonClick() {
        welcomeText!!.text = "Welcome to JavaFX Application!"
    }
}