/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author Sander
 */
public class ImageCropController implements Initializable{
    
    @FXML
    private Label laLoaded;
    @FXML
    private Button btnCrop;  
    @FXML
    private Label laProgress;
    @FXML
    private CheckBox cbFolders;
    
    private Stage stage;
    private Application application;
    
    private ImageProcessor imageProcessor;
    
    private final static String NotLoadedText = "No images were loaded";  

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageProcessor = new ImageProcessor(this);
    }
    
    @FXML
    private void btnLoadImagesClick(ActionEvent event){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Directory Picker");
        File imageFolder = directoryChooser.showDialog(stage);
        
        btnCrop.setDisable(imageFolder == null);
        
        if (imageFolder != null){
           imageProcessor.loadImages(imageFolder.getPath(), cbFolders.isSelected());
        }else
            laLoaded.setText(NotLoadedText);
    }
    
    @FXML
    private void btnCropClick(ActionEvent event){
        System.out.println("crop");
    }

    public void setUpController(Stage stage, Application application) {
        this.stage = stage;
        this.application = application;
    }
    
    public void setLoadedText(String text){
        laLoaded.setText(text);
    }
    
    public void setProgressText(String text){
        laProgress.setText(text);
    }
    
    public void close(){
        imageProcessor.close();
    }
            
    
}
