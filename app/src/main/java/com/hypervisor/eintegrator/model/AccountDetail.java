package com.hypervisor.eintegrator.model;

/**
 * Created by dexter on 16/3/17.
 */
public class AccountDetail {
    String AADHAR_ID
            ,M_ID
            ,BHAMASHAH_ACK_ID
            ,NAME
            ,MOBILE_NO
            ,FATHER_NAME_ENG;

    public String getAADHAR_ID() {
        return AADHAR_ID;
    }

    @Override
    public String toString() {
        return "AccountDetail{" +
                "AADHAR_ID='" + AADHAR_ID + '\'' +
                ", M_ID='" + M_ID + '\'' +
                ", BHAMASHAH_ACK_ID='" + BHAMASHAH_ACK_ID + '\'' +
                ", NAME='" + NAME + '\'' +
                ", MOBILE_NO='" + MOBILE_NO + '\'' +
                ", FATHER_NAME_ENG='" + FATHER_NAME_ENG + '\'' +
                '}';
    }

    public void setAADHAR_ID(String AADHAR_ID) {
        this.AADHAR_ID = AADHAR_ID;
    }

    public String getM_ID() {
        return M_ID;
    }

    public void setM_ID(String m_ID) {
        M_ID = m_ID;
    }

    public String getBHAMASHAH_ACK_ID() {
        return BHAMASHAH_ACK_ID;
    }

    public void setBHAMASHAH_ACK_ID(String BHAMASHAH_ACK_ID) {
        this.BHAMASHAH_ACK_ID = BHAMASHAH_ACK_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getFATHER_NAME_ENG() {
        return FATHER_NAME_ENG;
    }

    public void setFATHER_NAME_ENG(String FATHER_NAME_ENG) {
        this.FATHER_NAME_ENG = FATHER_NAME_ENG;
    }
}
