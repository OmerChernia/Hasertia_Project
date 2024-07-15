package com.laynezcoder.estfx.util;

import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;

public class ImageConverter {

    public static BufferedImage convertToBufferedImage(WritableImage wi, int width, int height) {
        BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        PixelReader pixelReader = wi.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color fxColor = pixelReader.getColor(x, y);
                java.awt.Color awtColor = new java.awt.Color((float) fxColor.getRed(), (float) fxColor.getGreen(), (float) fxColor.getBlue(), (float) fxColor.getOpacity());
                bufImage.setRGB(x, y, awtColor.getRGB());
            }
        }
        return bufImage;
    }
}
