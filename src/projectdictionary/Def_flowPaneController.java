/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projectdictionary;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author hahah
 */
public class Def_flowPaneController implements Initializable {

    @FXML
    private FlowPane flowPaneChild;
    
    @FXML
    private TextFlow definition;

    @FXML
    private TextFlow example;

    @FXML
    private TextFlow meaning;
    
    @FXML
    private Button translate;
    
    public void setData(Pair<String, String> defxEx) {
        Text defText = new Text(defxEx.getKey());
        definition.getChildren().add(defText);
        Text exText = new Text(defxEx.getValue());
        example.getChildren().add(exText);
        translate.setOnAction(e -> {
            Text meanText = new Text("Van ban dich se o day!");
            meaning.getChildren().add(meanText);
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
//projectdictionary.Def_flowPaneController
