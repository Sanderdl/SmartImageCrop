/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
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
    private Button btnLoadImages;
    @FXML
    private Label laProgress;
    @FXML
    private CheckBox cbFolders;
    @FXML
    private CheckBox cbOverride;
    @FXML
    private ProgressBar pbProgress;
    @FXML
    private TextArea taMessages;
    
    private Stage stage;
    
    private ImageProcessor imageProcessor;
    
    private final static String NOTLOADEDTEXT = "No images were loaded";  

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
            laLoaded.setText(NOTLOADEDTEXT);
    }
    
    @FXML
    private void btnCropClick(ActionEvent event){
        btnLoadImages.setDisable(true);
        imageProcessor.smartCropImages();
    }

    public void setUpController(Stage stage) {
        this.stage = stage;
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
    
    public boolean getOverrideImages(){
        return cbOverride.isSelected();
    }
    
    public ProgressBar getProgressBar(){
        return pbProgress;
    }

    public Label getProgressLabel() {
        return laProgress;
    }
        
    public void sendMessage(String message){
        taMessages.appendText(message + "\n");
    }
    
    public void resetButtons(){
        btnCrop.setDisable(true);
        btnLoadImages.setDisable(false);
    }
    
}
