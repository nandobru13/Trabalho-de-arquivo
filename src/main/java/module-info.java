module com.uece.projects.leitor_de_gabaritos {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.uece.projects.leitor_de_gabaritos to javafx.fxml;
    exports com.uece.projects.leitor_de_gabaritos;
}
