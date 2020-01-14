package com.blackwhite;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {

//    private static FxController controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(Main.class.getResourceAsStream("/reception.fxml"));
//        controller = loader.getController();
        Scene scene = new Scene(root, 900, 500);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("style.css")).toExternalForm());
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    @Override
//    public void stop() {
//        try {
//            controller.closeConnection();
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
