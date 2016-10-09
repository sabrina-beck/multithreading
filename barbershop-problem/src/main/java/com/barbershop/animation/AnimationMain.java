package com.barbershop.animation;

import com.barbershop.animation.character.nurse.NurseGenerator;
import com.barbershop.animation.character.pokemon.PokemonRandomizer;
import com.barbershop.animation.character.Position;
import com.barbershop.animation.scenario.Scenario;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;

import static com.barbershop.animation.character.pokemon.PokemonRandomizer.POKEMON_HEIGHT;
import static com.barbershop.animation.scenario.Scenario.CARPET_HEIGHT;

public class AnimationMain extends Application {

    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 300;

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

        Scenario scenario = new Scenario(scenarioLayer);
        scenario.draw();

        Position initialPosition = scenario.getPokemonInitialPosition();
        PokemonRandomizer characterRandomizer = new PokemonRandomizer(initialPosition);
        //NurseGenerator nurseGenerator = new NurseGenerator(initialPosition);

        Thread thread = new Thread(new ThreadLouca(animationLayer, characterRandomizer.newPokemon()));
        //Thread thread = new Thread(new ThreadLouca(animationLayer, nurseGenerator.newJoy()));
        thread.start();
    }

}
