/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package projectdictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
/**
 *
 * @author hahah
 */
public class FXML_project_Controller implements Initializable {
    
    @FXML
    private Button addBtn;

    @FXML
    private AnchorPane addForm;

    @FXML
    private Button apiBtn;

    @FXML
    private AnchorPane apiForm;

    @FXML
    private Button dicBtn;

    @FXML
    private AnchorPane dicForm;

    @FXML
    private TableView<word> dictionaryView;

    @FXML
    private Label english_showLabel;

    @FXML
    private TableColumn<word, String> english_word;

    @FXML
    private Button savedBtn;

    @FXML
    private AnchorPane savedForm;

    @FXML
    private TextField search_text;

    @FXML
    private Label vienamese_meaningLabel;
    
    private Connection connectDB;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    
    public ObservableList<word> wordList() {
        ObservableList<word> englishWords = FXCollections.observableArrayList();
        
        // Đường dẫn tới file văn bản chứa các từ tiếng Anh
        String filePath = "C:\\Users\\hahah\\Downloads\\english.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Loại bỏ dấu cách thừa và thêm từ vào ObservableList
                word wordAdd = new word(line.trim());
                englishWords.add(wordAdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return englishWords;
    }
    
    private ObservableList<word> myWordList;
    public void myWordListShow() {
        myWordList = wordList();
        english_word.setCellValueFactory(new PropertyValueFactory<>("english"));
        dictionaryView.setItems(myWordList);
        
        FilteredList<word> filteredData = new FilteredList<>(wordList(), b->true);
        search_text.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((word) -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
//                if(word.getEnglish().toLowerCase().indexOf(searchKeyword)>-1) {
//                    return true;
//                }else {
//                    return false;
//                }
                String englishWord = word.getEnglish().toLowerCase();
                return englishWord.startsWith(searchKeyword);
            });
        });
        
        SortedList<word> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dictionaryView.comparatorProperty());
        dictionaryView.setItems(sortedData);
    }
    
    
    public void myWordSelect() {

        word wordSelect = dictionaryView.getSelectionModel().getSelectedItem();
        int num = dictionaryView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        english_showLabel.setText(wordSelect.getEnglish());
//        vienamese_meaningLabel.setText(translateDef.getData(wordSelect.getEnglish()).toString());
        vienamese_meaningLabel.setText("fidasourioeywifuhsiuydiuryeiuyuifysiydr");
    }
    public void translateAPI() {
        
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == dicBtn) {
            dicForm.setVisible(true);
            addForm.setVisible(false);
            apiForm.setVisible(false);
            savedForm.setVisible(false);
        } else if (event.getSource() == addBtn) {
            dicForm.setVisible(false);
            addForm.setVisible(true);
            apiForm.setVisible(false);
            savedForm.setVisible(false);
            
        } else if (event.getSource() == apiBtn) {
            dicForm.setVisible(false);
            addForm.setVisible(false);
            apiForm.setVisible(true);
            savedForm.setVisible(false);
            
        } else if (event.getSource() == savedBtn) {
            dicForm.setVisible(false);
            addForm.setVisible(false);
            apiForm.setVisible(false);
            savedForm.setVisible(true);
        } 

    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myWordListShow();
    }
}
