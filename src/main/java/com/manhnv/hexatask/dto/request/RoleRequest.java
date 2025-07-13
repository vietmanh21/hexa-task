package com.manhnv.hexatask.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class RoleRequest {
    @NotEmpty
    private String name;
}
