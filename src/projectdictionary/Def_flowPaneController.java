/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projectdictionary;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * FXML Controller class
 *
 * @author hahah
 */
public class Def_flowPaneController implements Initializable {

    @FXML
    private FlowPane flowPaneChild;
    
    @FXML
    private TextFlow definition;

    @FXML
    private TextFlow example;

    @FXML
    private TextFlow meaning;
    
    @FXML
    private Button translate;
    
    public static String getMeanVietNam(String input) {
        String scriptUrl = "https://script.google.com/macros/s/AKfycbxd1TdG3ndXOof5gyKLt_nUAe0YfpPTtDB5XK2NYDkYth9TbLSqlq-2jiy_j-Y-glrC/exec";
        String source = "en";
        String target = "vi";
        String result = null;
        try {
            // chuyển input thành một dạng gì đó để truyền vào đường link
            String encodedInput = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
            // gọi đường dẫn
            String url = scriptUrl + "?source_lang=" + source + "&target_lang=" + target + "&text=" + encodedInput;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Gửi yêu cầu và nhận phản hồi
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 302) {
                // Lấy URL chuyển hướng
                String redirectUrl = response.headers().firstValue("Location").orElse("");
                if (!redirectUrl.isEmpty()) {
                    // Tạo yêu cầu HTTP mới cho trang chuyển hướng
                    HttpRequest redirectRequest = HttpRequest.newBuilder()
                            .uri(URI.create(redirectUrl))
                            .GET()
                            .build();
                    
                    // Gửi yêu cầu và nhận phản hồi cho trang chuyển hướng
                    HttpResponse<String> redirectResponse = HttpClient.newHttpClient().send(redirectRequest, HttpResponse.BodyHandlers.ofString());
                    
                    // chuyển kết quả nhận về thành một chuối json.
                    JsonReader jsonReader = Json.createReader(new StringReader(redirectResponse.body()));
                    JsonObject jsonResponse = jsonReader.readObject();
                    
                    // In kết quả từ trang chuyển hướng
                    result = jsonResponse.getString("translateText");
                    

                } else {
                    System.err.println("URL chuyển hướng không tồn tại.");
                }
            } else if (response.statusCode() == 200) {
                // In kết quả từ Google Apps Script
                System.out.println("Kết quả từ Google Apps Script: " + response.body());
            } else {
                System.err.println("Lỗi trong quá trình gửi yêu cầu.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public void setData(Pair<String, String> defxEx) {
        Text defText = new Text(defxEx.getKey());
        defText.setFont(Font.font("Arial", 24));
        definition.getChildren().add(defText);
        Text exText = new Text(defxEx.getValue());
        example.getChildren().add(exText);
        translate.setOnAction(e -> {
            Text meanText = new Text("\nVietnamese: " + getMeanVietNam(defxEx.getKey()) + "\n");
            meaning.getChildren().add(meanText);
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
//projectdictionary.Def_flowPaneController
