/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectdictionary;

/**
 *
 * @author hahah
 */
public class searchModel {
    int wordId;
    String englishh;
    String vietnamesee;

    public searchModel(int wordId, String englishh, String vietnamesee) {
        this.wordId = wordId;
        this.englishh = englishh;
        this.vietnamesee = vietnamesee;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getEnglishh() {
        return englishh;
    }

    public String getVietnamesee() {
        return vietnamesee;
    }

    public void setEnglishh(String englishh) {
        this.englishh = englishh;
    }

    public void setVietnamesee(String vietnamesee) {
        this.vietnamesee = vietnamesee;
    }
    
    
}
