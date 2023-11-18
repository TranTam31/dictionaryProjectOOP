/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectdictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hahah
 */
public class saveWordClass {
    private static final String FILE_NAME = "C:\\Users\\hahah\\Downloads\\savedWord.txt";
    public static boolean checkWordExists(String wordToCheck) {
        List<String> savedWords = readWordsFromFile();
        return savedWords.contains(wordToCheck.toLowerCase());
    }

    // Phương thức lưu từ vào file
    public static void saveWord(String wordToSave) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(wordToSave.toLowerCase() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức đọc từ file và trả về danh sách các từ đã lưu
    public static List<String> readWordsFromFile() {
        List<String> savedWords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String word;
            while ((word = reader.readLine()) != null) {
                savedWords.add(word.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedWords;
    }
    public static void removeWordFromFile(String wordToRemove) {
        // Đọc toàn bộ nội dung từ file vào danh sách
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Tìm và xóa từ cần xóa khỏi danh sách
        lines.removeIf(word -> word.equals(wordToRemove));

        // Ghi lại danh sách đã cập nhật vào file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
