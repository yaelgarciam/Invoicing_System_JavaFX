package com.tec.invoicing.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty sku = new SimpleStringProperty();
    private final DoubleProperty unitPrice = new SimpleDoubleProperty();

    public Product(String name, String sku, double unitPrice) {
        setName(name);
        setSku(sku);
        setUnitPrice(unitPrice);
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

    public String getSku() {
        return sku.get();
    }

    public void setSku(String sku) {
        this.sku.set(sku);
    }

    public StringProperty skuProperty() {
        return sku;
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return getName();
    }
}
