package com.barbershop.animation;

import com.barbershop.animation.sprite.Character;
import com.barbershop.animation.sprite.CharacterSprite;
import com.barbershop.animation.sprite.CharacterSpriteSheet;
import com.barbershop.animation.sprite.Position;
import javafx.scene.canvas.GraphicsContext;

import java.io.InputStream;
import java.util.List;

public class ThreadLouca implements Runnable {

    private final Character character;
    private GraphicsContext gc;

    public ThreadLouca(GraphicsContext gc) {
        this.gc = gc;

        InputStream spritesInputStream = AnimationMain.class.getClassLoader().getResourceAsStream("sprites-kanto-transparent.png");
        CharacterSpriteSheet spriteSheet = new CharacterSpriteSheet(spritesInputStream);
        int x = 8*32 + 4, y = 0, width = 32, height = 32;

        List<CharacterSprite> sprites = spriteSheet.getCharacterSprites(x, y, width, height);

        character = new Character.Builder()
                .addSprites(sprites)
                .initialPosition(new Position(0, 0))
                .width(50)
                .height(50)
                .build();
    }

    @Override
    public void run() {
        character.walkTo(gc, new Position(20, 20));
    }
}
