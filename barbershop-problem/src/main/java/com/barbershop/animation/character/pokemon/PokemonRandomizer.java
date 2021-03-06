package com.barbershop.animation.character.pokemon;

import com.barbershop.Main;
import com.barbershop.animation.character.Character;
import com.barbershop.animation.character.CharacterSprite;
import com.barbershop.animation.character.Position;

import java.io.InputStream;
import java.lang.*;
import java.util.List;
import java.util.Random;

public class PokemonRandomizer {

    public static final String SPRITES_SHEET_FILE = "sprites-kanto-transparent.png";
    private static final Random random = new Random();

    public static final int SPRITE_WIDTH = 32;
    public static final int SPRITE_HEIGHT = 32;
    public static final int POKEMON_WIDTH = 50;
    public static final int POKEMON_HEIGHT = 50;

    private final PokemonSpriteSheet spriteSheet;
    private Position initialPosition;

    public PokemonRandomizer(Position initialPosition) {
        this.initialPosition = initialPosition;

        InputStream spritesInputStream = Main.class.getClassLoader().getResourceAsStream
                (SPRITES_SHEET_FILE);
        spriteSheet = new PokemonSpriteSheet(spritesInputStream);
    }

    public Character newPokemon() {
        int indexY = random.nextInt(11);
        int indexX = random.nextInt(15);
        if(indexY == 10) {
            indexX = random.nextInt(3);
        }

        int x = indexX * 2 * SPRITE_WIDTH + indexX,
            y = indexY * 4 * SPRITE_HEIGHT + indexY;

        List<CharacterSprite> sprites = spriteSheet.getCharacterSprites(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);

        return new Character.Builder()
                .addSprites(sprites)
                .initialPosition(initialPosition)
                .width(POKEMON_WIDTH)
                .height(POKEMON_HEIGHT)
                .build();
    }
}
