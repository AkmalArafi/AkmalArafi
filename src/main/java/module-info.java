module com.example.selectinsert {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.selectinsert to javafx.fxml;
    exports com.example.selectinsert;
    exports com.example.selectinsert.Controller;
    opens com.example.selectinsert.Controller to javafx.fxml;
}