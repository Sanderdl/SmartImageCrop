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
public class ImageCropTask extends Task<Boolean>{
    
    private final File[] images;
    private final int width;
    private final int height;
    private final int startX;
    private final int startY;
    
    public ImageCropTask(File[] images, int width, int height, int startX, int startY){
        this.images = images;
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }
    
    @Override
    protected Boolean call(){
        for(File f : images){
            crop(f);
        }
        return true;
    }
    
    private void crop(File f) {
        try {
            BufferedImage originalImgage = ImageIO.read(f);
            
            System.out.println(startX);
            System.out.println(startY);

            BufferedImage SubImgage = originalImgage.getSubimage(startX, startY, width, height);
            System.out.println("Cropped Image Dimension: " + SubImgage.getWidth() + "x" + SubImgage.getHeight());
            
            String oldDir = f.getPath();
            String newDir = oldDir.substring(0,oldDir.lastIndexOf(File.separator)+1);
            String newFile = f.getName().substring(0, f.getName().lastIndexOf("."));
            
            File outputfile = new File(newDir+newFile +"_cropped.png");
            ImageIO.write(SubImgage, "png", outputfile);

            System.out.println("Image cropped successfully: " + outputfile.getPath());

        } catch (IOException e) {
            System.out.println("messed up");
        }
    }
    
}
