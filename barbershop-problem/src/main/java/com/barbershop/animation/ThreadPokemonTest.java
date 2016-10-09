package com.barbershop.animation;

import com.barbershop.animation.character.Character;
import com.barbershop.animation.character.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ThreadPokemonTest implements Runnable {

    private final Character character;
    private final Canvas canvas;

    public ThreadPokemonTest(Canvas canvas, Character character) {
        this.canvas = canvas;
        this.character = character;
    }

    @Override
    public void run() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        character.walkTo(gc, new Position(0, 0));
    }
}
