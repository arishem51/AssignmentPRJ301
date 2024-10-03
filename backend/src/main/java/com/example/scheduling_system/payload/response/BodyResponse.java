package com.example.scheduling_system.payload.response;

import java.util.HashMap;

public class BodyResponse<TData> {
    private String message;
    private TData data;

    public BodyResponse() {
    }

    public BodyResponse(String message) {
        this.message = message;
        this.data = null;
    }

    public BodyResponse(String message, TData data) {
        this.message = message;
        this.data = data;
    }

    public HashMap<Object, Object> getBodyResponse() {
        var response = new HashMap<Object, Object>();
        response.put("message", this.message);
        response.put("data", this.data);
        return response;
    }
}
