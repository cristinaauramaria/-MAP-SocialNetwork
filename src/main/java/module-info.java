module socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens socialnetwork to javafx.fxml,java.base;
    opens socialnetwork.domain to javafx.base;
    exports socialnetwork;
    exports socialnetwork.controler;
    opens socialnetwork.controler to javafx.fxml;
}