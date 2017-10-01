/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javax.imageio.ImageIO;

/**
 *
 * @author Sander
 */
public class ImageCropTask extends Task<Void> {

    private final File[] images;
    private final int maxProgress;
    private int progress;
    private final SizeResult sizes;
    private final ImageCropController controller;

    public ImageCropTask(File[] images, SizeResult sizes, ImageCropController controller) {
        this.images = images;
        this.sizes = sizes;
        this.maxProgress = images.length * 2;
        this.progress = images.length;
        this.controller = controller;
    }

    @Override
    protected Void call() {
        updateMessage("Cropping and saving images...");
        for (int i = 0; i < images.length; i++) {
            crop(images[i], i);
            progress++;
            updateProgress(progress, maxProgress);
        }
        updateMessage("Cropping complete");
        return null;
    }

    private void crop(File f, int i) {
        try {
            BufferedImage originalImgage = ImageIO.read(f);

            Integer[] size = sizes.getPadding(i);

            BufferedImage SubImgage = originalImgage.getSubimage(
                    size[2], size[3], size[0], size[1]
            );

            int difX = sizes.getMinWidth() - size[0];
            int difY = sizes.getMinHeight() - size[1];

            BufferedImage resized = resize(SubImgage, difX, difY);

            File outputfile;
            if (!controller.getOverrideImages()) {
                String oldDir = f.getPath();
                String newDir = oldDir.substring(0, oldDir.lastIndexOf(File.separator) + 1);
                String newFile = f.getName().substring(0, f.getName().lastIndexOf("."));

                outputfile = new File(newDir + newFile + "_cropped.png");
            } else {
                outputfile = new File(f.getPath());
            }

            ImageIO.write(resized, "png", outputfile);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.sendMessage("Image cropped successfully: " + outputfile.getPath());
                }
            });

        } catch (IOException e) {
            Logger.getLogger(ImageCropTask.class.getName()).log(Level.SEVERE, null, e);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.sendMessage(f.getPath() + " could not be loaded.");
                }
            });

        } catch (RasterFormatException e) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.sendMessage(f.getPath() + " was not the same size, image was not cropped.");
                }
            });
            Logger.getLogger(ImageCropTask.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private BufferedImage resize(BufferedImage image, int diffX, int diffY) {
        BufferedImage bufferedImage = new BufferedImage(sizes.getMinWidth(),
                sizes.getMinHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.drawImage(image, diffX / 2, diffY, null);

        g2d.dispose();

        return bufferedImage;
    }

    @Override
    protected void succeeded() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.resetButtons();
            }
        });
    }

}
