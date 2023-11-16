/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projectdictionary;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author hahah
 */
public class CardWordController implements Initializable {

    @FXML
    private VBox box;
    
    @FXML
    private FlowPane defs;

    @FXML
    private Label partOfSpeech;
    
    private String [] colors = {"B9E5FF", "BDB2FE", "FB9AA8", "FF5056"};
    
    public void setData(Word word) {
        partOfSpeech.setText(word.getPartOfSpeech());
        ArrayList<Pair<String, String>> getDefxEx = word.getDefxEx();
        try {
            for(Pair<String, String> onePair : getDefxEx) {
                FXMLLoader fXMLLoader = new FXMLLoader();
                fXMLLoader.setLocation(getClass().getResource("Def_flowPane.fxml"));
                FlowPane flowPaneChild = fXMLLoader.load();
                Def_flowPaneController def_flowPaneController = fXMLLoader.getController();
                def_flowPaneController.setData(onePair);
                defs.getChildren().add(flowPaneChild);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        box.setStyle("-fx-background-color: #" + colors[(int)(Math.random()*colors.length)]);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
//projectdictionary.CardWordController