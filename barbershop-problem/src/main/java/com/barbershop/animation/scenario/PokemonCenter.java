package com.barbershop.animation.scenario;

import com.barbershop.animation.character.Position;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

import static com.barbershop.animation.character.pokemon.PokemonRandomizer.POKEMON_HEIGHT;

public class PokemonCenter {


    public static final String CARPET_FILE_NAME = "carpet.png";
    public static final String WALL_FILE_NAME = "wall.png";
    public static final String CHAIR_FILE_NAME = "stool.png";
    public static final String PAYMENT_TABLE_FILE_NAME = "table.png";

    public static final double CARPET_WIDTH = 52.0;
    public static final double CARPET_HEIGHT = 32.0;
    public static final int WALL_HEIGHT = 120;
    public static final int CHAIR_WIDTH = 30;
    public static final int CHAIR_HEIGHT = 24;
    public static final int TABLE_WIDTH = 50;
    public static final int TABLE_HEIGHT = 30;
    public static final Position TABLE_POSITION = new Position(400, WALL_HEIGHT);

    private final Image wall;
    private final Image chair;
    private final Image carpet;
    private final Image table;

    private final Position carpetPosition;
    private final Canvas canvas;
    private final StandingRoom standingRoom;

    public PokemonCenter(Canvas canvas, int numberOfSeats, Position standingRoomPosition) {
        this.canvas = canvas;

        InputStream carpetInputStream = PokemonCenter.class.getClassLoader().getResourceAsStream(CARPET_FILE_NAME);
        carpet = new Image(carpetInputStream);
        carpetPosition = new Position(canvas.getWidth() / 2, canvas.getHeight() - CARPET_HEIGHT);

        InputStream wallInputStream = PokemonCenter.class.getClassLoader().getResourceAsStream(WALL_FILE_NAME);
        wall = new Image(wallInputStream);

        InputStream chairInputStream = PokemonCenter.class.getClassLoader().getResourceAsStream(CHAIR_FILE_NAME);
        chair = new Image(chairInputStream);

        InputStream tableInputStream = PokemonCenter.class.getClassLoader().getResourceAsStream
                (PAYMENT_TABLE_FILE_NAME);
        table = new Image(tableInputStream);

        standingRoom = new StandingRoom(canvas, numberOfSeats, standingRoomPosition);
    }

    public Position getPokemonInitialPosition() {
        return new Position(carpetPosition.getX(), carpetPosition.getY() - (POKEMON_HEIGHT - CARPET_HEIGHT));
    }

    public static int getNurseInitialPosition(int nurseHeight) {
        return WALL_HEIGHT - nurseHeight;
    }

    public StandingRoom getStandingRoom() {
        return standingRoom;
    }

    public static Position getTablePosition() {
        return TABLE_POSITION;
    }

    public void draw() {
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(carpet, carpetPosition.getX(), carpetPosition.getY(), CARPET_WIDTH, CARPET_HEIGHT);
            gc.drawImage(wall, 0, 0, canvas.getWidth(), WALL_HEIGHT);
            gc.drawImage(table, TABLE_POSITION.getX(), TABLE_POSITION.getY(), TABLE_WIDTH, TABLE_HEIGHT);
        });

        standingRoom.draw();
    }

    public void addNurseChair(Position position) {
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(chair, position.getX(), position.getY(), CHAIR_WIDTH, CHAIR_HEIGHT);
        });
    }

}
