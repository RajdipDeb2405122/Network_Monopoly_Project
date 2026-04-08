module demof {
    //Core JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.swing;

    //Third-party libraries
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    //Opening packages for FXML reflection
    opens FXML_Controllers to javafx.fxml;
    opens application to javafx.fxml;

    //Externally Package Exportation
    exports FXML_Controllers;
    exports application;
}
