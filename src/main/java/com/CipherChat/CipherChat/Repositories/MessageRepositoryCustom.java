package com.CipherChat.CipherChat.Repositories;

import org.bson.Document;
import java.util.List;

public interface MessageRepositoryCustom {
    List<Document> getChatSummaries(String userId);
}
