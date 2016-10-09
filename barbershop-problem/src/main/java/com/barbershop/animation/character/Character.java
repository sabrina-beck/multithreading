package com.barbershop.animation.character;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class Character {

    public static final int SLEEPING_TIMEOUT = 50;
    private static final double STEP_DELTA = 1;
    private final Map<Orientation, List<CharacterSprite>> spritesByOrientation;
    private Position position;
    private final double width;
    private final double height;

    private Orientation currentOrientation;
    private int spriteIndex;

    public Character(List<CharacterSprite> sprites, Position initialPosition, double width, double height) {
        this.spritesByOrientation = sprites.stream().collect(groupingBy(CharacterSprite::getOrientation));
        this.position = initialPosition;
        this.width = width;
        this.height = height;
        this.spriteIndex = 0;
        this.currentOrientation = Orientation.DOWN;
    }

    public void stay(GraphicsContext map, Orientation orientation) {
        this.disappear(map);
        nextSpriteIndex(orientation);
        this.draw(map);
    }

    public void walkTo(GraphicsContext map, Position destiny) {
        walkHorizontallyTo(map, destiny.getX());
        walkVerticallyTo(map, destiny.getY());
    }

    public Position getPosition() {
        return position;
    }

    private void walkHorizontallyTo(GraphicsContext map, double x) {
        if (this.position.getX() == x) {
            return;
        }

        Orientation orientation;
        double deltaX;
        if (this.position.getX() > x) {
            orientation = Orientation.LEFT;
            deltaX = -STEP_DELTA;
        } else {
            orientation = Orientation.RIGHT;
            deltaX = STEP_DELTA;
        }

        while (this.position.getX() != x) {
            drawMove(map, orientation, deltaX, 0);
        }
    }

    private void walkVerticallyTo(GraphicsContext map, double y) {
        if (this.position.getY() == y) {
            return;
        }

        Orientation orientation;
        double deltaY;
        if (this.position.getY() > y) {
            orientation = Orientation.UP;
            deltaY = -STEP_DELTA;
        } else {
            orientation = Orientation.DOWN;
            deltaY = STEP_DELTA;
        }

        while (this.position.getY() != y) {
            drawMove(map, orientation, 0, deltaY);
        }
    }

    private void drawMove(GraphicsContext map,
                          Orientation orientation,
                          double deltaX,
                          double deltaY) {
        this.disappear(map);
        this.position = new Position(this.position.getX() + deltaX,
                this.position.getY() + deltaY);

        nextSpriteIndex(orientation);

        this.draw(map);
        sleep();
    }

    private void nextSpriteIndex(Orientation orientation) {
        if(currentOrientation != orientation) {
            this.spriteIndex = 0;
            this.currentOrientation = orientation;
        } else {
            List<CharacterSprite> sprites = this.spritesByOrientation.get(orientation);
            this.spriteIndex = (spriteIndex + 1) % sprites.size();
        }
    }

    private void draw(GraphicsContext gc) {
        Platform.runLater(() -> {
            CharacterSprite sprite = this.spritesByOrientation.get(currentOrientation).get(spriteIndex);
            gc.drawImage(sprite.getImage(), this.position.getX(), this.position.getY(),
                    this.width, this.height);
        });
    }

    public void disappear(GraphicsContext gc) {
        Platform.runLater(() -> {
            gc.clearRect(this.position.getX(), this.position.getY(),
                    this.width, this.height);
        });
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
