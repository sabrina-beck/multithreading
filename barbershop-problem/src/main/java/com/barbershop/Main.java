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
import java.security.InvalidParameterException;

public class Main extends Application {

    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 450;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Pokemon Center Problem");

        InputStream floorIS = PokemonCenter.class.getClassLoader().getResourceAsStream("floor.png");
        BackgroundImage backgroundImage = new BackgroundImage(new Image(floorIS),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Pane pane = new Pane();
        pane.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.show();

        int nurses = Integer.valueOf(getParameters().getUnnamed().get(0));
        int seats = Integer.valueOf(getParameters().getUnnamed().get(1));

        if (nurses > 6 || nurses < 1 || seats < 1 || seats > 18)
            throw new InvalidParameterException();

        Thread a = new Thread(new PokemonCenterProblemProducer(pane, nurses, seats));
        a.setDaemon(true);
        a.start();
    }

}
