module com.github.paultsemingfischer.rbtree {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens com.github.paultsemingfischer.rbtree to javafx.fxml;
    exports com.github.paultsemingfischer.rbtree;
    exports com.github.paultsemingfischer.rbtree.graphics;
    opens com.github.paultsemingfischer.rbtree.graphics to javafx.fxml;
}