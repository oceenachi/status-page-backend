package com.statuspage.status.exception;

public class RequestFailException extends RuntimeException{

    public RequestFailException(String message){
        super(message);
    }
}
