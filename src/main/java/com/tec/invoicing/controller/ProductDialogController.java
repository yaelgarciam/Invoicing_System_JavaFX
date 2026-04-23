package com.tec.invoicing.controller;

import com.tec.invoicing.model.Product;
import com.tec.invoicing.service.CatalogService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class ProductDialogController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField skuField;

    @FXML
    private TextField unitPriceField;

    private CatalogService catalogService;
    private Stage stage;
    private Product createdProduct;

    @FXML
    private void initialize() {
        unitPriceField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null
        ));
    }

    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Product getCreatedProduct() {
        return createdProduct;
    }

    @FXML
    private void handleSaveProduct() {
        if (nameField.getText().isBlank() || skuField.getText().isBlank() || unitPriceField.getText().isBlank()) {
            showError("Complete all product fields before saving.");
            return;
        }

        double unitPrice;
        try {
            unitPrice = Double.parseDouble(unitPriceField.getText());
        } catch (NumberFormatException exception) {
            showError("Enter a valid unit price.");
            return;
        }

        if (unitPrice <= 0) {
            showError("Unit price must be greater than zero.");
            return;
        }

        createdProduct = new Product(nameField.getText().trim(), skuField.getText().trim(), unitPrice);
        catalogService.addProduct(createdProduct);
        stage.close();
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Validation Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
