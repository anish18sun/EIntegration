package com.hypervisor.eintegrator.model;

/**
 * Created by dexter on 17/3/17.
 */

public class BillDetails {
    String LableName, LableValue;


    @Override
    public String toString() {
        return LableName + " : " + LableValue;
    }


    public String getLableValue() {
        return LableValue;
    }

    public void setLableValue(String labelValue) {
        LableValue = labelValue;
    }

    public String getLabelName() {
        return LableName;
    }

    public void setLabelName(String labelName) {
        LableName = labelName;
    }
}
