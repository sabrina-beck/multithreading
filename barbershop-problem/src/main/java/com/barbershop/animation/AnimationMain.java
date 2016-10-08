package com.barbershop.animation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AnimationMain extends Application {

    private Canvas canvas;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Pokemon Center Problem");

        canvas = new Canvas(500, 500);

        Pane pane = new Pane();
        pane.getChildren().add(canvas);
        scene = new Scene(pane, 500, 500);
        primaryStage.setScene(scene);

        primaryStage.show();

        Thread thread = new Thread(new ThreadLouca(canvas.getGraphicsContext2D()));
        thread.start();
    }

}
