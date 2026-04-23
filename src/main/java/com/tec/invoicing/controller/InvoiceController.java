package com.tec.invoicing.controller;

import com.tec.invoicing.MainApp;
import com.tec.invoicing.model.CurrentInvoiceModel;
import com.tec.invoicing.model.Customer;
import com.tec.invoicing.model.Invoice;
import com.tec.invoicing.model.InvoiceItem;
import com.tec.invoicing.model.Product;
import com.tec.invoicing.service.CatalogService;
import com.tec.invoicing.service.InvoiceService;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class InvoiceController {

    @FXML
    private TextField invoiceNumberField;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private TextField taxIdField;

    @FXML
    private TextField addressField;

    @FXML
    private DatePicker issueDatePicker;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private ComboBox<Product> productComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<InvoiceItem> lineItemsTable;

    @FXML
    private TableColumn<InvoiceItem, String> productColumn;

    @FXML
    private TableColumn<InvoiceItem, String> skuColumn;

    @FXML
    private TableColumn<InvoiceItem, Integer> quantityColumn;

    @FXML
    private TableColumn<InvoiceItem, Number> unitPriceColumn;

    @FXML
    private TableColumn<InvoiceItem, Number> subtotalColumn;

    @FXML
    private TableColumn<InvoiceItem, Void> actionsColumn;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private Label totalLabel;

    private final CurrentInvoiceModel invoiceModel = new CurrentInvoiceModel();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());

    private CatalogService catalogService;
    private InvoiceService invoiceService;

    @FXML
    private void initialize() {
        configureQuantityInputs();
        configureLineItemsTable();
    }

    public void configure(CatalogService catalogService, InvoiceService invoiceService) {
        this.catalogService = catalogService;
        this.invoiceService = invoiceService;

        customerComboBox.setItems(catalogService.getCustomers());
        productComboBox.setItems(catalogService.getProducts());
        bindForm();
        resetFormSilently();
    }

    private void configureQuantityInputs() {
        quantityField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null
        ));
    }

    private void configureLineItemsTable() {
        lineItemsTable.setEditable(true);
        productColumn.setCellValueFactory(data -> data.getValue().productNameProperty());
        skuColumn.setCellValueFactory(data -> data.getValue().skuProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        unitPriceColumn.setCellValueFactory(data -> data.getValue().unitPriceProperty());
        subtotalColumn.setCellValueFactory(data -> data.getValue().subtotalProperty());

        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(Integer value) {
                return value == null ? "" : value.toString();
            }

            @Override
            public Integer fromString(String value) {
                try {
                    return Integer.valueOf(value);
                } catch (NumberFormatException exception) {
                    return 0;
                }
            }
        }));

        quantityColumn.setOnEditCommit(event -> {
            int editedValue = event.getNewValue() == null ? 0 : event.getNewValue();
            if (editedValue <= 0) {
                event.getRowValue().setQuantity(event.getOldValue());
                lineItemsTable.refresh();
                showError("The quantity must be a positive integer.");
                return;
            }

            event.getRowValue().setQuantity(editedValue);
        });

        unitPriceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : currencyFormat.format(value.doubleValue()));
            }
        });

        subtotalColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : currencyFormat.format(value.doubleValue()));
            }
        });

        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.getStyleClass().add("danger-button");
                removeButton.setOnAction(event -> {
                    InvoiceItem selectedItem = getTableView().getItems().get(getIndex());
                    invoiceModel.getItems().remove(selectedItem);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }
        });
    }

    private void bindForm() {
        invoiceNumberField.textProperty().bind(invoiceModel.invoiceNumberProperty());
        customerComboBox.valueProperty().bindBidirectional(invoiceModel.selectedCustomerProperty());
        taxIdField.textProperty().bind(invoiceModel.customerTaxIdProperty());
        addressField.textProperty().bind(invoiceModel.customerAddressProperty());
        issueDatePicker.valueProperty().bindBidirectional(invoiceModel.issueDateProperty());
        dueDatePicker.valueProperty().bindBidirectional(invoiceModel.dueDateProperty());

        lineItemsTable.setItems(invoiceModel.getItems());

        subtotalLabel.textProperty().bind(Bindings.createStringBinding(
                () -> currencyFormat.format(invoiceModel.getSubtotal()),
                invoiceModel.subtotalProperty()
        ));
        taxLabel.textProperty().bind(Bindings.createStringBinding(
                () -> currencyFormat.format(invoiceModel.getTax()),
                invoiceModel.taxProperty()
        ));
        totalLabel.textProperty().bind(Bindings.createStringBinding(
                () -> currencyFormat.format(invoiceModel.getTotal()),
                invoiceModel.totalProperty()
        ));
    }

    @FXML
    private void handleAddItem() {
        Product selectedProduct = productComboBox.getValue();
        if (selectedProduct == null) {
            showError("Select a product before adding it to the invoice.");
            return;
        }

        if (quantityField.getText().isBlank()) {
            showError("Enter a quantity before adding the item.");
            return;
        }

        int quantity = Integer.parseInt(quantityField.getText());
        if (quantity <= 0) {
            showError("The quantity must be a positive integer.");
            return;
        }

        invoiceService.addOrIncrementItem(invoiceModel.getItems(), selectedProduct, quantity);
        productComboBox.getSelectionModel().clearSelection();
        quantityField.clear();
    }

    @FXML
    private void handleSaveDraft() {
        if (!validateInvoiceData()) {
            return;
        }

        Invoice invoice = invoiceService.buildInvoice(invoiceModel, "Pending");
        invoiceService.saveInvoice(invoice);
        showInformation("The invoice draft was saved in history with Pending status.");
    }

    @FXML
    private void handleGenerateInvoice() {
        if (!validateInvoiceData()) {
            return;
        }

        Invoice invoice = invoiceService.buildInvoice(invoiceModel, "Paid");
        invoiceService.saveInvoice(invoice);
        showInformation("The invoice was generated and saved with Paid status.");
        resetFormSilently();
    }

    @FXML
    private void handleResetForm() {
        Alert confirmationAlert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Do you want to clear the form?",
                ButtonType.YES,
                ButtonType.NO
        );
        confirmationAlert.setHeaderText("Reset Form");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            resetFormSilently();
        }
    }

    @FXML
    private void handleNewProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("product-dialog-view.fxml"));
            Parent root = loader.load();

            ProductDialogController controller = loader.getController();
            controller.setCatalogService(catalogService);

            Stage dialogStage = new Stage();
            dialogStage.initOwner(invoiceNumberField.getScene().getWindow());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("New Product");

            Scene dialogScene = new Scene(root);
            dialogScene.getStylesheets().add(MainApp.class.getResource("styles.css").toExternalForm());
            dialogStage.setScene(dialogScene);

            controller.setStage(dialogStage);
            dialogStage.showAndWait();

            if (controller.getCreatedProduct() != null) {
                productComboBox.setValue(controller.getCreatedProduct());
            }
        } catch (IOException exception) {
            showError("The new product window could not be opened.");
        }
    }

    private boolean validateInvoiceData() {
        if (invoiceModel.getSelectedCustomer() == null) {
            showError("Select a customer before saving or generating the invoice.");
            return false;
        }

        if (invoiceModel.getItems().isEmpty()) {
            showError("Add at least one item before saving or generating the invoice.");
            return false;
        }

        return true;
    }

    private void resetFormSilently() {
        invoiceModel.reset(invoiceService.nextInvoiceNumber());
        productComboBox.getSelectionModel().clearSelection();
        quantityField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Validation Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Invoice System");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
