package com.barbershop;

import com.barbershop.animation.scenario.PokemonCenter;
import com.barbershop.problem.PokemonCenterProblemProducer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {

    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 450;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Pokemon Center Problem");

        Canvas nurseLayer = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        Canvas pokemonLayer = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        Canvas scenarioLayer = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);

        InputStream floorIS = PokemonCenter.class.getClassLoader().getResourceAsStream("floor.png");
        BackgroundImage backgroundImage = new BackgroundImage(new Image(floorIS),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Pane pane = new Pane();
        pane.setBackground(new Background(backgroundImage));

        pane.getChildren().add(scenarioLayer);
        pane.getChildren().add(nurseLayer);
        pane.getChildren().add(pokemonLayer);
        Scene scene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.show();

        new Thread(new PokemonCenterProblemProducer(scenarioLayer, pokemonLayer, nurseLayer)).start();
    }

}
