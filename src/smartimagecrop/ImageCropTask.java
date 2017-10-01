/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import javafx.concurrent.Task;
import javax.imageio.ImageIO;

/**
 *
 * @author Sander
 */
public class ImageCropTask extends Task<Void> {

    private final File[] images;
    private final boolean override;
    private final int maxProgress;
    private int progress;
    private final SizeResult sizes;

    public ImageCropTask(File[] images, SizeResult sizes, boolean override) {
        this.images = images;
        this.sizes = sizes;
        this.override = override;
        this.maxProgress = images.length * 2;
        this.progress = images.length;
    }

    @Override
    protected Void call() {
        for (int i = 0; i < images.length; i++) {
            crop(images[i], i);
            progress++;
            updateProgress(progress, maxProgress);
        }
        return null;
    }

    private void crop(File f, int i) {
        try {
            BufferedImage originalImgage = ImageIO.read(f);

            Integer[] size = sizes.getPadding(i);

            int startX = originalImgage.getWidth() - size[2];
            int startY = originalImgage.getHeight() - size[3];

            BufferedImage SubImgage = originalImgage.getSubimage(
                    size[2], size[3], size[0], size[1]
            );
            
            int difX = sizes.getMinWidth() - size[0];
            int difY = sizes.getMinHeight() - size[1];
            
            BufferedImage resized = resize(SubImgage, difX, difY);

            File outputfile = null;
            if (!override) {
                String oldDir = f.getPath();
                String newDir = oldDir.substring(0, oldDir.lastIndexOf(File.separator) + 1);
                String newFile = f.getName().substring(0, f.getName().lastIndexOf("."));

                outputfile = new File(newDir + newFile + "_cropped.png");
            } else {
                outputfile = new File(f.getPath());
            }

            ImageIO.write(resized, "png", outputfile);

            System.out.println("Image cropped successfully: " + outputfile.getPath());

        } catch (IOException e) {
            System.out.println(f.getPath() + " could not be loaded.");
        } catch (RasterFormatException e) {
            System.out.println(f.getPath() + " was not the same size, image was not cropped.");
        }

    }

    private BufferedImage resize(BufferedImage image, int diffX, int diffY) {
        BufferedImage bufferedImage = new BufferedImage(sizes.getMinWidth(), 
                sizes.getMinHeight(), BufferedImage.TYPE_INT_ARGB);

        // Create a graphics contents on the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();

        // Draw graphics
        //g2d.setComposite(AlphaComposite.Clear);
        g2d.drawImage(image, diffX/2, diffY, null);
        

        // Graphics context no longer needed so dispose it
        g2d.dispose();

        return bufferedImage;
    }

}
