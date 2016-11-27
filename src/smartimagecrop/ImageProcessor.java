/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sander
 */
public class ImageProcessor {
    
    private final ImageCropController controller;
    private final ExecutorService pool;
    
    private File[] Images;
    
    public ImageProcessor(ImageCropController controller){
        this.controller = controller;
        this.pool = Executors.newSingleThreadExecutor();
    }
    
    public void loadImages(String path, boolean subFolders){
        
        LoadImageTask loadTask = new LoadImageTask(path, subFolders);
        
        pool.submit(loadTask);
        
        try{
            Images = loadTask.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        controller.setLoadedText("Loaded "+ Images.length + " images to process.");
    }
    
    public void smartCropImages(){
        int[] sizes = calculateSmallestPossibleSize();
        cropAndSaveImages(sizes[0], sizes[1], sizes[2], sizes[3]);
        controller.setProgressText("Cropping completed!");
    }
    
    private int [] calculateSmallestPossibleSize(){
        controller.setProgressText("Calculating smallest possible size...");
        int[] sizes = new int[4];
        
        CalculateSizeTask sizeTask = new CalculateSizeTask(Images);
        pool.submit(sizeTask);
        
        try {
            sizes = sizeTask.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sizes;
    }
    
    private void cropAndSaveImages(int width, int height , int x, int y ){
        controller.setProgressText("Cropping images...");
        ImageCropTask cropTask = new ImageCropTask(Images, width, height, x, y);
        pool.submit(cropTask);
        
        try {
            boolean succes = cropTask.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close(){
        pool.shutdown();
    }
}
