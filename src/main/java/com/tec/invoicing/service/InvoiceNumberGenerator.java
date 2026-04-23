package com.tec.invoicing.service;

import java.time.LocalDate;

public class InvoiceNumberGenerator {

    private int counter = 1;

    public String nextInvoiceNumber() {
        int year = LocalDate.now().getYear();
        return String.format("INV-%d-%04d", year, counter++);
    }
}
