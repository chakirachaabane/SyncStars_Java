module com.example.pidevproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.persistence;
    requires java.validation;
    requires java.desktop;
    requires org.apache.pdfbox;

    opens com.example.pidevproject to javafx.fxml;
    exports com.example.pidevproject;
    exports Controllers;
    opens Controllers to javafx.fxml;
}