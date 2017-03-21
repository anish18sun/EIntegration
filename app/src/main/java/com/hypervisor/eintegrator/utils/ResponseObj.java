package com.hypervisor.eintegrator.utils;

/**
 * Created by dexter on 20/3/17.
 */
public class ResponseObj {
    private String response;
    private boolean result;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ResponseObj{" +
                "response='" + response + '\'' +
                ", result=" + result +
                '}';
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
