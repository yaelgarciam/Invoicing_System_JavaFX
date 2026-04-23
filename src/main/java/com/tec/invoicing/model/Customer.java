package com.tec.invoicing.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty taxId = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();

    public Customer(String name, String taxId, String address) {
        setName(name);
        setTaxId(taxId);
        setAddress(address);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getTaxId() {
        return taxId.get();
    }

    public void setTaxId(String taxId) {
        this.taxId.set(taxId);
    }

    public StringProperty taxIdProperty() {
        return taxId;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    @Override
    public String toString() {
        return getName();
    }
}
