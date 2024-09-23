package com.kitchensink.ks.data.repository;

import com.kitchensink.ks.data.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserDocument, String> {
    @Query("{username:'?0'}")
    Optional<UserDocument> findByUsername(String username);
}
