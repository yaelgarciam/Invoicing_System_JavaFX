package com.tec.invoicing.controller;

import com.tec.invoicing.model.Invoice;
import com.tec.invoicing.model.InvoiceItem;
import com.tec.invoicing.service.InvoiceRepository;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HistoryController {

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<Invoice> historyTable;

    @FXML
    private TableColumn<Invoice, String> invoiceNumberColumn;

    @FXML
    private TableColumn<Invoice, String> customerColumn;

    @FXML
    private TableColumn<Invoice, LocalDate> dateColumn;

    @FXML
    private TableColumn<Invoice, Number> totalColumn;

    @FXML
    private TableColumn<Invoice, String> statusColumn;

    @FXML
    private Label detailInvoiceNumberLabel;

    @FXML
    private Label detailCustomerLabel;

    @FXML
    private Label detailTaxIdLabel;

    @FXML
    private Label detailAddressLabel;

    @FXML
    private Label detailIssueDateLabel;

    @FXML
    private Label detailDueDateLabel;

    @FXML
    private Label detailStatusLabel;

    @FXML
    private TableView<InvoiceItem> detailItemsTable;

    @FXML
    private TableColumn<InvoiceItem, String> detailProductColumn;

    @FXML
    private TableColumn<InvoiceItem, String> detailSkuColumn;

    @FXML
    private TableColumn<InvoiceItem, Number> detailQuantityColumn;

    @FXML
    private TableColumn<InvoiceItem, Number> detailUnitPriceColumn;

    @FXML
    private TableColumn<InvoiceItem, Number> detailSubtotalColumn;

    @FXML
    private Label detailSubtotalLabel;

    @FXML
    private Label detailTaxLabel;

    @FXML
    private Label detailTotalLabel;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private FilteredList<Invoice> filteredInvoices;

    @FXML
    private void initialize() {
        configureHistoryTable();
        configureDetailTable();
        configureSelection();
        configureDateFilters();
        clearDetail();
    }

    public void configure(InvoiceRepository invoiceRepository) {
        filteredInvoices = new FilteredList<>(invoiceRepository.getInvoices(), invoice -> true);
        SortedList<Invoice> sortedInvoices = new SortedList<>(filteredInvoices);
        sortedInvoices.comparatorProperty().bind(historyTable.comparatorProperty());
        historyTable.setItems(sortedInvoices);
    }

    private void configureHistoryTable() {
        invoiceNumberColumn.setCellValueFactory(data -> data.getValue().invoiceNumberProperty());
        customerColumn.setCellValueFactory(data -> data.getValue().customerNameProperty());
        dateColumn.setCellValueFactory(data -> data.getValue().issueDateProperty());
        totalColumn.setCellValueFactory(data -> data.getValue().totalProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : dateFormatter.format(value));
            }
        });

        totalColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : currencyFormat.format(value.doubleValue()));
            }
        });
    }

    private void configureDetailTable() {
        detailProductColumn.setCellValueFactory(data -> data.getValue().productNameProperty());
        detailSkuColumn.setCellValueFactory(data -> data.getValue().skuProperty());
        detailQuantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty());
        detailUnitPriceColumn.setCellValueFactory(data -> data.getValue().unitPriceProperty());
        detailSubtotalColumn.setCellValueFactory(data -> data.getValue().subtotalProperty());

        detailUnitPriceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : currencyFormat.format(value.doubleValue()));
            }
        });

        detailSubtotalColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : currencyFormat.format(value.doubleValue()));
            }
        });
    }

    private void configureSelection() {
        historyTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedInvoice) -> {
            if (selectedInvoice == null) {
                clearDetail();
                return;
            }

            showInvoiceDetail(selectedInvoice);
        });
    }

    private void configureDateFilters() {
        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> applyDateFilter());
        toDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> applyDateFilter());
    }

    private void applyDateFilter() {
        if (filteredInvoices == null) {
            return;
        }

        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        filteredInvoices.setPredicate(invoice -> {
            if (invoice == null || invoice.getIssueDate() == null) {
                return false;
            }

            boolean matchesFromDate = fromDate == null || !invoice.getIssueDate().isBefore(fromDate);
            boolean matchesToDate = toDate == null || !invoice.getIssueDate().isAfter(toDate);

            return matchesFromDate && matchesToDate;
        });
    }

    @FXML
    private void handleClearFilter() {
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        applyDateFilter();
    }

    private void showInvoiceDetail(Invoice invoice) {
        detailInvoiceNumberLabel.setText(invoice.getInvoiceNumber());
        detailCustomerLabel.setText(invoice.getCustomerName());
        detailTaxIdLabel.setText(invoice.getCustomerTaxId());
        detailAddressLabel.setText(invoice.getCustomerAddress());
        detailIssueDateLabel.setText(formatDate(invoice.getIssueDate()));
        detailDueDateLabel.setText(formatDate(invoice.getDueDate()));
        detailStatusLabel.setText(invoice.getStatus());
        detailItemsTable.setItems(invoice.getItems());
        detailSubtotalLabel.setText(currencyFormat.format(invoice.getSubtotal()));
        detailTaxLabel.setText(currencyFormat.format(invoice.getTax()));
        detailTotalLabel.setText(currencyFormat.format(invoice.getTotal()));
    }

    private void clearDetail() {
        detailInvoiceNumberLabel.setText("-");
        detailCustomerLabel.setText("-");
        detailTaxIdLabel.setText("-");
        detailAddressLabel.setText("-");
        detailIssueDateLabel.setText("-");
        detailDueDateLabel.setText("-");
        detailStatusLabel.setText("-");
        detailItemsTable.setItems(FXCollections.observableArrayList());
        detailSubtotalLabel.setText(currencyFormat.format(0));
        detailTaxLabel.setText(currencyFormat.format(0));
        detailTotalLabel.setText(currencyFormat.format(0));
    }

    private String formatDate(LocalDate value) {
        return value == null ? "-" : dateFormatter.format(value);
    }
}
