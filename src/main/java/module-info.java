module com.uece.projects.leitor_de_gabaritos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.uece.projects.leitor_de_gabaritos to javafx.fxml;
    exports com.uece.projects.leitor_de_gabaritos;
}
