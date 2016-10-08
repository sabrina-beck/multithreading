package com.barbershop.animation;

import com.barbershop.animation.sprite.*;
import com.barbershop.animation.sprite.Character;
import javafx.scene.canvas.GraphicsContext;

import java.io.InputStream;
import java.util.List;

public class ThreadLouca implements Runnable {

    private final Character character;
    private GraphicsContext gc;

    public ThreadLouca(GraphicsContext gc) {
        this.gc = gc;

        InputStream spritesInputStream = AnimationMain.class.getClassLoader().getResourceAsStream
                ("sprites-kanto-transparent.png");
        CharacterSpriteSheet spriteSheet = new CharacterSpriteSheet(spritesInputStream);
        character = new CharacterRandomizer().newCharacter();
    }

    @Override
    public void run() {
        character.walkTo(gc, new Position(20, 20));
        character.walkTo(gc, new Position(0, 0));
    }
}
