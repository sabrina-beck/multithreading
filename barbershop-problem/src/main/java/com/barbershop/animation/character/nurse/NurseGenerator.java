package com.barbershop.animation.character.nurse;


import com.barbershop.animation.character.Character;
import com.barbershop.animation.character.CharacterSprite;
import com.barbershop.animation.character.Position;

import java.io.InputStream;
import java.util.List;

public class NurseGenerator {

    private static final String SPRITES_FILE_NAME = "sprites-joy.png";
    private static final int SPRITE_WIDTH = 68;
    private static final int SPRITE_HEIGHT = 72;
    private static final int SPRITE_X = 0;
    private static final int SPRITE_Y = 0;
    public static final int JOY_WIDTH = 50;
    public static final int JOY_HEIGHT = 53;

    private final NurseSpriteSheet spriteSheet;
    private Position initialPosition;

    public NurseGenerator(Position initialPosition) {
        this.initialPosition = initialPosition;

        InputStream spriteSheetInputStream =
                NurseGenerator.class.getClassLoader().getResourceAsStream(SPRITES_FILE_NAME);
        this.spriteSheet = new NurseSpriteSheet(spriteSheetInputStream);
    }

    public Character newJoy() {
        List<CharacterSprite> sprites = spriteSheet.getNurseSprites(SPRITE_X, SPRITE_Y, SPRITE_WIDTH, SPRITE_HEIGHT);

        return new Character.Builder()
                .addSprites(sprites)
                .initialPosition(initialPosition)
                .width(JOY_WIDTH)
                .height(JOY_HEIGHT)
                .build();
    }
}
