package com.hypervisor.eintegrator.model;

/**
 * Created by dexter on 18/3/17.
 */
public class EntitlementInformation {
    Entitlement entitlement;
    Accural accural;

    public Entitlement getEntitlement() {
        return entitlement;
    }

    @Override
    public String toString() {
        return "EntitlementInformation{" +
                "entitlement=" + entitlement +
                ", accural=" + accural +
                '}';
    }

    public void setEntitlement(Entitlement entitlement) {
        this.entitlement = entitlement;
    }

    public Accural getAccural() {
        return accural;
    }

    public void setAccural(Accural accural) {
        this.accural = accural;
    }
}
