package com.service.kookchild.domain.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGptConfig {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String MODEL = "gpt-3.5-turbo";
    public static final Integer MAX_TOKEN = 300;
    public static final Boolean STREAM = false;
    public static final String ROLE = "user";
    public static final Double TEMPERATURE = 0.6;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String URL = "https://api.openai.com/v1/chat/completions";
    public static final Double TOP_P = 1.0;
}