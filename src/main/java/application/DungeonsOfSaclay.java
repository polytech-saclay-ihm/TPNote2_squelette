package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DungeonsOfSaclay extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Model model = new Model();

        FXMLLoader fxmlLoader = new FXMLLoader(DungeonsOfSaclay.class.getResource("View.fxml"));
        Parent root = fxmlLoader.load();
        View view = fxmlLoader.getController();
        Controller controller = new Controller(model, view);
        view.setController(controller);
        controller.afficherMenuPrincipal();
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("Dungeons of Saclay");
        stage.setScene(scene);
        stage.show();

    }

}
