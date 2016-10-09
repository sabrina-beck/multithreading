package com.barbershop.animation.character.nurse;

import com.barbershop.animation.character.CharacterSprite;
import com.barbershop.animation.character.Orientation;
import com.barbershop.animation.sprite.SpriteSheet;
import com.google.common.collect.ImmutableList;

import java.io.InputStream;
import java.util.List;

public class NurseSpriteSheet extends SpriteSheet {
    public NurseSpriteSheet(InputStream inputStream) {
        super(inputStream);
    }

    public List<CharacterSprite> getNurseSprites(int x, int y, int width, int height) {
        ImmutableList.Builder<CharacterSprite> builder = ImmutableList.builder();

        builder.add(new CharacterSprite(getSprite(x, y, width, height), Orientation.DOWN));
        builder.add(new CharacterSprite(getSprite(x + width, y, width, height), Orientation.DOWN));
        builder.add(new CharacterSprite(getSprite(x + 2 * width, y, width, height), Orientation.DOWN));
        builder.add(new CharacterSprite(getSprite(x + 3 * width, y, width, height), Orientation.DOWN));

        builder.add(new CharacterSprite(getSprite(x, y + height, width, height), Orientation.LEFT));
        builder.add(new CharacterSprite(getSprite(x + width, y + height, width, height), Orientation.LEFT));
        builder.add(new CharacterSprite(getSprite(x + 2 * width, y + height, width, height), Orientation.LEFT));
        builder.add(new CharacterSprite(getSprite(x + 3 * width, y + height, width, height), Orientation.LEFT));

        builder.add(new CharacterSprite(getSprite(x, y + 2 * height, width, height), Orientation.RIGHT));
        builder.add(new CharacterSprite(getSprite(x + width, y + 2 * height, width, height), Orientation.RIGHT));
        builder.add(new CharacterSprite(getSprite(x + 2 * width, y + 2 * height, width, height), Orientation.RIGHT));
        builder.add(new CharacterSprite(getSprite(x + 3 * width, y + 2 * height, width, height), Orientation.RIGHT));

        builder.add(new CharacterSprite(getSprite(x, y + 3 * height, width, height), Orientation.UP));
        builder.add(new CharacterSprite(getSprite(x + width, y + 3 * height, width, height), Orientation.UP));
        builder.add(new CharacterSprite(getSprite(x + 2 * width, y + 3 * height, width, height), Orientation.UP));
        builder.add(new CharacterSprite(getSprite(x + 3 * width, y + 3 * height, width, height), Orientation.UP));

        return builder.build();
    }

}
