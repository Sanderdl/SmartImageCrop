/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.io.File;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Sander
 */
public class ImageProcessor {
    
    private final ImageCropController controller;
    private final ExecutorService pool;
    
    private CalculateSizeTask sizeTask;
    private ImageCropTask cropTask;
    
    private File[] Images;
    private SizeResult sizes;
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
        unbind();
        
        CyclicBarrier cbCalculate;
        cbCalculate = new CyclicBarrier(1, new Runnable() {
            @Override
            public void run() {
                try {
                    sizes = sizeTask.get();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            cropAndSaveImages();
                        }
                    });
                    
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getMessage());
                }
            }
        });
        
        calculateSmallestPossibleSize(cbCalculate);

    }
    
    private void calculateSmallestPossibleSize(CyclicBarrier cb){
        controller.setProgressText("Calculating smallest possible size...");
       
        sizeTask = new CalculateSizeTask(Images, cb);
        setSizeBind();
        
        pool.submit(sizeTask);
        
    }
    
    private void cropAndSaveImages(){
        cropTask = new ImageCropTask(Images, sizes, controller);
        unbind();
        setCropBind();
        pool.submit(cropTask);
        
    }
    
    private void setSizeBind(){
        controller.getProgressBar().progressProperty().bind(sizeTask.progressProperty());
    }
    
    private void setCropBind(){
        controller.getProgressBar().progressProperty().bind(cropTask.progressProperty());
        controller.getProgressLabel().textProperty().bind(cropTask.messageProperty());
    }
    
    private void unbind(){
        controller.getProgressBar().progressProperty().unbind();
        controller.getProgressLabel().textProperty().unbind();
    }
    
    public void close(){
        pool.shutdown();
    }
}
