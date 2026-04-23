package com.tec.invoicing.service;

import com.tec.invoicing.model.Customer;
import com.tec.invoicing.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CatalogService {

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    public CatalogService() {
        customers.addAll(
                new Customer("Alpha Technologies", "ATX010203AB1", "1450 Innovation Ave, Austin, TX"),
                new Customer("Blue Orbit Systems", "BOS040506CD2", "88 Silicon Road, San Jose, CA"),
                new Customer("Nova Digital Labs", "NDL070809EF3", "2400 Spectrum Blvd, Irvine, CA"),
                new Customer("Quantum Softworks", "QSW101112GH4", "315 Data Park, Seattle, WA"),
                new Customer("Vertex Cloud Solutions", "VCS131415IJ5", "920 Network Street, Miami, FL")
        );

        products.addAll(
                new Product("Laptop Pro 14", "SKU-1001", 18999.00),
                new Product("Mechanical Keyboard", "SKU-1002", 1499.00),
                new Product("Wireless Mouse", "SKU-1003", 799.00),
                new Product("27\" Monitor", "SKU-1004", 5299.00),
                new Product("USB-C Dock", "SKU-1005", 2199.00),
                new Product("Noise Cancelling Headset", "SKU-1006", 1890.00),
                new Product("External SSD 1TB", "SKU-1007", 2499.00),
                new Product("Office Chair", "SKU-1008", 4399.00)
        );
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }
}
