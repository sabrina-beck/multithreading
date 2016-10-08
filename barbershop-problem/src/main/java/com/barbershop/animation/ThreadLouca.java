package com.barbershop.animation;

import com.barbershop.animation.character.Character;
import com.barbershop.animation.character.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ThreadLouca implements Runnable {

    private final Character character;
    private Canvas canvas;

    public ThreadLouca(Canvas canvas, Character character) {
        this.canvas = canvas;
        this.character = character;
    }

    @Override
    public void run() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        character.walkTo(gc, new Position(0, 0));
    }
}
