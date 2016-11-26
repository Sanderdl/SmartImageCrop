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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Sander
 */
public class SmartImageCrop extends Application {
    
    @Override
    public void start(Stage stage) {
        
        Parent root = null;
        
        double minSize = 300;
        
        stage.setMinHeight(minSize);
        stage.setMinWidth(minSize);
        
        try {
            root = FXMLLoader.load(getClass().getResource("ImageCrop.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(SmartImageCrop.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
