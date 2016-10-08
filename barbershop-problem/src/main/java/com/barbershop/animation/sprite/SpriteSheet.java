package com.barbershop.animation.sprite;

import javafx.scene.image.*;

import java.io.InputStream;

public class SpriteSheet {
    private final Image sheet;
    private PixelReader reader;

    public SpriteSheet(InputStream inputStream) {
        sheet = new Image(inputStream);
        reader = sheet.getPixelReader();
    }

    public Image getSprite(int x, int y, int width, int height) {
        return subimage(x, y, width, height);
    }

    private Image subimage(int x, int y, int width, int height) {
        WritableImage subimage = new WritableImage(width, height);
        PixelWriter writer = subimage.getPixelWriter();

        byte[] buffer = new byte[width * height * 4];
        reader.getPixels(x, y, width, height, PixelFormat.getByteBgraInstance(), buffer, 0, width * 4);

        writer.setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), buffer, 0, width * 4);
        return subimage;
    }
}