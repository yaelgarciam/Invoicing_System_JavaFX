package com.tec.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Invoice {

    private final StringProperty invoiceNumber = new SimpleStringProperty();
    private final StringProperty customerName = new SimpleStringProperty();
    private final StringProperty customerTaxId = new SimpleStringProperty();
    private final StringProperty customerAddress = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> issueDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>();
    private final StringProperty status = new SimpleStringProperty();
    private final DoubleProperty subtotal = new SimpleDoubleProperty();
    private final DoubleProperty tax = new SimpleDoubleProperty();
    private final DoubleProperty total = new SimpleDoubleProperty();
    private final ObservableList<InvoiceItem> items = FXCollections.observableArrayList(
            item -> new Observable[]{item.quantityProperty(), item.unitPriceProperty(), item.subtotalProperty()}
    );

    public Invoice(
            String invoiceNumber,
            String customerName,
            String customerTaxId,
            String customerAddress,
            LocalDate issueDate,
            LocalDate dueDate,
            List<InvoiceItem> items,
            String status,
            double subtotal,
            double tax,
            double total
    ) {
        setInvoiceNumber(invoiceNumber);
        setCustomerName(customerName);
        setCustomerTaxId(customerTaxId);
        setCustomerAddress(customerAddress);
        setIssueDate(issueDate);
        setDueDate(dueDate);
        this.items.setAll(items);
        setStatus(status);
        setSubtotal(subtotal);
        setTax(tax);
        setTotal(total);
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }

    public StringProperty invoiceNumberProperty() {
        return invoiceNumber;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public String getCustomerTaxId() {
        return customerTaxId.get();
    }

    public void setCustomerTaxId(String customerTaxId) {
        this.customerTaxId.set(customerTaxId);
    }

    public StringProperty customerTaxIdProperty() {
        return customerTaxId;
    }

    public String getCustomerAddress() {
        return customerAddress.get();
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress.set(customerAddress);
    }

    public StringProperty customerAddressProperty() {
        return customerAddress;
    }

    public LocalDate getIssueDate() {
        return issueDate.get();
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate.set(issueDate);
    }

    public ObjectProperty<LocalDate> issueDateProperty() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    public ObservableList<InvoiceItem> getItems() {
        return items;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public double getSubtotal() {
        return subtotal.get();
    }

    public void setSubtotal(double subtotal) {
        this.subtotal.set(subtotal);
    }

    public DoubleProperty subtotalProperty() {
        return subtotal;
    }

    public double getTax() {
        return tax.get();
    }

    public void setTax(double tax) {
        this.tax.set(tax);
    }

    public DoubleProperty taxProperty() {
        return tax;
    }

    public double getTotal() {
        return total.get();
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public DoubleProperty totalProperty() {
        return total;
    }
}
