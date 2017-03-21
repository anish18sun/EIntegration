package com.hypervisor.eintegrator.model;

/**
 * Created by dexter on 20/3/17.
 */
public class Message {
    String mes;
    int type ;

    @Override
    public String toString() {
        return "Message{" +
                "mes='" + mes + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMes() {
        return mes;

    }

    public void setMes(String mes) {
        this.mes = mes;
    }

}
