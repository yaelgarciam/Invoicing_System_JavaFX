package com.tec.invoicing.service;

import com.tec.invoicing.model.Invoice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InvoiceRepository {

    private final ObservableList<Invoice> invoices = FXCollections.observableArrayList();

    public ObservableList<Invoice> getInvoices() {
        return invoices;
    }

    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
    }
}
