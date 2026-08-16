package com.example.cloudvault.dto;

public class ApiResp {

    private String status;
    private Object res;
    private String error;

    public ApiResp(String status, Object res) {
        this.status = status;
        this.res = res;
    }
    public ApiResp(String status, String err) {
        this.status = status;
        this.error = error;
    }


    public String getStatus() {
        return status;
    }

    public Object getRes() {
        return res;
    }
    public String geterror() {
        return error;
    }

}