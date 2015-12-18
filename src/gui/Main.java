package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = getClass().getResource("gui.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = (Parent) fxmlLoader.load(location.openStream());

        Controller controller = (Controller)fxmlLoader.getController();
        controller.init();

        //Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));

        primaryStage.setTitle("Sortiermaschine");
        primaryStage.setScene(new Scene(root, 610, 480));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
