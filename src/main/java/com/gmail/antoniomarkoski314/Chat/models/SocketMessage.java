package com.gmail.antoniomarkoski314.Chat.models;

public class SocketMessage {
    String userFrom;
    String userTo;
    String message;

    public SocketMessage(String userFrom, String userTo, String message) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.message = message;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "userFrom='" + userFrom + '\'' +
                ", userTo='" + userTo + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
