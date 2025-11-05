module com.mycompany.trabalho.arquivo {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.trabalho.arquivo to javafx.fxml;
    exports com.mycompany.trabalho.arquivo;
}
