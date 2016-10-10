package com.barbershop.animation.character.nurse;

import com.barbershop.animation.character.CharacterSprite;
import com.barbershop.animation.character.Orientation;
import com.barbershop.animation.sprite.SpriteSheet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NurseSpriteSheet extends SpriteSheet {
    public NurseSpriteSheet(InputStream inputStream) {
        super(inputStream);
    }

    public List<CharacterSprite> getNurseSprites(int x, int y, int width, int height) {
        List<CharacterSprite> result = new ArrayList<>();

        result.add(new CharacterSprite(getSprite(x, y, width, height), Orientation.DOWN));
        result.add(new CharacterSprite(getSprite(x + width, y, width, height), Orientation.DOWN));
        result.add(new CharacterSprite(getSprite(x + 2 * width, y, width, height), Orientation.DOWN));
        result.add(new CharacterSprite(getSprite(x + 3 * width, y, width, height), Orientation.DOWN));

        result.add(new CharacterSprite(getSprite(x, y + height, width, height), Orientation.LEFT));
        result.add(new CharacterSprite(getSprite(x + width, y + height, width, height), Orientation.LEFT));
        result.add(new CharacterSprite(getSprite(x + 2 * width, y + height, width, height), Orientation.LEFT));
        result.add(new CharacterSprite(getSprite(x + 3 * width, y + height, width, height), Orientation.LEFT));

        result.add(new CharacterSprite(getSprite(x, y + 2 * height, width, height), Orientation.RIGHT));
        result.add(new CharacterSprite(getSprite(x + width, y + 2 * height, width, height), Orientation.RIGHT));
        result.add(new CharacterSprite(getSprite(x + 2 * width, y + 2 * height, width, height), Orientation.RIGHT));
        result.add(new CharacterSprite(getSprite(x + 3 * width, y + 2 * height, width, height), Orientation.RIGHT));

        result.add(new CharacterSprite(getSprite(x, y + 3 * height, width, height), Orientation.UP));
        result.add(new CharacterSprite(getSprite(x + width, y + 3 * height, width, height), Orientation.UP));
        result.add(new CharacterSprite(getSprite(x + 2 * width, y + 3 * height, width, height), Orientation.UP));
        result.add(new CharacterSprite(getSprite(x + 3 * width, y + 3 * height, width, height), Orientation.UP));

        return result;
    }

}
