package com.barbershop.animation.scenario;

import com.barbershop.animation.character.Position;

public class Seat {

    private final Position position;
    private boolean busy;

    public Seat(Position position) {
        this.position = position;
        this.busy = false;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
