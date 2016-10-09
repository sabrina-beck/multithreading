package com.barbershop.animation.scenario;

import com.barbershop.animation.character.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

import static com.barbershop.animation.character.pokemon.PokemonRandomizer.POKEMON_HEIGHT;

public class Scenario {

    public static final double CARPET_WIDTH = 52.0;
    public static final double CARPET_HEIGHT = 32.0;
    public static final int WALL_HEIGHT = 120;

    private final Image carpet;
    private final Position carpetPosition;
    private final Image wall;

    private final Canvas canvas;

    public Scenario(Canvas canvas) {
        this.canvas = canvas;

        InputStream carpetInputStream = Scenario.class.getClassLoader().getResourceAsStream("carpet.png");
        carpet = new Image(carpetInputStream);
        carpetPosition = new Position(canvas.getWidth() / 2, canvas.getHeight() - CARPET_HEIGHT);

        InputStream wallInputStream = Scenario.class.getClassLoader().getResourceAsStream("wall.png");
        wall = new Image(wallInputStream);
    }

    public Position getPokemonInitialPosition() {
        return new Position(carpetPosition.getX(), carpetPosition.getY() - (POKEMON_HEIGHT - CARPET_HEIGHT));
    }

    public static int getWallHeight() {
        return WALL_HEIGHT;
    }

    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        synchronized (gc) {
            gc.drawImage(carpet, carpetPosition.getX(), carpetPosition.getY(), CARPET_WIDTH, CARPET_HEIGHT);
            gc.drawImage(wall, 0, 0, canvas.getWidth(), WALL_HEIGHT);
        }
    }

}
