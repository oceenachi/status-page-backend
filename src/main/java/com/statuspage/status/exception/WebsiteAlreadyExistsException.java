package com.statuspage.status.exception;

public class WebsiteAlreadyExistsException extends RuntimeException {
    public WebsiteAlreadyExistsException(String message){
        super(message);
    }
}
