package com.manhnv.hexatask.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Builder
@Getter
@Setter
public class User {
    @Id
    private String userId;
    private String email;
    private String password;
    private String userName;
    private String avatar;

    private Set<Long> roleIds = new HashSet<>();
}
