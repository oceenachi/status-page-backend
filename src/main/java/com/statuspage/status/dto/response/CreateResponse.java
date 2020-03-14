package com.statuspage.status.dto.response;

public class CreateResponse {

    private Object createdObject;
    private String message;

    public CreateResponse(Object createdObject, String message) {
        this.createdObject = createdObject;
        this.message = message;
    }

    public Object getCreatedObject() {
        return createdObject;
    }

    public void setCreatedObject(Object createdObject) {
        this.createdObject = createdObject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
