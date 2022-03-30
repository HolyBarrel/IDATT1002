package edu.ntnu.idatt1002.k01g08.fta;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Scene scene;
    private static Stage stage;
    public SceneManager(){

    }
    public static void setView(String viewFxml) throws IOException {
        FXMLLoader loader = getFXMLLoader(viewFxml);
        scene.setRoot(loader.load());
    }
    public static FXMLLoader getFXMLLoader(String name) {
        String path = String.format("%s.fxml", name);
        System.out.println(path);
        return new FXMLLoader(Main.class.getResource(path));
    }

    public static void setScene(Scene scene) {
        SceneManager.scene = scene;
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }
}