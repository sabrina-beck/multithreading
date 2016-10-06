package com.barbershop.animation.sprite;

import javafx.scene.image.Image;

import java.util.List;

public class Sprite {

    private final List<Image> framesUp;
    private final List<Image> framesDown;
    private final List<Image> framesLeft;
    private final List<Image> framesRight;

    protected Sprite(List<Image> framesUp, List<Image> framesDown, List<Image> framesLeft, List<Image> framesRight) {
        this.framesUp = framesUp;
        this.framesDown = framesDown;
        this.framesLeft = framesLeft;
        this.framesRight = framesRight;
    }

    public List<Image> getFramesUp() {
        return framesUp;
    }

    public List<Image> getFramesDown() {
        return framesDown;
    }

    public List<Image> getFramesLeft() {
        return framesLeft;
    }

    public List<Image> getFramesRight() {
        return framesRight;
    }
}
