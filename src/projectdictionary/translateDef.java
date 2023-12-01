/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectdictionary;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
/**
 *
 * @author hahah
 */
public class translateDef {
    public static JsonArray getDef(JsonArray meanings) {
        JsonArrayBuilder meaningArray = Json.createArrayBuilder();
        for(JsonValue meaning : meanings) {
            JsonObjectBuilder meaningObject = Json.createObjectBuilder();
            JsonObject mean = (JsonObject) meaning;
            meaningObject.add("partOfSpeech", mean.getString("partOfSpeech"));
            JsonArray definitions = mean.getJsonArray("definitions");
            JsonArrayBuilder definitionArray = Json.createArrayBuilder();
            for(JsonValue definition : definitions) {
                JsonObject definitionObject = (JsonObject) definition;
                JsonObjectBuilder definitionxEx = Json.createObjectBuilder();
                if (definitionObject.containsKey("definition")) {
                    definitionxEx.add("definition", definitionObject.getString("definition"));
                }
                if (definitionObject.containsKey("example")) {
                    definitionxEx.add("example", definitionObject.getString("example"));
                }
                else {
                    definitionxEx.add("example", "Xin lỗi trong trường hợp này chúng tôi chưa tạo ra ví dụ!");
                }
                definitionArray.add(definitionxEx);
            }
            meaningObject.add("definitionArray", definitionArray);
            meaningArray.add(meaningObject);
        }
        return meaningArray.build();
    }

    public static JsonObject getPhonetic(JsonArray phonetics) {
        JsonObject phoneticGet = null;
        for (JsonValue phonetic : phonetics) {
            JsonObject phoneticObject = (JsonObject) phonetic;
            if (phoneticObject.containsKey("text")) {
                phoneticGet = Json.createObjectBuilder()
                    .add("phonetic", phoneticObject.getString("text"))
                    .build();
                break;
            }
        }
        return phoneticGet;
    }

    public static JsonObject getAudio(JsonArray phonetics) {
        JsonObject audioGet = null;
        for (JsonValue phonetic : phonetics) {
            JsonObject phoneticObject = (JsonObject) phonetic;
            if (phoneticObject.containsKey("audio")) {
                if (!phoneticObject.getString("audio").isEmpty()) {
                    audioGet = Json.createObjectBuilder()
                    .add("audio", phoneticObject.getString("audio"))
                    .build();
                    break;
                }
            }
        }
        return audioGet;
    }

    public static JsonObject getData(String input) {
        String scriptUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/";
        JsonObjectBuilder result = Json.createObjectBuilder();
        try {
            String url = scriptUrl + input;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Gửi yêu cầu và nhận phản hồi
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // In kết quả từ Google Apps Script
                JsonReader jsonReader = Json.createReader(new StringReader(response.body()));
                JsonArray wordArray = jsonReader.readArray();
                JsonObject Phonenic = null;
                JsonObject Audio = null;
                JsonArrayBuilder Defs = Json.createArrayBuilder();
                for (JsonValue word : wordArray) {
                    JsonObject wordObject = (JsonObject) word;
                    JsonArray meanings = wordObject.getJsonArray("meanings");
                    JsonArray phonetics = wordObject.getJsonArray("phonetics");

                    if(!phonetics.isEmpty()) {
                        if(getPhonetic(phonetics) != null) {
                            Phonenic = getPhonetic(phonetics);
                        }
                        if(getAudio(phonetics) != null) {
                            Audio = getAudio(phonetics);
                        }
                    }
                    JsonArray meanGet = getDef(meanings);
                    for(JsonValue obj : meanGet) {
                        JsonObject object = (JsonObject) obj;
                        Defs.add(object);
                    }
                }
                
                if(Phonenic != null) {
                    result.add("phonetic", Phonenic.getString("phonetic"));
                } else {
                    result.add("phonetic", "Từ này hiện chưa cập nhật phiên âm!");
                }
                if(Audio != null) {
                    result.add("audio", Audio.getString("audio"));
                } else {
                    result.add("audio", JsonValue.NULL);
                }
                result.add("meanings", Defs);
            } else {
                result.add("phonetic", "Từ này hiện chưa được cập nhật!")
                      .add("audio", JsonValue.NULL)
                      .add("meanings", "Rất xin lỗi bạn, từ này chúng tôi chưa cập nhật!");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result.build();
    }
    
    public static String getMeanVietNam(String input, String source, String target) {
        String scriptUrl = "https://script.google.com/macros/s/AKfycbxd1TdG3ndXOof5gyKLt_nUAe0YfpPTtDB5XK2NYDkYth9TbLSqlq-2jiy_j-Y-glrC/exec";
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
    
    public static String getOneDef(String input) {
        String scriptUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/";
        String defFirst = null;
        try {
            String url = scriptUrl + input;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Gửi yêu cầu và nhận phản hồi
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonReader jsonReader = Json.createReader(new StringReader(response.body()));
                JsonArray wordArray = jsonReader.readArray();
                JsonObject wordObject = wordArray.getJsonObject(0);
                JsonArray meanings = wordObject.getJsonArray("meanings");
                JsonObject mean = meanings.getJsonObject(0);
                JsonArray definitions = mean.getJsonArray("definitions");
                JsonObject firstObject = definitions.getJsonObject(0);
                defFirst = firstObject.getString("definition");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return defFirst;
    }
    
}
