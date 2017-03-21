package com.hypervisor.eintegrator.model;

/**
 * Created by dexter on 17/3/17.
 */
public class Transaction {
    private String entitlementId,
            entitlementMemId,
            transactionId,
            dueTransactionId,
            bankAccNo,
            ifsc,
            paymentAmount,
            paymentDate,
            status;

    public String getEntitlementId() {
        return entitlementId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "entitlementId='" + entitlementId + '\'' +
                ", entitlementMemId='" + entitlementMemId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", dueTransactionId='" + dueTransactionId + '\'' +
                ", bankAccNo='" + bankAccNo + '\'' +
                ", ifsc='" + ifsc + '\'' +
                ", paymentAmount='" + paymentAmount + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public void setEntitlementId(String entitlementId) {
        this.entitlementId = entitlementId;
    }

    public String getEntitlementMemId() {
        return entitlementMemId;
    }

    public void setEntitlementMemId(String entitlementMemId) {
        this.entitlementMemId = entitlementMemId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDueTransactionId() {
        return dueTransactionId;
    }

    public void setDueTransactionId(String dueTransactionId) {
        this.dueTransactionId = dueTransactionId;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
