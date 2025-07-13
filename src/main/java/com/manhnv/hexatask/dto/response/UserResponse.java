package com.manhnv.hexatask.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private String userId;
    private String email;
    private String userName;
    private String avatar;
    private String accessToken;
    private String refreshToken;
    private List<String> authority;
}
