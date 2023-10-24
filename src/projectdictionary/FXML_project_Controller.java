/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package projectdictionary;

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
    private TableView<searchModel> dictionaryView;

    @FXML
    private Label english_showLabel;

    @FXML
    private TableColumn<searchModel, String> english_word;

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
    
//    ObservableList<searchModel> searchModelList = FXCollections.observableArrayList();
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        String selectData = "SELECT english, vietnamese FROM word";
//        connectDB = database.connectDB();
//        
//        try {
//            Statement statement = connectDB.createStatement();
//            ResultSet queryOutput = statement.executeQuery(selectData);
//            
//            while(queryOutput.next()) {
//                String english_wordd = queryOutput.getString("english");
//                String vietnamese_meann = queryOutput.getString("vietnamese");
//                
//                
//                searchModelList.add(new searchModel(english_wordd, vietnamese_meann));
//            }
//            
//            english_word.setCellValueFactory(new PropertyValueFactory<>("englishh"));
//            vienamese_meaningLabel.setCellValueFactory(new PropertyValueFactory<>("vietnamesee"));
//            
//            dictionaryView.setItems(searchModelList);
//            
//        } catch (Exception e) {
//        }
//    }    
    public ObservableList<searchModel> myPlansDataList() {
        ObservableList<searchModel> listData = FXCollections.observableArrayList();
        String selectData = "SELECT * FROM word";
        connectDB = database.connectDB();

        try {
            prepare = connectDB.prepareStatement(selectData);
            result = prepare.executeQuery();

            searchModel pData;
            while (result.next()) {
                pData = new searchModel(result.getInt("wordID"),result.getString("english"),result.getString("vietnamese"));
                listData.add(pData);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<searchModel> myPlansListData;

    public void myPlansShowData() {
        //chỗ này là hiển thị ra ở chỗ TableView
        myPlansListData = myPlansDataList();
        english_word.setCellValueFactory(new PropertyValueFactory<>("englishh"));
        dictionaryView.setItems(myPlansListData);
        
        
        // đoạn này xử lý phần nó search nè
        FilteredList<searchModel> filteredData = new FilteredList<>(myPlansDataList(), b->true);
        search_text.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((searchModel) -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if(searchModel.getEnglishh().toLowerCase().indexOf(searchKeyword)>-1) {
                    return true;
//                } else if(searchModel.getVietnamesee().toLowerCase().indexOf(searchKeyword) > -1) {
//                    return true;
                }else {
                    return false;
                }
            });
        });
        
        SortedList<searchModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dictionaryView.comparatorProperty());
        dictionaryView.setItems(sortedData);
    }

    
    private int wordId;
    public void myPlansSelectData() {

        searchModel pData = dictionaryView.getSelectionModel().getSelectedItem();
        int num = dictionaryView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        wordId = pData.getWordId();
        String selectData = "SELECT * FROM word WHERE wordID = '"+ wordId +"'";
        connectDB = database.connectDB();
        try {
            prepare = connectDB.prepareStatement(selectData);
            result = prepare.executeQuery();
            while(result.next()) {
                vienamese_meaningLabel.setText(result.getString("vietnamese"));
                english_showLabel.setText(result.getString("english"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        myPlansShowData();
    }
}
