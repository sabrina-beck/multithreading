package com.barbershop.animation.sprite;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpriteBuilder {

    private final List<Image> framesUp;
    private final List<Image> framesDown;
    private final List<Image> framesLeft;
    private final List<Image> framesRight;

    public SpriteBuilder() {
        this.framesUp = new ArrayList<Image>();
        this.framesDown = new ArrayList<Image>();
        this.framesLeft = new ArrayList<Image>();
        this.framesRight = new ArrayList<Image>();
    }

    public SpriteBuilder addFramesUp(Image... frame) {
        this.framesUp.addAll(Arrays.asList(frame));
        return this;
    }

    public SpriteBuilder addFramesDown(Image... frame) {
        this.framesDown.addAll(Arrays.asList(frame));
        return this;
    }

    public SpriteBuilder addFramesLeft(Image... frame) {
        this.framesLeft.addAll(Arrays.asList(frame));
        return this;
    }

    public SpriteBuilder addFramesRight(Image... frame) {
        this.framesRight.addAll(Arrays.asList(frame));
        return this;
    }

    public Sprite build() {
        return new Sprite(framesUp, framesDown, framesLeft, framesRight);
    }
}
