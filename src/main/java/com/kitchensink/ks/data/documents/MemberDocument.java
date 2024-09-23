package com.kitchensink.ks.data.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("members")
public class MemberDocument {
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
}
