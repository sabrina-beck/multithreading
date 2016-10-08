package com.barbershop.animation.character;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class Character {

    public static final int SLEEPING_TIMEOUT = 100;
    private static final double STEP_DELTA = 1;
    private final Map<Orientation, List<CharacterSprite>> spritesByOrientation;
    private Position currentPosition;
    private final double width;
    private final double height;
    private int spriteIndex;

    public Character(List<CharacterSprite> sprites, Position initialPosition, double width, double height) {
        this.spritesByOrientation = sprites.stream().collect(groupingBy(CharacterSprite::getOrientation));
        this.currentPosition = initialPosition;
        this.width = width;
        this.height = height;
        this.spriteIndex = 0;
    }

    public void walkTo(GraphicsContext map, Position destiny) {
        walkHorizontallyTo(map, destiny.getX());
        walkVerticallyTo(map, destiny.getY());
    }

    private void walkHorizontallyTo(GraphicsContext map, double x) {
        if (this.currentPosition.getX() == x) {
            return;
        }

        Orientation orientation;
        double deltaX;
        if (this.currentPosition.getX() > x) {
            orientation = Orientation.LEFT;
            deltaX = -STEP_DELTA;
        } else {
            orientation = Orientation.RIGHT;
            deltaX = STEP_DELTA;
        }

        while (this.currentPosition.getX() != x) {
            drawMove(map, orientation, deltaX, 0);
        }
    }

    private void walkVerticallyTo(GraphicsContext map, double y) {
        if (this.currentPosition.getY() == y) {
            return;
        }

        Orientation orientation;
        double deltaY;
        if (this.currentPosition.getY() > y) {
            orientation = Orientation.UP;
            deltaY = -STEP_DELTA;
        } else {
            orientation = Orientation.DOWN;
            deltaY = STEP_DELTA;
        }

        while (this.currentPosition.getY() != y) {
            drawMove(map, orientation, 0, deltaY);
        }
    }

    private void drawMove(GraphicsContext map,
                          Orientation orientation,
                          double deltaX,
                          double deltaY) {
        this.clear(map);
        this.currentPosition = new Position(this.currentPosition.getX() + deltaX,
                this.currentPosition.getY() + deltaY);

        List<CharacterSprite> sprites = this.spritesByOrientation.get(orientation);
        this.spriteIndex = (spriteIndex + 1) % sprites.size();

        this.draw(map, orientation);
        sleep();
    }

    private void draw(GraphicsContext gc, Orientation orientation) {
        CharacterSprite sprite = this.spritesByOrientation.get(orientation).get(spriteIndex);
        gc.drawImage(sprite.getImage(), this.currentPosition.getX(), this.currentPosition.getY(),
                this.width, this.height);
    }

    private void clear(GraphicsContext gc) {
        gc.clearRect(this.currentPosition.getX(), this.currentPosition.getY(),
                this.width, this.height);
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEPING_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class Builder {

        private final List<CharacterSprite> sprites;
        private Position position;
        private double width;
        private double height;

        public Builder() {
            this.sprites = new ArrayList<>();
        }

        public Builder addSprites(List<CharacterSprite> sprite) {
            this.sprites.addAll(sprite);
            return this;
        }

        public Builder initialPosition(Position position) {
            this.position = position;
            return this;
        }

        public Builder width(double width) {
            this.width = width;
            return this;
        }

        public Builder height(double height) {
            this.height = height;
            return this;
        }

        public Character build() {
            return new Character(this.sprites, this.position, this.width, this.height);
        }
    }


}
