/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javax.imageio.ImageIO;

/**
 *
 * @author Sander
 */
public class CalculateSizeTask extends Task<SizeResult>{

    private final File[] images;
    private final int maxProgress;
    private int progress = 0;
    private final CyclicBarrier cb;
    private final SizeResult result;

    public CalculateSizeTask(File[] images, CyclicBarrier cb) {
        this.images = images;
        this.maxProgress = images.length * 2;
        this.cb = cb;
        this.result = new SizeResult();
    }

    @Override
    protected SizeResult call() {

        int minWidth = 0;
        int minHeight = 0;
        
        for (File f : images) {
           Integer[] sizes = neededSize(f);
           result.addPadding(sizes);
           
           if (sizes[0] > minWidth)
               minWidth = sizes[0];
           
           if (sizes[1] > minHeight)
               minHeight = sizes[1];
           
            progress++;
            updateProgress(progress, maxProgress);         
        }
        
        result.setMinWidth(minWidth);
        result.setMinHeight(minHeight);
              
        return result;
    }

    private Integer[] neededSize(File f) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(f);
        } catch (IOException e) {
            Logger.getLogger(CalculateSizeTask.class.getName()).log(Level.SEVERE, null, e);
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
        
        Integer[] sizes = new Integer[4];
        sizes[0] = width;
        sizes[1] = height;
        sizes[2] = x1;
        sizes[3] = y1;

        return sizes;
    }

    public boolean isTransparent(int x, int y ,BufferedImage img) {
        int pixel = img.getRGB(x, y);
        return (pixel >> 24) == 0x00;
    }
    
    @Override
    protected void succeeded() {
        try {
            cb.await();
        }
        catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(CalculateSizeTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
}
