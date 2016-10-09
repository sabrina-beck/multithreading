package com.barbershop.animation;

import com.barbershop.animation.character.Character;
import com.barbershop.animation.character.Orientation;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ThreadJoyTest implements Runnable {
    private final Character character;
    private Canvas canvas;

    public ThreadJoyTest(Canvas canvas, Character character) {
        this.canvas = canvas;
        this.character = character;
    }

    @Override
    public void run() {
        GraphicsContext map = canvas.getGraphicsContext2D();
        while (true) {
            character.stay(map);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
