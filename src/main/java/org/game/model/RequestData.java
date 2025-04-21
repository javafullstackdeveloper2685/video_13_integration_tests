package org.game.model;

public class RequestData {
    private String message;

    public RequestData() {
    }

    public RequestData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
