
package projectdictionary;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author hassssss
 */
public class Word {
    private String partOfSpeech;
    private ArrayList<Pair<String, String>> defxEx;

    public Word(String partOfSpeech, ArrayList<Pair<String, String>> defxEx) {
        this.partOfSpeech = partOfSpeech;
        this.defxEx = defxEx;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public ArrayList<Pair<String, String>> getDefxEx() {
        return defxEx;
    }

    public void setDefxEx(ArrayList<Pair<String, String>> defxEx) {
        this.defxEx = defxEx;
    }
}
