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
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author Sander
 */
public class ImageCropController implements Initializable, IControllerParent{
    
    @FXML
    private Label laLoaded;
    @FXML
    private Button btnCrop;
    
    private Stage stage;
    private Application application;
    
    private final static String NotLoadedText = "Images were not loaded.";  

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    private void btnLoadImagesClick(ActionEvent event){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Directory Picker");
        File imageFolder = directoryChooser.showDialog(stage);
        
        btnCrop.setDisable(imageFolder == null);
        
        if (imageFolder != null){
            
        }else
            laLoaded.setText(NotLoadedText);
    }
    
    @FXML
    private void btnCropClick(ActionEvent event){
        System.out.println("crop");
    }

    @Override
    public void setControllerParent(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void shareApplication(Application application) {
        this.application = application;
    }
            
    
}
