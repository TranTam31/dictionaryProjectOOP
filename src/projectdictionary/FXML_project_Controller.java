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
import java.util.Optional;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
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

        FilteredList<word_old> filteredData = new FilteredList<>(wordList(), b -> true);
        search_text.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((word) -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
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
        search_text.setText("");
        sound.setVisible(false);
        english_showLabel.setText(wordSelect.getEnglish());
        cardLayout.getChildren().clear();
        JsonObject wordChooseJsonObject = translateDef.getData(wordSelect.getEnglish());
        if (wordChooseJsonObject.get("phonetic") != JsonValue.NULL) {
            phonetic.setText(wordChooseJsonObject.getString("phonetic"));
        }
        if (wordChooseJsonObject.get("audio") != JsonValue.NULL) {
            sound.setVisible(true);
            Media media = new Media(wordChooseJsonObject.getString("audio"));
            mediaPlayer = new MediaPlayer(media);
            sound.setOnAction(e -> {
                mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
                mediaPlayer.play();
            });
        }

        JsonArray meanings = wordChooseJsonObject.getJsonArray("meanings");
        for (JsonValue meaning : meanings) {
            JsonObject mean = (JsonObject) meaning;
            String part = mean.getString("partOfSpeech");
            ArrayList<Pair<String, String>> defxEx = new ArrayList<>();
            JsonArray defArray = mean.getJsonArray("definitionArray");
            for (JsonValue getDefxExam : defArray) {
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
        if (wordExists) {
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

    //Add Form
    @FXML
    private TextField english_add;
    @FXML
    private TextArea vietnamese_add;
    @FXML
    private TableView<addWord> add_TableView;
    @FXML
    private TableColumn<addWord, String> addEnglish_col;
    @FXML
    private TableColumn<addWord, String> addVietnamese_col;
    @FXML
    private Button addWord_btn;
    @FXML
    private Button update_btn;
    @FXML
    private Button delete_btn;
    @FXML
    private Button clear_btn;
    @FXML
    private TextField search_word_add;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    private Alert alert;

    public void myPlansAddBtn() {
        connect = database.connectDB();
        try {
            if (english_add.getText().isEmpty() || vietnamese_add.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkPlan = "SELECT english FROM word WHERE english = '"
                        + english_add.getText() + "'";

                prepare = connect.prepareStatement(checkPlan);
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Từ " + english_add.getText() + " is already recorded");
                    alert.showAndWait();
                } else {
                    String insertData = "INSERT INTO word (english, vietnamese) "
                            + "VALUES(?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, english_add.getText());
                    prepare.setString(2, vietnamese_add.getText());

                    prepare.executeUpdate();

                    myPlansShowData();
                    myPlansClearBtn();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void myPlansUpdateBtn() {
        connect = database.connectDB();
        try {
            if (wordID_var == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the item first");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Plan: "
                        + english_add.getText());
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    String checkData = "SELECT * FROM word WHERE wordID = " + wordID_var;

                    statement = connect.createStatement();
                    result = statement.executeQuery(checkData);
                    if (result.next()) {

                        String updateData = "UPDATE word SET english = '"
                                + english_add.getText() + "', vietnamese = '"
                                + vietnamese_add.getText()
                                + "' WHERE wordID = " + wordID_var;

                        prepare = connect.prepareStatement(updateData);
                        prepare.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated!");
                        alert.showAndWait();

                        myPlansShowData();
                        myPlansClearBtn();
                    }
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void myPlansDeleteBtn() {
        connect = database.connectDB();
        try {
            if (wordID_var == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the item first");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Plan: "
                        + english_add.getText());
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    String checkData = "SELECT * FROM word WHERE wordID = " + wordID_var;

                    statement = connect.createStatement();
                    result = statement.executeQuery(checkData);
                    if (result.next()) {
                        String deleteData = "DELETE FROM word WHERE wordID = " + wordID_var;

                        prepare = connect.prepareStatement(deleteData);
                        prepare.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Deleted!");
                        alert.showAndWait();

                        myPlansShowData();
                        myPlansClearBtn();
                    }
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void myPlansClearBtn() {
        english_add.setText("");
        vietnamese_add.setText("");
        wordID_var = 0;
    }

    public ObservableList<addWord> myPlansDataList() {

        ObservableList<addWord> listData = FXCollections.observableArrayList();

        String selectData = "SELECT * FROM word";

        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            addWord pData;
            while (result.next()) {
                pData = new addWord(result.getInt("wordID"),
                        result.getString("english"), result.getString("vietnamese"));

                listData.add(pData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<addWord> myPlansListData;

    public void myPlansShowData() {
        myPlansListData = myPlansDataList();
        addEnglish_col.setCellValueFactory(new PropertyValueFactory<>("english"));
        addVietnamese_col.setCellValueFactory(new PropertyValueFactory<>("vietnamese"));
        add_TableView.setItems(myPlansListData);
        
        FilteredList<addWord> filteredData = new FilteredList<>(myPlansDataList(), b -> true);
        search_word_add.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((word) -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                String englishWord = word.getEnglish().toLowerCase();
                return englishWord.startsWith(searchKeyword);
            });
        });

        SortedList<addWord> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(add_TableView.comparatorProperty());
        add_TableView.setItems(sortedData);
    }

    private int wordID_var;

    public void myPlansSelectData() {

        addWord pData = add_TableView.getSelectionModel().getSelectedItem();
        int num = add_TableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        wordID_var = pData.getWordAddID();

        english_add.setText(pData.getEnglish());
        vietnamese_add.setText(pData.getVietnamese());

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
    @FXML
    private ImageView check_image;
    private final PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.8));
    private final FadeTransition fadeInOut = new FadeTransition(Duration.seconds(2), check_image);
    
    public void chooseLan_input() throws UnsupportedEncodingException {
        fadeInOut.setFromValue(1.0);
        fadeInOut.setToValue(0.0);
        //check_image.setOpacity(0);
        Map<String, Pair<String, String>> languageMap_input = new HashMap<>();
        languageMap_input.put("Tự phát hiện ngôn ngữ", new Pair("auto", "en-us"));
        languageMap_input.put("English", new Pair("en", "en-us"));
        languageMap_input.put("Việt Nam", new Pair("vi", "vi-vn"));

        ObservableList<String> listData = FXCollections.observableArrayList(languageMap_input.keySet());
        chooseLan_input.setItems(listData);

        chooseLan_input.setOnAction(e -> {
            speak_input.setVisible(false);
            String selectedKey = chooseLan_input.getSelectionModel().getSelectedItem();
            inputAPI.setText("");
            if (selectedKey != null) {
                lan_input = languageMap_input.get(selectedKey).getKey();
                if (!"auto".equals(lan_input)) {
                    speak_input.setVisible(true);
                }
                lan_input_speak = languageMap_input.get(selectedKey).getValue();
            }
        });
        
        

        Map<String, Pair<String, String>> languageMap_output = new HashMap<>();
        languageMap_output.put("Việt Nam", new Pair("vi", "vi-vn"));
        languageMap_output.put("English", new Pair("en", "en-us"));
        languageMap_output.put("Đức", new Pair("de", "de-de"));
        languageMap_output.put("Pháp", new Pair("fr", "fr-fr"));
        languageMap_output.put("Hàn Quốc", new Pair("ko", "ko-kr"));
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
                if(!"".equals(inputAPI.getText())) {
                    pauseTransition.stop();
                    pauseTransition.playFromStart();
                }
            }
        });
        
        
        inputAPI.textProperty().addListener((observable, oldValue, newValue) -> {
            if(lan_input != null && lan_output != null) {
                pauseTransition.stop();
                pauseTransition.playFromStart();
            }
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
        
        pauseTransition.setOnFinished(event -> {
            updateOutputAPI();
            System.out.println("OK!");
//            fadeInOut.setFromValue(1.0);
//            fadeInOut.setToValue(0.0);
            fadeInOut.play();
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

        FilteredList<word_old> filteredData = new FilteredList<>(wordList_saveList(), b -> true);
        search_text1.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((word) -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
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
        search_text1.setText("");
        sound1.setVisible(false);
        saveButton1.setVisible(true);
        english_showLabel1.setText(wordSelect.getEnglish());
        cardLayout1.getChildren().clear();
        JsonObject wordChooseJsonObject = translateDef.getData(wordSelect.getEnglish());
        if (wordChooseJsonObject.get("phonetic") != JsonValue.NULL) {
            phonetic1.setText(wordChooseJsonObject.getString("phonetic"));
        }
        if (wordChooseJsonObject.get("audio") != JsonValue.NULL) {
            sound1.setVisible(true);
            Media media = new Media(wordChooseJsonObject.getString("audio"));
            mediaPlayer = new MediaPlayer(media);
            sound1.setOnAction(e -> {
                System.out.println(wordChooseJsonObject.getString("audio"));
                mediaPlayer.seek(mediaPlayer.getStartTime()); // Đặt lại vị trí của âm thanh về đầu
                mediaPlayer.play();
            });
        }

        JsonArray meanings = wordChooseJsonObject.getJsonArray("meanings");
        for (JsonValue meaning : meanings) {
            JsonObject mean = (JsonObject) meaning;
            String part = mean.getString("partOfSpeech");
            ArrayList<Pair<String, String>> defxEx = new ArrayList<>();
            JsonArray defArray = mean.getJsonArray("definitionArray");
            for (JsonValue getDefxExam : defArray) {
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

    public void switchForm(ActionEvent event) throws UnsupportedEncodingException {

        if (event.getSource() == dicBtn) {
            dicForm.setVisible(true);
            addForm.setVisible(false);
            apiForm.setVisible(false);
            savedForm.setVisible(false);
            cardLayout.getChildren().clear();
            search_text.setText("");
            english_showLabel.setText("EnglishWord");
            phonetic.setText("Phonetic");
            sound.setVisible(false);
            saveButton.setVisible(false);
        } else if (event.getSource() == addBtn) {
            dicForm.setVisible(false);
            addForm.setVisible(true);
            apiForm.setVisible(false);
            savedForm.setVisible(false);
            myPlansShowData();
        } else if (event.getSource() == apiBtn) {
            dicForm.setVisible(false);
            addForm.setVisible(false);
            apiForm.setVisible(true);
            savedForm.setVisible(false);
            chooseLan_input();
        } else if (event.getSource() == savedBtn) {
            dicForm.setVisible(false);
            addForm.setVisible(false);
            apiForm.setVisible(false);
            savedForm.setVisible(true);
            cardLayout1.getChildren().clear();
            search_text1.setText("");
            english_showLabel1.setText("EnglishWord");
            phonetic1.setText("Phonetic");
            sound1.setVisible(false);
            saveButton1.setVisible(false);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myWordListShow();
        myWord_SaveListShow();
    }
}
