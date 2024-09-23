package com.kitchensink.ks.data.repository;

import com.kitchensink.ks.data.documents.MemberDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<MemberDocument, String> {}
