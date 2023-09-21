package com.service.kookchild.domain.chatgpt.service;

import com.service.kookchild.domain.chatgpt.config.ChatGptConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatGptService {
    @Value("${CHATGPT-API-KEY}")
    private String apiKey;

    public String sendRequestToChatGPT(String question) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);
        headers.setContentType(MediaType.valueOf(ChatGptConfig.MEDIA_TYPE));

        Map<String, Object> message = new HashMap<>();
        message.put("role", ChatGptConfig.ROLE);
        message.put("content", question);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", ChatGptConfig.MODEL);
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", ChatGptConfig.MAX_TOKEN);
        requestBody.put("temperature", ChatGptConfig.TEMPERATURE);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(ChatGptConfig.URL, entity, Map.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();

            List<?> choices = (List<?>) responseBody.get("choices");
            if (choices.isEmpty()) {
                throw new RuntimeException("Choices list is empty!");
            }

            Object choiceObject = choices.get(0);
            if (!(choiceObject instanceof Map)) {
                throw new RuntimeException("Expected a Map but found: " + choiceObject.getClass());
            }

            Map<String, Object> choiceMap = (Map<String, Object>) choiceObject;
            Map<String, Object> messageMap = (Map<String, Object>) choiceMap.get("message");
            String content = (String) messageMap.get("content");
            return content;
        } else {
            throw new RuntimeException("Failed to get response from OpenAI");
        }
    }

    public String makeImages(String missionTitle){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", missionTitle);
        requestBody.put("n", ChatGptConfig.IMAGE_COUNT);
        requestBody.put("size", ChatGptConfig.IMAGE_SIZE);

        HttpEntity<Map<String, Object>> requestHttpEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URLIMAGE,
                requestHttpEntity,
                Map.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();

            List<?> datas = (List<?>) responseBody.get("data");
            if (datas.isEmpty()) {
                throw new RuntimeException("Datas list is empty!");
            }

            Object dataObject = datas.get(0);
            if (!(dataObject instanceof Map)) {
                throw new RuntimeException("Expected a Map but found: " + dataObject.getClass());
            }

            Map<String, Object> dataMap = (Map<String, Object>) dataObject;
            String url = (String) dataMap.get("url");

            return url;
        } else {
            throw new RuntimeException("Failed to get response from server");
        }
    }
}
