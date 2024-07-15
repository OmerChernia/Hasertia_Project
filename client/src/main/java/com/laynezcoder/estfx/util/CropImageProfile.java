package com.laynezcoder.estfx.util;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class CropImageProfile {

    private final String EXTENSION = "jpg";

    private ImageView imageView;

    private double imageHeight;

    private double imageWidth;

    private final File file;

    private InputStream outputStream;

    private InputStream inputStream;

    private Rectangle rect;

    private Group group;

    public CropImageProfile(File file) {
        this.file = file;
        createWorkSpace();
    }

    private void createWorkSpace() {
        group = new Group();

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        Image image = new Image(inputStream);
        imageHeight = image.getHeight();
        imageWidth = image.getWidth();

        imageView = new ImageView(image);
        imageView.setFitHeight(imageHeight);
        imageView.setFitWidth(imageWidth);
        imageView.setPreserveRatio(true);

        rect = new Rectangle();
        group.getChildren().addAll(imageView, rect);

        if (imageWidth > imageHeight) {
            rect.setWidth(imageHeight);
            rect.setHeight(imageHeight);
            rect.setX(imageWidth / 2 - imageHeight / 2);
            rect.setY(0);
        } else if (imageHeight > imageWidth) {
            rect.setWidth(imageWidth);
            rect.setHeight(imageWidth);
            rect.setX(0);
            rect.setY(imageHeight / 2 - imageWidth / 2);
        } else if (imageHeight == imageWidth) {
            group.getChildren().remove(rect);
        }
    }

    public InputStream getInputStream() throws FileNotFoundException {
        if (!group.getChildren().contains(rect)) {
            return new FileInputStream(file);
        } else {
            Bounds bounds = rect.getBoundsInParent();
            int width = (int) bounds.getWidth();
            int height = (int) bounds.getHeight();

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

            WritableImage wi = new WritableImage(width, height);
            imageView.snapshot(parameters, wi);

            BufferedImage bufImage = ImageConverter.convertToBufferedImage(wi, width, height);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(bufImage, EXTENSION, os);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            outputStream = new ByteArrayInputStream(os.toByteArray());
        }
        return outputStream;
    }
}
