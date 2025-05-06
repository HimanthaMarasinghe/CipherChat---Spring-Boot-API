package com.CipherChat.CipherChat.Models;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.Timestamp;
import java.util.Date;

@Document
public class Message {
    private String senderId;
    private String receiverId;
    private String message;
    private String status;
    private Date sendAt;
    private Date receivedAt;
    
    public Message(String senderId, String receiverId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.status = "Sent";
        this.sendAt = new Date();
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }
}
