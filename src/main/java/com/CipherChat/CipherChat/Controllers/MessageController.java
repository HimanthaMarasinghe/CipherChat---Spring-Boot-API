package com.CipherChat.CipherChat.Controllers;

import com.CipherChat.CipherChat.Repositories.MessageRepository;
import com.CipherChat.CipherChat.Models.Message;
import com.CipherChat.CipherChat.Repositories.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {

    @Autowired
    MessageRepository MessageRepo;

    @Autowired
    UserRepository UserRepo;

    @GetMapping("/getChats/{userId}")
    public ResponseEntity<List<Document>> getUserChats(@PathVariable String userId) {
        System.out.println(userId);
        // Call the aggregation method and get the result
        List<Document> chatSummaries = MessageRepo.getChatSummaries(userId);
        return ResponseEntity.ok(chatSummaries);  // Spring Boot will automatically convert to JSON
    }


    @GetMapping("/getConversation/{user1Id}/{user2Id}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable String user1Id, @PathVariable String user2Id) {
        if(user1Id.equals(user2Id) || !UserRepo.existsById(user1Id) || !UserRepo.existsById(user2Id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(MessageRepo.findConversation(user1Id, user2Id));
    }

    @PostMapping("/newMessage")
    public Message addMessage(@RequestBody Message message) {
        if (!UserRepo.existsById(message.getSenderId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender ID not found");
        }
        if (!UserRepo.existsById(message.getReceiverId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver ID not found");
        }
        return MessageRepo.save(message);
    }

}
