package com.example.scheduling_system.dto.payload.response;

public class BodyResponse<TData> {
    private String message;
    private TData data;

    public BodyResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TData getData() {
        return data;
    }

    public void setData(TData data) {
        this.data = data;
    }

    public BodyResponse(String message) {
        this.message = message;
        this.data = null;
    }

    public BodyResponse(String message, TData data) {
        this.message = message;
        this.data = data;
    }

}
