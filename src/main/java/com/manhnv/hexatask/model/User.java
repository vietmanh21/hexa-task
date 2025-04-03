package com.manhnv.hexatask.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Builder
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String username;
}
