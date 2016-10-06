package com.barbershop.animation;

import com.barbershop.animation.sprite.SpriteSheet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;

public class AnimationMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Pokemon Center Problem");

        Canvas canvas = new Canvas(500, 500);

        Pane pane = new Pane();
        pane.getChildren().add(canvas);
        primaryStage.setScene(new Scene(pane, 500, 500));

        primaryStage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        draw(gc);
    }

    private void draw(GraphicsContext gc) {
        InputStream spritesInputStream = AnimationMain.class.getClassLoader().getResourceAsStream("sprites-kanto-transparent.png");
        SpriteSheet spriteSheet = new SpriteSheet(spritesInputStream);
        spriteSheet.getSprite(0, 0, 32, 32, gc);
    }
}
