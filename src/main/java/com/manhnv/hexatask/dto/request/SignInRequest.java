package com.manhnv.hexatask.dto.request;

import com.manhnv.hexatask.component.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignInRequest {
    @Email
    private String email;
//    @Size(min = 8, message = "Password must be at least 8 characters long")
//    @ValidPassword
    private String password;
}
