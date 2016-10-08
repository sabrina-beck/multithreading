package com.barbershop.animation.sprite;

import javafx.scene.image.Image;

public class CharacterSprite {

    private Image image;
    private final Orientation orientation;

    public CharacterSprite(Image image, Orientation orientation) {
        this.image = image;
        this.orientation = orientation;
    }

    public Image getImage() {
        return image;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
