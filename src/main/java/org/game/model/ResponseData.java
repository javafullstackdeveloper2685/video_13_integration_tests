package org.game.model;

public class ResponseData {
    private String status;
    private String echoMessage;

    public ResponseData(String status, String echoMessage) {
        this.status = status;
        this.echoMessage = echoMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEchoMessage() {
        return echoMessage;
    }

    public void setEchoMessage(String echoMessage) {
        this.echoMessage = echoMessage;
    }
}
