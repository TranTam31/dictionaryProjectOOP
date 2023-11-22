/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectdictionary;

/**
 *
 * @author hahah
 */
public class addWord {
    private int wordAddID;
    private String english;
    private String vietnamese;

    public addWord(int wordAddID, String english, String vietnamese) {
        this.wordAddID = wordAddID;
        this.english = english;
        this.vietnamese = vietnamese;
    }

    public int getWordAddID() {
        return wordAddID;
    }

    public String getEnglish() {
        return english;
    }

    public String getVietnamese() {
        return vietnamese;
    }
    
    
}
