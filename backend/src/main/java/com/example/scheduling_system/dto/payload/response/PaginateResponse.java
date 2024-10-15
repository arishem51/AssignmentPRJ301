package com.example.scheduling_system.dto.payload.response;

import java.util.List;

import com.example.scheduling_system.dto.Meta;

public class PaginateResponse<T> {
    private List<T> data;
    private Meta meta;

    public PaginateResponse() {

    }

    public PaginateResponse(List<T> data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
