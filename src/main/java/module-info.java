module application {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive java.xml;

    opens application to javafx.fxml;

    exports application;
}