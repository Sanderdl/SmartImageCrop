/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;

/**
 *
 * @author Sander
 */
public class LoadImageTask extends Task<File[]> {

    private final File directory;
    private final String extention;
    private final FilenameFilter IMAGE_FILTER;
    private final FilenameFilter IMAGE_FOLDER_FILTER;
    private final boolean subFolders;
    private final List<File> images;

    public LoadImageTask(String path, boolean subfolders) {
        directory = new File(path);
        extention = ".png";
        IMAGE_FOLDER_FILTER = (final File dir, final String name) -> {
            if (name.endsWith(extention) || dir.isDirectory())
                return true;
            return (false);
        };
        
        IMAGE_FILTER = (final File dir, final String name) -> {
            if (name.endsWith(extention))
                return true;
            return (false);
        };
        
        this.subFolders = subfolders;
        images = new ArrayList<>();
    }

    @Override
    protected File[] call() {
        
        if (subFolders){
            loadSubFolders(directory);
            File[] files = new File[images.size()];
            files = images.toArray(files);
            return files;
        }
        return directory.listFiles(IMAGE_FILTER);
    }
    
    private void loadSubFolders(File directory){
        File[] files = directory.listFiles(IMAGE_FOLDER_FILTER);
        
        for (File f : files){
            if(f.isDirectory())
                loadSubFolders(f);
            else
                images.add(f);
        }
    }

}
