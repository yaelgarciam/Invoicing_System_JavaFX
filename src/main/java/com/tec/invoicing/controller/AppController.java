package com.tec.invoicing.controller;

import com.tec.invoicing.service.CatalogService;
import com.tec.invoicing.service.InvoiceRepository;
import com.tec.invoicing.service.InvoiceService;
import javafx.fxml.FXML;

public class AppController {

    @FXML
    private InvoiceController invoiceViewController;

    @FXML
    private HistoryController historyViewController;

    private final CatalogService catalogService = new CatalogService();
    private final InvoiceRepository invoiceRepository = new InvoiceRepository();
    private final InvoiceService invoiceService = new InvoiceService(invoiceRepository);

    @FXML
    private void initialize() {
        historyViewController.configure(invoiceRepository);
        invoiceViewController.configure(catalogService, invoiceService);
    }
}
