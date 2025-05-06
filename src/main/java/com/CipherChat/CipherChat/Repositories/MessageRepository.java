package com.CipherChat.CipherChat.Repositories;

import com.CipherChat.CipherChat.Models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    @Query("{$or: [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ]}")
    List<Message> findConversation(String user1Id, String user2Id);
}
