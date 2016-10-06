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

    public Sprite getSprite(int x, int y, int width, int height) {
        Image up = subimage(x, y, width, height);
        Image up2 = subimage(x, y + height, width, height);

        Image down = subimage(x, y + 2 * height, width, height);
        Image down2 = subimage(x, y + 3 * height, width, height);

        Image left = subimage(x + width, y, width, height);
        Image left2 = subimage(x + width, y + height, width, height);

        Image right = subimage(x + width, y + 2 * height, width, height);
        Image right2 = subimage(x + width, y + 3 * height, width, height);

        return new SpriteBuilder()
                .addFramesUp(up, up2)
                .addFramesDown(down, down2)
                .addFramesLeft(left, left2)
                .addFramesRight(right, right2)
                .build();
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
