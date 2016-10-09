package com.barbershop.animation.scenario;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class WaitingRoom {
    private static final String RED_SEAT_FILE_NAME = "red-seat.png";
    private static final String YELLOW_SEAT_FILE_NAME = "yellow-seat.png";

    private final Canvas canvas;

    private final Image redSeat;
    private final Image yellowSeat;

    private Seat[] seats;

    public WaitingRoom(Canvas canvas, int numberOfSeats) {
        InputStream redSeatInputStream =
                WaitingRoom.class.getClassLoader().getResourceAsStream(RED_SEAT_FILE_NAME);
        this.redSeat = new Image(redSeatInputStream);

        InputStream yellowSeatInputStream =
                WaitingRoom.class.getClassLoader().getResourceAsStream(YELLOW_SEAT_FILE_NAME);
        this.yellowSeat = new Image(yellowSeatInputStream);

        this.canvas = canvas;

        this.seats = new Seat[numberOfSeats];
    }

    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(redSeat, 100, 100, redSeat.getWidth(), redSeat.getHeight());
    }

}
