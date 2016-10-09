package com.barbershop.animation.character.pokemon;

import com.barbershop.animation.character.CharacterSprite;
import com.barbershop.animation.character.Orientation;
import com.barbershop.animation.sprite.SpriteSheet;
import com.google.common.collect.ImmutableList;

import java.io.InputStream;
import java.util.List;

public class PokemonSpriteSheet extends SpriteSheet {
    public PokemonSpriteSheet(InputStream inputStream) {
        super(inputStream);
    }

    public List<CharacterSprite> getCharacterSprites(int x, int y, int width, int height) {
        ImmutableList.Builder<CharacterSprite> sprites = ImmutableList.builder();

        sprites.add(new CharacterSprite(getSprite(x, y, width, height), Orientation.UP));
        sprites.add(new CharacterSprite(getSprite(x, y + height, width, height), Orientation.UP));

        sprites.add(new CharacterSprite(getSprite(x, y + 2 * height, width, height), Orientation.DOWN));
        sprites.add(new CharacterSprite(getSprite(x, y + 3 * height, width, height), Orientation.DOWN));

        sprites.add(new CharacterSprite(getSprite(x + width, y, width, height), Orientation.LEFT));
        sprites.add(new CharacterSprite(getSprite(x + width, y + height, width, height), Orientation.LEFT));

        sprites.add(new CharacterSprite(getSprite(x + width, y + 2 * height, width, height), Orientation.RIGHT));
        sprites.add(new CharacterSprite(getSprite(x + width, y + 3 * height, width, height), Orientation.RIGHT));

        return sprites.build();
    }

}
