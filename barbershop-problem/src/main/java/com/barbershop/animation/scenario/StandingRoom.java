package com.barbershop.animation.scenario;

import com.barbershop.animation.character.Position;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.Optional;

public class StandingRoom {
    private static final String RED_SEAT_FILE_NAME = "red-seat.png";
    private static final String YELLOW_SEAT_FILE_NAME = "yellow-seat.png";
    public static final double SEAT_WIDTH = 30;
    public static final double SEAT_HEIGHT = 30;
    public static final double SPACE_BETWEEN_SEATS = 50;

    private final Canvas canvas;

    private final Image redSeat;
    private final Image yellowSeat;

    private Seat[] seats;
    private Seat[] placesToWaitStanding;

    public StandingRoom(Canvas canvas, int numberOfSeats, int numberOfPlacesToWaitStanding, Position initialPosition) {
        InputStream redSeatInputStream =
                StandingRoom.class.getClassLoader().getResourceAsStream(RED_SEAT_FILE_NAME);
        this.redSeat = new Image(redSeatInputStream);

        InputStream yellowSeatInputStream =
                StandingRoom.class.getClassLoader().getResourceAsStream(YELLOW_SEAT_FILE_NAME);
        this.yellowSeat = new Image(yellowSeatInputStream);

        this.canvas = canvas;

        this.seats = new Seat[numberOfSeats];
        Position currentPosition = initialPosition;
        for (int i = 0; i < numberOfSeats; i++) {
            seats[i] = new Seat(currentPosition);

            double newX = currentPosition.getX() + SEAT_WIDTH + SPACE_BETWEEN_SEATS;
            double newY = currentPosition.getY();
            if (newX > (canvas.getWidth() - SEAT_WIDTH)) {
                newX = initialPosition.getX();
                newY = currentPosition.getY() + SEAT_HEIGHT + SPACE_BETWEEN_SEATS;
            }
            currentPosition = new Position(newX, newY);
        }

        this.placesToWaitStanding = new Seat[numberOfPlacesToWaitStanding];
        for (int i = 0; i < numberOfPlacesToWaitStanding; i++) {
            placesToWaitStanding[i] = new Seat(currentPosition);

            double newX = currentPosition.getX() + SEAT_WIDTH + SPACE_BETWEEN_SEATS;
            double newY = currentPosition.getY();
            if (newX > (canvas.getWidth() - SEAT_WIDTH)) {
                newX = initialPosition.getX();
                newY = currentPosition.getY() + SEAT_HEIGHT + SPACE_BETWEEN_SEATS;
            }
            currentPosition = new Position(newX, newY);
        }
    }

    public Optional<Seat> getFreePlaceToWaitStanding() {
        synchronized (this.placesToWaitStanding) {
            for(Seat seat : this.placesToWaitStanding) {
                if(!seat.isBusy()) {
                    seat.setBusy(true);
                    return Optional.of(seat);
                }
            }
        }
        return Optional.empty(); // no available seat
    }

    public Optional<Seat> getFreeSeat() {
        synchronized (this.seats) {
            for(Seat seat : this.seats) {
                if(!seat.isBusy()) {
                    seat.setBusy(true);
                    return Optional.of(seat);
                }
            }
        }
        return Optional.empty(); // no available seat
    }

    public void draw() {
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            boolean red = true;
            for (Seat seat : this.seats) {
                Position position = seat.getPosition();
                if (red) {
                    gc.drawImage(redSeat, position.getX(), position.getY(), SEAT_WIDTH, SEAT_HEIGHT);
                } else {
                    gc.drawImage(yellowSeat, position.getX(), position.getY(), SEAT_WIDTH, SEAT_HEIGHT);
                }
                red = !red;
            }
        });
    }

}
