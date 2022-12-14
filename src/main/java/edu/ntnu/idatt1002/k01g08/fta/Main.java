package edu.ntnu.idatt1002.k01g08.fta;

import edu.ntnu.idatt1002.k01g08.fta.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Entry page for the application
 */
public class Main extends Application {

    /**
     * Starts the application
     * @param stage Stage sent by JavaFX
     */
    @Override
    public void start(Stage stage) {
        try{
            FXMLLoader loader = SceneManager.getLoader("main");
            SceneManager.setScene(new Scene(loader.load()));
            SceneManager.setCurrentScene("main");
            stage.setScene(SceneManager.getScene());
            stage.setTitle("Football Tournament Application");
            Image image = new Image(Main.class.getResourceAsStream("img/logo.png"));
            stage.getIcons().add(image);
            stage.show();
        }catch (IOException i){
            i.printStackTrace();
        }
    }

    /**
     * Main
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);
    }
}