package com.CipherChat.CipherChat.Repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Document> getChatSummaries(String userId) {
        System.out.println("getChatSummaries");
        // Aggregation query combining messages and users (lookup)
        Aggregation aggregation = Aggregation.newAggregation(
                // Step 1: Get all messages involving the user
                Aggregation.match(new Criteria().orOperator(
                        Criteria.where("senderId").is(userId),
                        Criteria.where("receiverId").is(userId)
                )),

                // Step 2: Compute "chatPartnerId" (the other user)
                Aggregation.project("senderId", "receiverId", "message", "status", "sendAt")
                        .and(
                                ConditionalOperators.when(Criteria.where("senderId").is(userId))
                                .thenValueOf("receiverId")
                                .otherwiseValueOf("senderId")
                        ).as("chatPartnerId"),

                // Step 3: Sort by latest message
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "sendAt")),

                // Step 4: Group by chatPartnerId
                Aggregation.addFields().addField("isUnread")
                        .withValue(
                                ConditionalOperators.when(
                                        Criteria.where("receiverId").is(userId)
                                                .and("status").ne("read")
                                ).then(1).otherwise(0)
                        ).build(),

                Aggregation.group("chatPartnerId")
                        .first("message").as("lastMessage")
                        .first("sendAt").as("lastMessageTime")
                        .first("chatPartnerId").as("chatPartnerId")
                        .sum("isUnread").as("unreadCount"),

                // Step 5: Get user details of chat partner
                Aggregation.addFields().addField("chatPartnerObjectId")
                        .withValue(ConvertOperators.ToObjectId.toObjectId("$chatPartnerId")).build(),

                Aggregation.lookup("user", "chatPartnerObjectId", "_id", "chatPartnerInfo"),

                // Step 6: Optional – flatten chat partner info
                Aggregation.unwind("chatPartnerInfo")
        );


        return mongoTemplate.aggregate(aggregation, "message", Document.class).getMappedResults();
    }
}
