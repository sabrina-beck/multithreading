package com.barbershop.animation.scenario;

import com.barbershop.animation.character.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class Scenario {

    private static final double CARPET_WIDTH = 52.0;
    private static final double CARPET_HEIGHT = 32.0;

    private final Image carpet;
    private final Position carpetPosition;
    private Canvas canvas;

    public Scenario(Canvas canvas) {
        this.canvas = canvas;

        InputStream imageInputStream = Scenario.class.getClassLoader().getResourceAsStream("carpet.png");
        carpet = new Image(imageInputStream);
        carpetPosition = new Position(canvas.getWidth() / 2, canvas.getHeight() - CARPET_HEIGHT);
    }

    public Position getCarpetPosition() {
        return carpetPosition;
    }

    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(carpet, carpetPosition.getX(), carpetPosition.getY(), CARPET_WIDTH, CARPET_HEIGHT);
    }

}
