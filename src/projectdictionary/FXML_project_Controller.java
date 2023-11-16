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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javax.json.JsonObject;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    private TableView<word_old> dictionaryView;

    @FXML
    private Label english_showLabel;

    @FXML
    private TableColumn<word_old, String> english_word;

    @FXML
    private Button savedBtn;

    @FXML
    private AnchorPane savedForm;

    @FXML
    private TextField search_text;
    
    private Connection connectDB;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    
    public ObservableList<word_old> wordList() {
        ObservableList<word_old> englishWords = FXCollections.observableArrayList();
        
        // Đường dẫn tới file văn bản chứa các từ tiếng Anh
        String filePath = "C:\\Users\\hahah\\Downloads\\tonghop.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Loại bỏ dấu cách thừa và thêm từ vào ObservableList
                word_old wordAdd = new word_old(line.trim());
                englishWords.add(wordAdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return englishWords;
    }
    
    private ObservableList<word_old> myWordList;
    public void myWordListShow() {
        myWordList = wordList();
        english_word.setCellValueFactory(new PropertyValueFactory<>("english"));
        dictionaryView.setItems(myWordList);
        
        FilteredList<word_old> filteredData = new FilteredList<>(wordList(), b->true);
        search_text.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((word) -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                String englishWord = word.getEnglish().toLowerCase();
                return englishWord.startsWith(searchKeyword);
            });
        });
        
        SortedList<word_old> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dictionaryView.comparatorProperty());
        dictionaryView.setItems(sortedData);
    }
    
    @FXML
    private Label phonetic;
    @FXML
    private Button sound;
    
    private MediaPlayer mediaPlayer;
    
    public void myWordSelect() {

        word_old wordSelect = dictionaryView.getSelectionModel().getSelectedItem();
        int num = dictionaryView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        english_showLabel.setText(wordSelect.getEnglish());
        JsonObject wordChooseJsonObject = translateDef.getData(wordSelect.getEnglish());
        System.out.println(wordChooseJsonObject);
        phonetic.setText(wordChooseJsonObject.getString("phonetic"));
        Media media = new Media(wordChooseJsonObject.getString("audio"));
        mediaPlayer = new MediaPlayer(media);
        sound.setOnAction(e -> {
            System.out.println(wordChooseJsonObject.getString("audio"));
            mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
            mediaPlayer.play();
        });
    }
//    private void playAudio() {
//        mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
//        mediaPlayer.play();
//    }
    
    @FXML
    private VBox cardLayout;
    private List<Word> recentlyAdd;
    
    public void doe() {
        recentlyAdd = new ArrayList<>(recentlyWord());
        try {
            for(int i=0; i<recentlyAdd.size();i++) {
                FXMLLoader fXMLLoader = new FXMLLoader();
                fXMLLoader.setLocation(getClass().getResource("CardWord.fxml"));
                VBox cardBox = fXMLLoader.load();
                CardWordController cardWordController = fXMLLoader.getController();
                cardWordController.setData(recentlyAdd.get(i));
                cardLayout.getChildren().add(cardBox);
            }
        } catch (Exception e) {
        }
    }    
    
    private ArrayList<Word> recentlyWord() {
        ArrayList<Word> wordList = new ArrayList<>();
        ArrayList<Pair<String,String>> defxEx = new ArrayList<>();
        defxEx.add(new Pair<>("Nghĩa thứ nhất/1", "Ví dụ thứ nhất"));
        defxEx.add(new Pair<>("Nghĩa thứ hai/1", "Ví dụ thứ hai"));
        defxEx.add(new Pair<>("Nghĩa thứ ba/1", "Ví dụ thứ ba"));
        Word word = new Word("noun", defxEx);
        wordList.add(word);
        
        defxEx = new ArrayList<>();
        defxEx.add(new Pair<>("Nghĩa thứ nhất/2", "Ví dụ thứ nhất"));
        defxEx.add(new Pair<>("Nghĩa thứ hai/2", "Ví dụ thứ hai"));
        defxEx.add(new Pair<>("Nghĩa thứ ba/2", "Ví dụ thứ ba"));
        word = new Word("verb", defxEx);
        wordList.add(word);
        
        defxEx = new ArrayList<>();
        defxEx.add(new Pair<>("Nghĩa thứ nhất", "Ví dụ thứ nhất"));
        defxEx.add(new Pair<>("Nghĩa thứ hai", "Ví dụ thứ hai"));
        defxEx.add(new Pair<>("Nghĩa thứ ba", "Ví dụ thứ ba"));
        word = new Word("adj", defxEx);
        wordList.add(word);
        
        return wordList;
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
        doe();
        myWordListShow();
    }
}
