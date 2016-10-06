package com.barbershop.animation.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;

import java.io.InputStream;

/**
 * @author sabrina on 06/10/16.
 */
public class SpriteSheet {
    private final Image sheet;
    private PixelReader reader;

    public SpriteSheet(InputStream inputStream) {
        sheet = new Image(inputStream);
        reader = sheet.getPixelReader();
    }

    public void getSprite(int x, int y, int width, int height, GraphicsContext gc) {
        Image up1 = subimage(x, y, width, height);
        gc.drawImage(up1, 0, 0, width, height);

        Image up2 = subimage(x, y + height, width, height);
        gc.drawImage(up2, 0, 32, width, height);

        Image down = subimage(x, y + 2 * height, width, height);
        gc.drawImage(down, 0, 2 * 32, width, height);

        Image down2 = subimage(x, y + 3 * height, width, height);
        gc.drawImage(down2, 0, 3 * 32, width, height);

        Image left = subimage(x + width, y, width, height);
        gc.drawImage(left, 32, 0, width, height);

        Image left1 = subimage(x + width, y + height, width, height);
        gc.drawImage(left1, 32, 32, width, height);

        Image right = subimage(x + width, y + 2 * height, width, height);
        gc.drawImage(right, 32, 2 * 32, width, height);

        Image right1 = subimage(x + width, y + 3 * height, width, height);
        gc.drawImage(right1, 32, 3 * 32, width, height);
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
