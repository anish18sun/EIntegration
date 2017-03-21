package com.hypervisor.eintegrator.model;

/**
 * Created by dexter on 17/3/17.
 */
public class Accural {
    private String entitlementId;
    private String entitlementMemId;
    private String transactionId;
    private String dueDate;

    public String getEntitlementId() {
        return entitlementId;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    @Override
    public String toString() {
        return "Accural{" +
                "entitlementId='" + entitlementId + '\'' +
                ", entitlementMemId='" + entitlementMemId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", dueAmount='" + dueAmount + '\'' +
                '}';
    }

    public void setDueAmount(String dueAmount) {
        this.dueAmount = dueAmount;
    }

    private String dueAmount;
}
