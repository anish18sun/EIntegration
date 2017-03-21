package com.hypervisor.eintegrator.model;

/**
 * Created by dexter on 17/3/17.
 */
public class Entitlement {
   private String entitlementId;

    public String getEntitlementId() {
        return entitlementId;
    }

    @Override
    public String toString() {
        return "Entitlement{" +
                "entitlementId='" + entitlementId + '\'' +
                ", entitlementMemId='" + entitlementMemId + '\'' +
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

    private String entitlementMemId ;
}
