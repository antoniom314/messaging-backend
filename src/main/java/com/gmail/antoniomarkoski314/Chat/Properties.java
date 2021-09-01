package com.gmail.antoniomarkoski314.Chat;

public class Properties {

    // Url
    public static final String authenticateUrl = "/api/authenticate";
    public static final String registerUrl = "/api/register";
    public static final String getUsersUrl = "/api/get-users";
    public static final String socketUrl = "/socket";
    public static final String errorUrl = "/error";

    // Jwt messaging
    public static final String SECRET = "YourSecretKey";
    public static final int EXPIRATION_TIME = 52 * 7 * 24 * 60 * 60 * 1000;
    public static final String AUTHENTICATION_HEADER = "Authentication";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BASIC_PREFIX = "Basic ";
    public static final String TOKEN_PREFIX = "Bearer ";
}
