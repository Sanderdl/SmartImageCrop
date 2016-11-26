/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 *
 * @author Sander
 */
public class ImageCropController implements Initializable{
    
    @FXML
    private Button btnLoadImages;
    
    @FXML
    private Button btnCrop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    private void btnLoadImagesClick(ActionEvent event){
        System.out.println("load");
        btnCrop.setDisable(false);
    }
    
    @FXML
    private void btnCropClick(ActionEvent event){
        System.out.println("crop");
    }
            
    
}
