package com.hypervisor.eintegrator.model;

/**
 * Created by dexter on 17/3/17.
 */
public class TransactionDetails {
    private String ServiceName;
    private String officeId;
    private String BillAmount;
    private String ConsumerName;
    private String ConsumerKeyValue;
    private String PartPaymentAllow;
    private String PartPaymentType;

    @Override
    public String toString() {
        return "TransactionDetails{" +
                "ServiceName='" + ServiceName + '\'' +
                ", officeId='" + officeId + '\'' +
                ", BillAmount='" + BillAmount + '\'' +
                ", ConsumerName='" + ConsumerName + '\'' +
                ", ConsumerKeyValue='" + ConsumerKeyValue + '\'' +
                ", PartPaymentAllow='" + PartPaymentAllow + '\'' +
                ", PartPaymentType='" + PartPaymentType + '\'' +
                ", LookUpId='" + LookUpId + '\'' +
                '}';
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(String billAmount) {
        BillAmount = billAmount;
    }

    public String getConsumerName() {
        return ConsumerName;
    }

    public void setConsumerName(String consumerName) {
        ConsumerName = consumerName;
    }

    public String getConsumerKeyValue() {
        return ConsumerKeyValue;
    }

    public void setConsumerKeyValue(String consumerKeyValue) {
        ConsumerKeyValue = consumerKeyValue;
    }

    public String getPartPaymentAllow() {
        return PartPaymentAllow;
    }

    public void setPartPaymentAllow(String partPaymentAllow) {
        PartPaymentAllow = partPaymentAllow;
    }

    public String getPartPaymentType() {
        return PartPaymentType;
    }

    public void setPartPaymentType(String partPaymentType) {
        PartPaymentType = partPaymentType;
    }

    public String getLookUpId() {
        return LookUpId;
    }

    public void setLookUpId(String lookUpId) {
        LookUpId = lookUpId;
    }

    private String LookUpId;
}
