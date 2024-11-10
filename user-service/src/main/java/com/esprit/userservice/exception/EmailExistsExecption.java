package com.esprit.userservice.exception;

public class EmailExistsExecption extends RuntimeException{
    public EmailExistsExecption(String message){
        super(message);
    }
}
