package com.tec.invoicing.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InvoiceItem {

    private final StringProperty productName = new SimpleStringProperty();
    private final StringProperty sku = new SimpleStringProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final DoubleProperty unitPrice = new SimpleDoubleProperty();
    private final DoubleProperty subtotal = new SimpleDoubleProperty();

    public InvoiceItem(Product product, int quantity) {
        this(product.getName(), product.getSku(), product.getUnitPrice(), quantity);
    }

    public InvoiceItem(String productName, String sku, double unitPrice, int quantity) {
        setProductName(productName);
        setSku(sku);
        setUnitPrice(unitPrice);
        setQuantity(quantity);
        subtotal.bind(this.quantity.multiply(this.unitPrice));
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public StringProperty productNameProperty() {
        return productName;
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

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
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

    public double getSubtotal() {
        return subtotal.get();
    }

    public DoubleProperty subtotalProperty() {
        return subtotal;
    }
}
