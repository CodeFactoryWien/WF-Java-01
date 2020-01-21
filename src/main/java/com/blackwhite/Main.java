package com.blackwhite;

import com.blackwhite.ctrl.FxController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {

    private static FxController controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(Main.class.getResourceAsStream("/reception_scenebuilder.fxml"));
        controller = loader.getController();
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setMaxWidth(600);
        primaryStage.setMaxHeight(400);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("style.css")).toExternalForm());
        primaryStage.setTitle("Hotel Black & White");
        primaryStage.setScene(scene);
        //Icon
        InputStream input = this.getClass().getResourceAsStream("/logo_icon.png");
        Image image = new Image(input);
        primaryStage.getIcons().add(image);
        primaryStage.show();
    }

    @Override
    public void stop() {
        try {
            controller.closeConnection();
        } catch (SQLException e){
            // do nothing
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
