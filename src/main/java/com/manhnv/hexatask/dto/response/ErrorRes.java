package com.manhnv.hexatask.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ErrorRes {
    private String status;
    private String message;
    private List<String> details;
}
