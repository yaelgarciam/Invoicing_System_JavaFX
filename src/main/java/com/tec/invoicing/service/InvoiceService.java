package com.tec.invoicing.service;

import com.tec.invoicing.model.CurrentInvoiceModel;
import com.tec.invoicing.model.Customer;
import com.tec.invoicing.model.Invoice;
import com.tec.invoicing.model.InvoiceItem;
import com.tec.invoicing.model.Product;
import java.util.List;
import javafx.collections.ObservableList;

public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceNumberGenerator invoiceNumberGenerator = new InvoiceNumberGenerator();

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public String nextInvoiceNumber() {
        return invoiceNumberGenerator.nextInvoiceNumber();
    }

    public void addOrIncrementItem(ObservableList<InvoiceItem> items, Product product, int quantity) {
        for (InvoiceItem item : items) {
            if (item.getSku().equals(product.getSku())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        items.add(new InvoiceItem(product, quantity));
    }

    public Invoice buildInvoice(CurrentInvoiceModel currentInvoiceModel, String status) {
        Customer customer = currentInvoiceModel.getSelectedCustomer();
        List<InvoiceItem> copiedItems = currentInvoiceModel.getItems().stream()
                .map(item -> new InvoiceItem(
                        item.getProductName(),
                        item.getSku(),
                        item.getUnitPrice(),
                        item.getQuantity()
                ))
                .toList();

        return new Invoice(
                currentInvoiceModel.getInvoiceNumber(),
                customer == null ? "" : customer.getName(),
                customer == null ? "" : customer.getTaxId(),
                customer == null ? "" : customer.getAddress(),
                currentInvoiceModel.getIssueDate(),
                currentInvoiceModel.getDueDate(),
                copiedItems,
                status,
                currentInvoiceModel.getSubtotal(),
                currentInvoiceModel.getTax(),
                currentInvoiceModel.getTotal()
        );
    }

    public void saveInvoice(Invoice invoice) {
        invoiceRepository.addInvoice(invoice);
    }
}
