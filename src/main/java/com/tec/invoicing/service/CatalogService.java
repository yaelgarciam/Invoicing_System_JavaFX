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
                new Customer("Teus Net", "TNPL001", "88 Silicon Valley, San Jose, CA"),
                new Customer("Parlocos", "PLJJ002", "98 Silicon Valley, San Jose, CA"),
                new Customer("ChuvaBot", "CHBT003", "108 Silicon Valley, San Jose, CA"),
                new Customer("El Tigre", "ELTG004", "118 Silicon Valley, San Jose, CA"),
                new Customer("Mitica"  , "MITA005", "128 Silicon Valley, San Jose, CA")
        );

        products.addAll(
                new Product("MacBookAir M4", "SKU-1001", 19999.00),
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
