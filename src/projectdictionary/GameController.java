//package projectdictionary;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.Pane;
//import javafx.scene.text.Text;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class GameController {
//
//    @FXML
//    private Button Exit1;
//    @FXML
//    private ImageView Game_Over;
//    @FXML
//    private Button Lose_new_game;
//    @FXML
//    private Button Win_new_game;
//    @FXML
//    private ImageView Winner;
//    @FXML
//    private ImageView head;
//    @FXML
//    private ImageView left_hand;
//    @FXML
//    private ImageView left_leg;
//    @FXML
//    private ImageView pants;
//    @FXML
//    private ImageView right_hand;
//    @FXML
//    private ImageView right_leg;
//    @FXML
//    private ImageView rope;
//    @FXML
//    private ImageView shirt;
//    @FXML
//    private ImageView pillar;
//    @FXML
//    private Text text;
//    @FXML
//    private Pane buttons;
//    @FXML
//    private Text hint;
//
//    private int mistakes;
//    private int correct;
//    private Word_game word = new Word_game();
//    private String myWord;
//    private List<String> myLetters;
//    private List<String> answer;
//
//    public GameController() throws FileNotFoundException {
//    }
//
//    public void initialize() {
//        Exit1.setVisible(false);
//        rope.setVisible(false);
//        head.setVisible(false);
//        shirt.setVisible(false);
//        pants.setVisible(false);
//        left_hand.setVisible(false);
//        right_hand.setVisible(false);
//        left_leg.setVisible(false);
//        right_leg.setVisible(false);
//        Winner.setVisible(false);
//        Win_new_game.setVisible(false);
//        Game_Over.setVisible(false);
//        Lose_new_game.setVisible(false);
//        pillar.setVisible(true);
//        mistakes = 0;
//        correct = 0;
//        myWord = word.getRandomWord();
//        if(!"end".equals(myWord)) {
//            myLetters = Arrays.asList(myWord.split(""));
//            answer = new ArrayList<>(myLetters.size() * 2);
//
//            for (int i = 0; i < myLetters.size() * 2; i++) {
//                if (i % 2 == 0) {
//                    answer.add("_");
//                } else {
//                    answer.add(" ");
//                }
//            }
//
//            String res = String.join("", answer);
//            text.setText(res);
//            hint.setText(translateDef.getOneDef(myWord));
//            buttons.setDisable(false);
//        } else {
//            
//        }
//    }
//
//
//    public void onClick(ActionEvent event) {
//        String letter = ((Button) event.getSource()).getText();
//        ((Button) event.getSource()).setDisable(true);
//
//        if (myLetters.contains(letter)) {
//            correct++;
//
//            for (int i = 0; i < myLetters.size(); i++) {
//                if (myLetters.get(i).equals(letter)) {
//                    answer.set(i * 2, letter);
//                }
//            }
//
//            String res = String.join("", answer);
//            text.setText(res);
//
//            if (answer.stream().noneMatch(s -> s.equals("_"))) {
//                Winner.setVisible(true);
//                Win_new_game.setVisible(true);
//                buttons.setDisable(true);
//                rope.setVisible(false);
//                head.setVisible(false);
//                shirt.setVisible(false);
//                pants.setVisible(false);
//                left_hand.setVisible(false);
//                right_hand.setVisible(false);
//                left_leg.setVisible(false);
//                right_leg.setVisible(false);
//                pillar.setVisible(false);
//                Exit1.setVisible(true);
//            }
//        }else{
//            mistakes++;
//            if(mistakes == 1) {
//                rope.setVisible(true);
//            }
//            else if (mistakes == 2) head.setVisible(true);
//            else if (mistakes == 3) shirt.setVisible(true);
//            else if (mistakes == 4) pants.setVisible(true);
//            else if (mistakes == 5) left_hand.setVisible(true);
//            else if (mistakes == 6) right_hand.setVisible(true);
//            else if (mistakes == 7) left_leg.setVisible(true);
//            else if (mistakes == 8)
//            {
//                right_leg.setVisible(true);
//                rope.setVisible(false);
//                head.setVisible(false);
//                shirt.setVisible(false);
//                pants.setVisible(false);
//                left_hand.setVisible(false);
//                right_hand.setVisible(false);
//                left_leg.setVisible(false);
//                right_leg.setVisible(false);
//                pillar.setVisible(false);
//                Game_Over.setVisible(true);
//                Lose_new_game.setVisible(true);
//                Exit1.setVisible(true);
//                text.setText(myWord);
//                buttons.setDisable(true);
//            }
//        }
//    }
//
//    public void newWord(){
//        for(int i=0; i<27; i++){
//            buttons.getChildren().get(i).setDisable(false);
//        }
//        initialize();
//    }
//
//    public void Exit() {
//        System.exit(0);
//    }
//}


package projectdictionary;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameController {

    @FXML
    private Button Exit1;
    @FXML
    private ImageView Game_Over;
    @FXML
    private Button Lose_new_game;
    @FXML
    private Button Win_new_game;
    @FXML
    private ImageView Winner;
    @FXML
    private ImageView head;
    @FXML
    private ImageView left_hand;
    @FXML
    private ImageView left_leg;
    @FXML
    private ImageView pants;
    @FXML
    private ImageView right_hand;
    @FXML
    private ImageView right_leg;
    @FXML
    private ImageView rope;
    @FXML
    private ImageView shirt;
    @FXML
    private ImageView pillar;
    @FXML
    private Text text;
    @FXML
    private Pane buttons;
    @FXML
    private Text hint;
    @FXML
    private Label mark;
    
    private int mistakes;
    private int correct;
    //private Word_game word = new Word_game();
    private String myWord;
    private List<String> myLetters;
    private List<String> answer;
    private int totalWord;
    public GameController() throws FileNotFoundException {
    }

    private ArrayList<String> words = new ArrayList<>();
    
    public void initword() {
        try (Scanner scanner = new Scanner(new File("C:\\Users\\hahah\\Downloads\\savedWord.txt"))) {
        while (scanner.hasNextLine()) {
            words.add(scanner.nextLine());
        }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public String getRandomWord() {
        Random random = new Random();
        int index = random.nextInt(words.size());
        String selectedWord = words.get(index);
        words.remove(selectedWord);
        return selectedWord.toUpperCase();
    }

    
    public void initialize() {
        initword();
        totalWord = words.size();
        mark.setText("Score: " + (words.size()-totalWord) + " / " + totalWord);
        Exit1.setVisible(false);
        rope.setVisible(false);
        head.setVisible(false);
        shirt.setVisible(false);
        pants.setVisible(false);
        left_hand.setVisible(false);
        right_hand.setVisible(false);
        left_leg.setVisible(false);
        right_leg.setVisible(false);
        Winner.setVisible(false);
        Win_new_game.setVisible(false);
        Game_Over.setVisible(false);
        Lose_new_game.setVisible(false);
        pillar.setVisible(true);
        mistakes = 0;
        correct = 0;
        if(!words.isEmpty()) {
            myWord = getRandomWord();
            myLetters = Arrays.asList(myWord.split(""));
            answer = new ArrayList<>(myLetters.size() * 2);

            for (int i = 0; i < myLetters.size() * 2; i++) {
                if (i % 2 == 0) {
                    answer.add("_");
                } else {
                    answer.add(" ");
                }
            }

            String res = String.join("", answer);
            text.setText(res);
            hint.setText(translateDef.getOneDef(myWord));
            buttons.setDisable(false);
        }
    }

    
    @FXML
    private Label chucmung;
    @FXML
    private Label bandaonhet;
    @FXML
    private Label diemso;
    @FXML
    private Label bancomuon;
    @FXML
    private Button newGameInit;
    @FXML
    private Button exitGame;
    private int correctAns = 0;
    
    public void onClick(ActionEvent event) {
        String letter = ((Button) event.getSource()).getText();
        ((Button) event.getSource()).setDisable(true);

        if (myLetters.contains(letter)) {
            correct++;

            for (int i = 0; i < myLetters.size(); i++) {
                if (myLetters.get(i).equals(letter)) {
                    answer.set(i * 2, letter);
                }
            }

            String res = String.join("", answer);
            text.setText(res);

            if (answer.stream().noneMatch(s -> s.equals("_"))) {
                correctAns++;
                mark.setText("Score: " + (correctAns) + " / " + totalWord);
                if(words.isEmpty()) {
                    Exit1.setVisible(false);
                    rope.setVisible(false);
                    head.setVisible(false);
                    shirt.setVisible(false);
                    pants.setVisible(false);
                    left_hand.setVisible(false);
                    right_hand.setVisible(false);
                    left_leg.setVisible(false);
                    right_leg.setVisible(false);
                    pillar.setVisible(false);
                    buttons.setVisible(false);
                    mark.setVisible(false);
                    text.setVisible(false);
                    hint.setVisible(false);
                    Winner.setVisible(false);
                    Win_new_game.setVisible(false);
                    Game_Over.setVisible(false);
                    Lose_new_game.setVisible(false);
                    
                    
                    chucmung.setVisible(true);
                    bandaonhet.setVisible(true);
                    diemso.setText("Điểm của bạn là " + correctAns + " / " + totalWord);
                    bancomuon.setVisible(true);
                    newGameInit.setVisible(true);
                    exitGame.setVisible(true);
                } else {
                    Winner.setVisible(true);
                    Win_new_game.setVisible(true);
                    buttons.setDisable(true);
                    rope.setVisible(false);
                    head.setVisible(false);
                    shirt.setVisible(false);
                    pants.setVisible(false);
                    left_hand.setVisible(false);
                    right_hand.setVisible(false);
                    left_leg.setVisible(false);
                    right_leg.setVisible(false);
                    pillar.setVisible(false);
                    Exit1.setVisible(true);
                }
            }
        }else{
            mistakes++;
            if(mistakes == 1) {
                rope.setVisible(true);
            }
            else if (mistakes == 2) head.setVisible(true);
            else if (mistakes == 3) shirt.setVisible(true);
            else if (mistakes == 4) pants.setVisible(true);
            else if (mistakes == 5) left_hand.setVisible(true);
            else if (mistakes == 6) right_hand.setVisible(true);
            else if (mistakes == 7) left_leg.setVisible(true);
            else if (mistakes == 8)
            {
                if(words.isEmpty()) {
                    Exit1.setVisible(false);
                    rope.setVisible(false);
                    head.setVisible(false);
                    shirt.setVisible(false);
                    pants.setVisible(false);
                    left_hand.setVisible(false);
                    right_hand.setVisible(false);
                    left_leg.setVisible(false);
                    right_leg.setVisible(false);
                    pillar.setVisible(false);
                    buttons.setVisible(false);
                    mark.setVisible(false);
                    text.setVisible(false);
                    hint.setVisible(false);
                    Winner.setVisible(false);
                    Win_new_game.setVisible(false);
                    Game_Over.setVisible(false);
                    Lose_new_game.setVisible(false);
                    
                    
                    chucmung.setVisible(true);
                    bandaonhet.setVisible(true);
                    diemso.setText("Điểm của bạn là " + (totalWord-words.size()) + " / " + totalWord);
                    bancomuon.setVisible(true);
                    newGameInit.setVisible(true);
                    exitGame.setVisible(true);
                } else {
                    right_leg.setVisible(true);
                    rope.setVisible(false);
                    head.setVisible(false);
                    shirt.setVisible(false);
                    pants.setVisible(false);
                    left_hand.setVisible(false);
                    right_hand.setVisible(false);
                    left_leg.setVisible(false);
                    right_leg.setVisible(false);
                    pillar.setVisible(false);
                    Game_Over.setVisible(true);
                    Lose_new_game.setVisible(true);
                    Exit1.setVisible(true);
                    text.setText(myWord);
                    buttons.setDisable(true);
                }
            }
        }
    }

    public void newWord(){
        for(int i=0; i<27; i++){
            buttons.getChildren().get(i).setDisable(false);
        }
        
        Exit1.setVisible(false);
        rope.setVisible(false);
        head.setVisible(false);
        shirt.setVisible(false);
        pants.setVisible(false);
        left_hand.setVisible(false);
        right_hand.setVisible(false);
        left_leg.setVisible(false);
        right_leg.setVisible(false);
        Winner.setVisible(false);
        Win_new_game.setVisible(false);
        Game_Over.setVisible(false);
        Lose_new_game.setVisible(false);
        pillar.setVisible(true);
        mistakes = 0;
        correct = 0;
        if(!words.isEmpty()) {
            myWord = getRandomWord();
            myLetters = Arrays.asList(myWord.split(""));
            answer = new ArrayList<>(myLetters.size() * 2);

            for (int i = 0; i < myLetters.size() * 2; i++) {
                if (i % 2 == 0) {
                    answer.add("_");
                } else {
                    answer.add(" ");
                }
            }

            String res = String.join("", answer);
            text.setText(res);
            hint.setText(translateDef.getOneDef(myWord));
            buttons.setDisable(false);
        } else {
            buttons.setVisible(false);
            
        }
    }

    public void newGameInit(){
        chucmung.setVisible(false);
        bandaonhet.setVisible(false);
        diemso.setText("");
        bancomuon.setVisible(false);
        newGameInit.setVisible(false);
        exitGame.setVisible(false);
        buttons.setVisible(false);
        initialize();
        mark.setVisible(true);
        text.setVisible(true);
        hint.setVisible(true);
        for(int i=0; i<27; i++){
            buttons.getChildren().get(i).setDisable(false);
        }
        buttons.setVisible(true);
        System.out.println(words);
    }
    
    public void Exit() {
        Stage stage = (Stage) Exit1.getScene().getWindow();
        stage.close();
    }
}