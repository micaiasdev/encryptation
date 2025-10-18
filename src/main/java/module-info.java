module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.util;
    requires org.bouncycastle.pkix;
    requires javafx.graphics;

    opens com.example to javafx.fxml;

    exports com.example;
}
