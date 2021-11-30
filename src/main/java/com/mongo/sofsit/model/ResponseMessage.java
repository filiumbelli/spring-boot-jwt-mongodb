package com.mongo.sofsit.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseMessage {


    private String message;
    private int status;
}
