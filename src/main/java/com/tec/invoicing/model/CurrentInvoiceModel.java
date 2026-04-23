package com.tec.invoicing.model;

import java.time.LocalDate;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CurrentInvoiceModel {

    private final ObjectProperty<Customer> selectedCustomer = new SimpleObjectProperty<>();
    private final StringProperty customerTaxId = new SimpleStringProperty("");
    private final StringProperty customerAddress = new SimpleStringProperty("");
    private final StringProperty invoiceNumber = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> issueDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>();
    private final ObservableList<InvoiceItem> items = FXCollections.observableArrayList(
            item -> new Observable[]{item.quantityProperty(), item.unitPriceProperty(), item.subtotalProperty()}
    );
    private final ReadOnlyDoubleWrapper subtotal = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper tax = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper total = new ReadOnlyDoubleWrapper();

    public CurrentInvoiceModel() {
        selectedCustomer.addListener((observable, oldCustomer, newCustomer) -> {
            if (newCustomer == null) {
                setCustomerTaxId("");
                setCustomerAddress("");
                return;
            }

            setCustomerTaxId(newCustomer.getTaxId());
            setCustomerAddress(newCustomer.getAddress());
        });

        subtotal.bind(Bindings.createDoubleBinding(
                () -> items.stream().mapToDouble(InvoiceItem::getSubtotal).sum(),
                items
        ));
        tax.bind(subtotal.multiply(0.16));
        total.bind(subtotal.add(tax));

        issueDate.set(LocalDate.now());
        dueDate.set(LocalDate.now().plusDays(30));
    }

    public void reset(String nextInvoiceNumber) {
        setInvoiceNumber(nextInvoiceNumber);
        setSelectedCustomer(null);
        setIssueDate(LocalDate.now());
        setDueDate(LocalDate.now().plusDays(30));
        items.clear();
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer.get();
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer.set(selectedCustomer);
    }

    public ObjectProperty<Customer> selectedCustomerProperty() {
        return selectedCustomer;
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

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }

    public StringProperty invoiceNumberProperty() {
        return invoiceNumber;
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

    public double getSubtotal() {
        return subtotal.get();
    }

    public ReadOnlyDoubleProperty subtotalProperty() {
        return subtotal.getReadOnlyProperty();
    }

    public double getTax() {
        return tax.get();
    }

    public ReadOnlyDoubleProperty taxProperty() {
        return tax.getReadOnlyProperty();
    }

    public double getTotal() {
        return total.get();
    }

    public ReadOnlyDoubleProperty totalProperty() {
        return total.getReadOnlyProperty();
    }
}
