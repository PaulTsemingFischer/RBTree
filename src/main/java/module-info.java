module com.github.paultsemingfischer.rbtree {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.github.paultsemingfischer.rbtree to javafx.fxml;
    exports com.github.paultsemingfischer.rbtree;
}