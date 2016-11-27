/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.concurrent.Task;
import javax.imageio.ImageIO;

/**
 *
 * @author Sander
 */
public class CalculateSizeTask extends Task<int[]> {

    private final File[] images;

    public CalculateSizeTask(File[] images) {
        this.images = images;
    }

    @Override
    protected int[] call() {
        int[] minSizes = new int[2];
        
        int minWidth = 0;
        int minHeight = 0;
        
        for (File f : images) {
           int[] sizes = neededSize(f);
           
           if (sizes[0] > minWidth)
               minWidth = sizes[0];
           
           if (sizes[1] > minHeight)
               minHeight = sizes[1];
        }
        
        minSizes[0] = minWidth;
        minSizes[1] = minHeight;
        
        return minSizes;
    }

    private int[] neededSize(File f) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(f);
        } catch (IOException e) {
        }

        int x1 = image.getWidth();
        int y1 = image.getHeight();

        int width = 0, height = 0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (!isTransparent(x, y, image)) {
                    if (x < x1) {
                        x1 = x;
                    } else if (x > width) {
                        width = x;
                    }

                    if (y < y1) {
                        y1 = y;
                    } else if (y > height) {
                        height = y;
                    }
                }
            }
        }

        width = width - x1;
        height = height - y1;
        
        int[] sizes = new int[2];
        sizes[0] = width;
        sizes[1] = height;

        return sizes;
    }

    public boolean isTransparent(int x, int y ,BufferedImage img) {
        int pixel = img.getRGB(x, y);
        return (pixel >> 24) == 0x00;
    }

}
