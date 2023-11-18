package projectdictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.json.JsonArray;
import javax.json.JsonValue;
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
    private Button savedBtn;

    @FXML
    private AnchorPane savedForm;
    
    private Connection connectDB;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    
    
    // Từ điển Form
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
    private VBox cardLayout;
    @FXML
    private TableView<word_old> dictionaryView;
    @FXML
    private Label phonetic;
    @FXML
    private Button sound;
    @FXML
    private Button saveButton;
    @FXML
    private Label english_showLabel;
    @FXML
    private TableColumn<word_old, String> english_word;
    @FXML
    private TextField search_text;
    private MediaPlayer mediaPlayer;
    
    public void myWordSelect() {

        word_old wordSelect = dictionaryView.getSelectionModel().getSelectedItem();
        int num = dictionaryView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        english_showLabel.setText(wordSelect.getEnglish());
        cardLayout.getChildren().clear();
        JsonObject wordChooseJsonObject = translateDef.getData(wordSelect.getEnglish());
        if(wordChooseJsonObject.get("phonetic")!=JsonValue.NULL) {
            phonetic.setText(wordChooseJsonObject.getString("phonetic"));
        }
        if(wordChooseJsonObject.get("audio")!=JsonValue.NULL) {
            Media media = new Media(wordChooseJsonObject.getString("audio"));
            mediaPlayer = new MediaPlayer(media);
            sound.setOnAction(e -> {
                System.out.println(wordChooseJsonObject.getString("audio"));
                mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
                mediaPlayer.play();
            });
        } else {
            Media media = new Media(new File("D:\\Music\\cmsnThien\\cmsnThien.mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            sound.setOnAction(e -> {
                System.out.println("hehe");
                mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
                mediaPlayer.play();
            });
        }
        
        JsonArray meanings = wordChooseJsonObject.getJsonArray("meanings");
        for(JsonValue meaning : meanings) {
            JsonObject mean = (JsonObject) meaning;
            String part = mean.getString("partOfSpeech");
            ArrayList<Pair<String,String>> defxEx = new ArrayList<>();
            JsonArray defArray = mean.getJsonArray("definitionArray");
            for(JsonValue getDefxExam : defArray) {
                JsonObject getDef = (JsonObject) getDefxExam;
                defxEx.add(new Pair<>(getDef.getString("definition"), getDef.getString("example")));
            }
            Word word = new Word(part, defxEx);
            try {
                FXMLLoader fXMLLoader = new FXMLLoader();
                fXMLLoader.setLocation(getClass().getResource("CardWord.fxml"));
                VBox cardBox = fXMLLoader.load();
                CardWordController cardWordController = fXMLLoader.getController();
                cardWordController.setData(word);
                cardLayout.getChildren().add(cardBox);
            } catch (Exception e) {
            }
        }
        
        boolean wordExists = saveWordClass.checkWordExists(wordSelect.getEnglish());
        if(wordExists) {
            saveButton.setVisible(false);
        } else {
            saveButton.setVisible(true);
            saveButton.setOnAction(e -> {
                saveWordClass.saveWord(wordSelect.getEnglish());
                saveButton.setVisible(false);
                myWord_SaveListShow();
            });
        }
    }

    
    // API Form
    @FXML
    private TextArea inputAPI;
    @FXML
    private ComboBox<String> chooseLan_input;
    @FXML
    private ComboBox<String> chooseLan_output;
    @FXML
    private TextArea outputAPI;
    
    String lan_input = null;
    String lan_input_speak = null;
    String lan_output = null;
    String lan_output_speak = null;
    String encodedSrc = null;
    
    @FXML
    private Button speak_input;
    @FXML
    private Button speak_output;
    
    private final PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
    public void chooseLan_input() throws UnsupportedEncodingException {
        Map<String, Pair<String,String>> languageMap_input = new HashMap<>();
        languageMap_input.put("Tự phát hiện ngôn ngữ", new Pair("auto", "en-us"));
        languageMap_input.put("English", new Pair("en", "en-us"));
        languageMap_input.put("Việt Nam", new Pair("vi", "vi-vn"));

        ObservableList<String> listData = FXCollections.observableArrayList(languageMap_input.keySet());
        chooseLan_input.setItems(listData);

        chooseLan_input.setOnAction(e -> {
            String selectedKey = chooseLan_input.getSelectionModel().getSelectedItem();
            if (selectedKey != null) {
                lan_input = languageMap_input.get(selectedKey).getKey();
                System.out.println(lan_input);
                lan_input_speak = languageMap_input.get(selectedKey).getValue();
                System.out.println(lan_input_speak);
                pauseTransition.stop();
                pauseTransition.playFromStart();
            }
        });
        
        Map<String, Pair<String,String>> languageMap_output = new HashMap<>();
        languageMap_output.put("Việt Nam",  new Pair("vi", "vi-vn"));
        languageMap_output.put("English", new Pair("en", "en-us"));
        languageMap_output.put("Đức", new Pair("de", "de-de"));
        languageMap_output.put("Pháp", new Pair("fr", "fr-fr"));
        languageMap_output.put("Hàn Quốc",  new Pair("ko", "ko-kr"));
        languageMap_output.put("Nga", new Pair("ru", "ru-ru"));
        languageMap_output.put("Nhật", new Pair("ja", "ja-jp"));
        languageMap_output.put("Thái", new Pair("th", "th-th"));
        languageMap_output.put("Trung Quốc", new Pair("zh", "zh-cn"));

        ObservableList<String> listData1 = FXCollections.observableArrayList(languageMap_output.keySet());
        chooseLan_output.setItems(listData1);

        chooseLan_output.setOnAction(e -> {
            String selectedKey = chooseLan_output.getSelectionModel().getSelectedItem();
            if (selectedKey != null) {
                lan_output = languageMap_output.get(selectedKey).getKey();
                lan_output_speak = languageMap_output.get(selectedKey).getValue();
                pauseTransition.stop();
                pauseTransition.playFromStart();
            }
        });
        inputAPI.textProperty().addListener((observable, oldValue, newValue) -> {
                pauseTransition.stop();
                pauseTransition.playFromStart();
            });
        pauseTransition.setOnFinished(event -> {
            updateOutputAPI();
            System.out.println("OK!");
        });
        
        speak_input.setOnAction(e1 -> {
            try {
                encodedSrc = URLEncoder.encode(inputAPI.getText(), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(FXML_project_Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            String url = "http://api.voicerss.org/?key=61a7d5f8f67646279b4c24dd81bb6576&hl=" + lan_input_speak + "&src=" + encodedSrc;
            System.out.println(url);
            System.out.println(lan_input_speak);
            System.out.println(inputAPI.getText() + ", " + encodedSrc);
            Media media = new Media(url);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
            mediaPlayer.play();
        });
        
        speak_output.setOnAction(e1 -> {
            try {
                encodedSrc = URLEncoder.encode(outputAPI.getText(), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(FXML_project_Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            String url = "http://api.voicerss.org/?key=61a7d5f8f67646279b4c24dd81bb6576&hl=" + lan_output_speak + "&src=" + encodedSrc;
            Media media = new Media(url);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
            mediaPlayer.play();
        });
    }
    private void updateOutputAPI() {
        String s = translateDef.getMeanVietNam(inputAPI.getText(), lan_input, lan_output);
        outputAPI.setText(s);
    }

    
    // Save Form
    public ObservableList<word_old> wordList_saveList() {
        ObservableList<word_old> englishWords = FXCollections.observableArrayList();
        
        // Đường dẫn tới file văn bản chứa các từ tiếng Anh
        String filePath = "C:\\Users\\hahah\\Downloads\\savedWord.txt";

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
    
    private ObservableList<word_old> myWordList_save;
    public void myWord_SaveListShow() {
        myWordList_save = wordList_saveList();
        english_word1.setCellValueFactory(new PropertyValueFactory<>("english"));
        dictionaryView1.setItems(myWordList_save);
        
        FilteredList<word_old> filteredData = new FilteredList<>(wordList_saveList(), b->true);
        search_text1.textProperty().addListener((observable, oldValue, newValue) -> {
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
        sortedData.comparatorProperty().bind(dictionaryView1.comparatorProperty());
        dictionaryView1.setItems(sortedData);
    }
    
    @FXML
    private VBox cardLayout1;
    @FXML
    private TableView<word_old> dictionaryView1;
    @FXML
    private Label english_showLabel1;
    @FXML
    private TableColumn<word_old, String> english_word1;
    @FXML
    private Button saveButton1;
    @FXML
    private TextField search_text1;
    @FXML
    private Button sound1;
    @FXML
    private Label phonetic1;
    
    public void myWord_SaveSelect() {

        word_old wordSelect = dictionaryView1.getSelectionModel().getSelectedItem();
        int num = dictionaryView1.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        
        saveButton1.setVisible(true);
        english_showLabel1.setText(wordSelect.getEnglish());
        cardLayout1.getChildren().clear();
        JsonObject wordChooseJsonObject = translateDef.getData(wordSelect.getEnglish());
        if(wordChooseJsonObject.get("phonetic")!=JsonValue.NULL) {
            phonetic1.setText(wordChooseJsonObject.getString("phonetic"));
        }
        if(wordChooseJsonObject.get("audio")!=JsonValue.NULL) {
            Media media = new Media(wordChooseJsonObject.getString("audio"));
            mediaPlayer = new MediaPlayer(media);
            sound1.setOnAction(e -> {
                System.out.println(wordChooseJsonObject.getString("audio"));
                mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
                mediaPlayer.play();
            });
        } else {
            Media media = new Media(new File("D:\\Music\\cmsnThien\\cmsnThien.mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            sound1.setOnAction(e -> {
                System.out.println("hehe");
                mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
                mediaPlayer.play();
            });
        }
        
        JsonArray meanings = wordChooseJsonObject.getJsonArray("meanings");
        for(JsonValue meaning : meanings) {
            JsonObject mean = (JsonObject) meaning;
            String part = mean.getString("partOfSpeech");
            ArrayList<Pair<String,String>> defxEx = new ArrayList<>();
            JsonArray defArray = mean.getJsonArray("definitionArray");
            for(JsonValue getDefxExam : defArray) {
                JsonObject getDef = (JsonObject) getDefxExam;
                defxEx.add(new Pair<>(getDef.getString("definition"), getDef.getString("example")));
            }
            Word word = new Word(part, defxEx);
            try {
                FXMLLoader fXMLLoader = new FXMLLoader();
                fXMLLoader.setLocation(getClass().getResource("CardWord.fxml"));
                VBox cardBox = fXMLLoader.load();
                CardWordController cardWordController = fXMLLoader.getController();
                cardWordController.setData(word);
                cardLayout1.getChildren().add(cardBox);
            } catch (Exception e) {
            }
        }
        
        saveButton1.setOnAction(e -> {
            saveWordClass.removeWordFromFile(wordSelect.getEnglish());
            myWord_SaveListShow();
            cardLayout1.getChildren().clear();
            search_text1.setText("");
            english_showLabel1.setText("EnglishWord");
            phonetic1.setText("Phonetic");
            saveButton1.setVisible(false);
            });
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
        try {
            chooseLan_input();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FXML_project_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        myWord_SaveListShow();
    }
}
