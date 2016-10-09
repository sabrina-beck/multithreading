package com.barbershop.animation;

import com.barbershop.animation.scenario.Scenario;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;

public class AnimationMain extends Application {

    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 400;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Pokemon Center Problem");

        Canvas animationLayer = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        Canvas scenarioLayer = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);

        InputStream floorIS = Scenario.class.getClassLoader().getResourceAsStream("floor.png");
        BackgroundImage backgroundImage = new BackgroundImage(new Image(floorIS),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Pane pane = new Pane();
        pane.setBackground(new Background(backgroundImage));

        pane.getChildren().add(scenarioLayer);
        pane.getChildren().add(animationLayer);
        Scene scene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.show();

        new Thread(new PokemonCenterProblem(scenarioLayer, animationLayer)).start();
    }

}
