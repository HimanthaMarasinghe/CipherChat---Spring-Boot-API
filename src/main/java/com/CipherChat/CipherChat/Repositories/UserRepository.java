package com.CipherChat.CipherChat.Repositories;

import com.CipherChat.CipherChat.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByName(String name);
}
