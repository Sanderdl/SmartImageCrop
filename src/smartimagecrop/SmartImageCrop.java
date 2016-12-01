/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Sander
 */
public class SmartImageCrop extends Application {
    
    private ImageCropController imc;
    
    @Override
    public void start(Stage stage) {
        
        Parent root = null;
        
        final double minSizeX = 300;
        final double minSizeY = 500;
        
        stage.setMinHeight(minSizeY);
        stage.setMinWidth(minSizeX);
        stage.setResizable(false);
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageCrop.fxml"));
            root = (Parent)loader.load();
            imc = (ImageCropController)loader.getController();
            
            imc.setUpController(stage);
        } catch (IOException ex) {
            Logger.getLogger(SmartImageCrop.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        stage.setOnCloseRequest((WindowEvent we) -> {
            imc.close();
        });
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
