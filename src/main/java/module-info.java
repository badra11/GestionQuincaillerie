module com.bad.gestionquincaillerie {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.apache.pdfbox;
    requires jbcrypt;

    opens com.bad.gestionquincaillerie.main to javafx.fxml;
    opens com.bad.gestionquincaillerie.controllers to javafx.fxml;
    opens com.bad.gestionquincaillerie.models to javafx.fxml, javafx.base;
    
    
    exports com.bad.gestionquincaillerie.main;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
}
