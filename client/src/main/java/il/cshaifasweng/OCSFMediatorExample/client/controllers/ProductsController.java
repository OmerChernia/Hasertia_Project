/*
 * Copyright 2020-2021 LaynezCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package il.cshaifasweng.OCSFMediatorExample.client.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertType;
import il.cshaifasweng.OCSFMediatorExample.client.alerts.AlertsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.animations.Animations;
import com.laynezcoder.client.mask.RequieredFieldsValidators;
import com.laynezcoder.client.mask.TextFieldMask;
import il.cshaifasweng.OCSFMediatorExample.client.models.Products;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationType;
import il.cshaifasweng.OCSFMediatorExample.client.notifications.NotificationsBuilder;
import il.cshaifasweng.OCSFMediatorExample.client.Constants;
import il.cshaifasweng.OCSFMediatorExample.client.util.ContextMenu;
import il.cshaifasweng.OCSFMediatorExample.client.util.JFXDialogTool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {

    private final ColorAdjust colorAdjust = new ColorAdjust();
    private final long LIMIT = 1000000;
    private final String ALREADY_EXISTS = "There is already a product with this barcode";
    private final String IS_GREATER = "Minimum price cannot be higher than sale price";

    private ObservableList<Products> listProducts;
    private ObservableList<Products> filterProducts;

    @FXML
    private StackPane stckProducts;

    @FXML
    private AnchorPane rootProducts;

    @FXML
    private AnchorPane containerDeleteProducts;

    @FXML
    private HBox hBoxSearch;

    @FXML
    private TextField txtSearchProduct;

    @FXML
    private TextField txtSearchBarCode;

    @FXML
    private JFXButton btnNewProduct;

    @FXML
    private TableView<Products> tblProducts;

    @FXML
    private TableColumn<Products, Integer> colId;

    @FXML
    private TableColumn<Products, Integer> colBarcode;

    @FXML
    private TableColumn<Products, String> colName;

    @FXML
    private TableColumn<Products, String> colDescription;

    @FXML
    private TableColumn<Products, Double> colPurchasePrice;

    @FXML
    private TableColumn<Products, Double> colSalePrice;

    @FXML
    private TableColumn<Products, Integer> colPorcentage;

    @FXML
    private TableColumn<Products, Double> colMinimalPrice;

    @FXML
    private AnchorPane containerAddProduct;

    @FXML
    private JFXTextField txtBarCode;

    @FXML
    private JFXTextField txtNameProduct;

    @FXML
    private JFXTextField txtPurchasePrice;

    @FXML
    private Text textAddProduct;

    @FXML
    private Text textPurchase;

    @FXML
    private Text textPorcentage;

    @FXML
    private JFXTextField txtSalePrice;

    @FXML
    private JFXTextArea txtDescriptionProduct;

    @FXML
    private JFXButton btnUpdateProduct;

    @FXML
    private JFXButton btnSaveProduct;

    @FXML
    private JFXButton btnCancelAddProduct;

    @FXML
    private JFXTextField txtPorcentage;

    @FXML
    private JFXTextField txtMinPrice;

    @FXML
    private ImageView imageProduct;

    @FXML
    private Pane paneContainer;

    @FXML
    private HBox imageContainer;

    private JFXDialogTool dialogAddProduct;

    private JFXDialogTool dialogDeleteProduct;

    private static final Stage stage = new Stage();

    private File imageFile;

    private ContextMenu contextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listProducts = FXCollections.observableArrayList();
        filterProducts = FXCollections.observableArrayList();
        loadData();
        setMask();
        animateNodes();
        selectText();
        setValidations();
        characterLimiter();
        initializeImage();
        setTextIfFieldIsEmpty();
        closeDialogWithTextFields();
        closeDialogWithEscapeKey();
    }

    private void setContextMenu() {
        contextMenu = new ContextMenu(tblProducts);

        contextMenu.setActionEdit(ev -> {
            showDialogEditProduct();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDeleteProduct();
            contextMenu.hide();
        });

        contextMenu.setActionDetails(ev -> {
            showDialogDetailsProduct();
            contextMenu.hide();
        });

        contextMenu.setActionRefresh(ev -> {
            loadData();
            contextMenu.hide();
        });

        contextMenu.show();
    }

    private void initializeImage() {
        imageContainer.hoverProperty().addListener((o, oldV, newV) -> {
            if (newV) {
                colorAdjust.setBrightness(0.25);
                imageProduct.setEffect(colorAdjust);
            } else {
                imageProduct.setEffect(null);
            }
        });

        imageContainer.setPadding(new Insets(5));
        imageProduct.setFitHeight(imageContainer.getPrefHeight() - 10);
        imageProduct.setFitWidth(imageContainer.getPrefWidth() - 10);
    }

    private void animateNodes() {
        Animations.fadeInUp(btnNewProduct);
        Animations.fadeInUp(tblProducts);
        Animations.fadeInUp(hBoxSearch);
    }

    private void setValidations() {
        RequieredFieldsValidators.toJFXTextField(txtSalePrice);
        RequieredFieldsValidators.toJFXTextField(txtMinPrice);
        RequieredFieldsValidators.toJFXTextField(txtPorcentage);
        RequieredFieldsValidators.toJFXTextField(txtBarCode);
        RequieredFieldsValidators.toJFXTextField(txtNameProduct);
        RequieredFieldsValidators.toJFXTextArea(txtDescriptionProduct);
        RequieredFieldsValidators.toJFXTextField(txtPurchasePrice);
    }

    private void setMask() {
        TextFieldMask.onlyDoubleNumbers10Integers(txtSalePrice);
        TextFieldMask.onlyDoubleNumbers5Integers(txtPurchasePrice);
        TextFieldMask.onlyDoubleNumbers5Integers(txtMinPrice);
        TextFieldMask.onlyNumbers(txtBarCode);
        TextFieldMask.onlyNumbers(txtSearchBarCode);
        TextFieldMask.onlyNumbers(txtPorcentage);
    }

    private void selectText() {
        TextFieldMask.selectText(txtNameProduct);
        TextFieldMask.selectText(txtSalePrice);
        TextFieldMask.selectText(txtMinPrice);
        TextFieldMask.selectText(txtBarCode);
        TextFieldMask.selectText(txtPorcentage);
        TextFieldMask.selectText(txtPurchasePrice);
        TextFieldMask.selectTextToJFXTextArea(txtDescriptionProduct);
    }

    private void setTextIfFieldIsEmpty() {
        TextFieldMask.setTextIfFieldIsEmpty(txtPurchasePrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtMinPrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtPorcentage);
        TextFieldMask.setTextIfFieldIsEmpty(txtSalePrice);
    }

    private void characterLimiter() {
        TextFieldMask.characterLimit(txtBarCode, 20);
        TextFieldMask.characterLimit(txtPorcentage, 3);
    }

    @FXML
    private void showDialogAddProduct() {
        resetValidation();
        calculateSalePrice();
        enableEditControls();
        disableTable();
        rootProducts.setEffect(Constants.BOX_BLUR_EFFECT);

        textAddProduct.setText("Add Product");
        imageContainer.toFront();
        containerAddProduct.setVisible(true);
        btnSaveProduct.setDisable(false);
        btnUpdateProduct.setVisible(true);
        btnSaveProduct.toFront();

        dialogAddProduct = new JFXDialogTool(containerAddProduct, stckProducts);
        dialogAddProduct.show();

        dialogAddProduct.setOnDialogOpened(ev -> {
            txtBarCode.requestFocus();
        });

        dialogAddProduct.setOnDialogClosed(ev -> {
            closeStage();
            tblProducts.setDisable(false);
            rootProducts.setEffect(null);
            containerAddProduct.setVisible(false);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogAddProduct() {
        if (dialogAddProduct != null) {
            dialogAddProduct.close();
        }
    }

    @FXML
    private void showDialogDeleteProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        rootProducts.setEffect(Constants.BOX_BLUR_EFFECT);
        containerDeleteProducts.setVisible(true);
        disableTable();

        dialogDeleteProduct = new JFXDialogTool(containerDeleteProducts, stckProducts);
        dialogDeleteProduct.show();

        dialogDeleteProduct.setOnDialogClosed(ev -> {
            tblProducts.setDisable(false);
            rootProducts.setEffect(null);
            containerDeleteProducts.setVisible(false);
            cleanControls();
        });

    }

    @FXML
    private void hideDialogDeleteProduct() {
        if (dialogDeleteProduct != null) {
            dialogDeleteProduct.close();
        }
    }

    @FXML
    private void showDialogEditProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddProduct();
        btnUpdateProduct.toFront();
        textAddProduct.setText("Update product");
        selectedRecord();

    }

    @FXML
    private void showDialogDetailsProduct() {
        if (tblProducts.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddProduct();
        textAddProduct.setText("Product details");
        selectedRecord();
        paneContainer.toFront();
        btnUpdateProduct.setVisible(false);
        btnSaveProduct.setDisable(true);
        btnSaveProduct.toFront();
        disableEditControls();
    }

    @FXML
    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionProduct"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colPorcentage.setCellValueFactory(new PropertyValueFactory<>("porcentage"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colMinimalPrice.setCellValueFactory(new PropertyValueFactory<>("minimalPrice"));
    }

    private void loadTable() {
        // Add some dummy data for testing
        listProducts.add(new Products(1, "123456", "Product 1", 10.0, 20, 12.0, 11.0, "Description 1"));
        listProducts.add(new Products(2, "789012", "Product 2", 15.0, 25, 18.75, 16.0, "Description 2"));
        listProducts.add(new Products(3, "345678", "Product 3", 20.0, 30, 26.0, 21.0, "Description 3"));
        listProducts.add(new Products(4, "901234", "Product 4", 25.0, 35, 33.75, 27.5, "Description 4"));

        tblProducts.setItems(listProducts);
        tblProducts.setFixedCellSize(30);
    }

    private void selectedRecord() {
        Products products = tblProducts.getSelectionModel().getSelectedItem();
        txtBarCode.setText(String.valueOf(products.getBarcode()));
        txtNameProduct.setText(products.getProductName());
        txtPurchasePrice.setText(String.valueOf(products.getPurchasePrice()));
        txtPorcentage.setText(String.valueOf(products.getPorcentage()));
        txtSalePrice.setText(String.valueOf(products.getSalePrice()));
        txtDescriptionProduct.setText(products.getDescriptionProduct());
        txtMinPrice.setText(String.valueOf(products.getMinimalPrice()));
        imageProduct.setImage(getImage(products.getId()));
        expandImage(products.getId(), products.getProductName());
    }

    @FXML
    private void newProduct() {
        String barcode = txtBarCode.getText().trim();
        String productName = txtNameProduct.getText().trim();
        String purchasePrice = txtPurchasePrice.getText().trim();
        String porcentage = txtPorcentage.getText().trim();
        String salePrice = txtSalePrice.getText().trim();
        String minPrice = txtMinPrice.getText().trim();
        String description = txtDescriptionProduct.getText().trim();

        if (barcode.isEmpty()) {
            txtBarCode.requestFocus();
            Animations.shake(txtBarCode);
            return;
        }

        for (Products p : listProducts) {
            if (p.getBarcode().equals(barcode)) {
                txtBarCode.requestFocus();
                NotificationsBuilder.create(NotificationType.ERROR, ALREADY_EXISTS);
                return;
            }
        }

        if (productName.isEmpty()) {
            txtNameProduct.requestFocus();
            Animations.shake(txtNameProduct);
            return;
        }

        if (purchasePrice.isEmpty()) {
            txtPurchasePrice.requestFocus();
            Animations.shake(txtPurchasePrice);
            return;
        }

        if (porcentage.isEmpty()) {
            txtPorcentage.requestFocus();
            Animations.shake(txtPorcentage);
            return;
        }

        if (salePrice.isEmpty()) {
            txtSalePrice.requestFocus();
            Animations.shake(txtSalePrice);
            return;
        }

        if (minPrice.isEmpty()) {
            txtMinPrice.requestFocus();
            Animations.shake(txtMinPrice);
            return;
        }

        if (Double.parseDouble(minPrice) > Double.parseDouble(salePrice)) {
            txtMinPrice.requestFocus();
            Animations.shake(txtMinPrice);
            NotificationsBuilder.create(NotificationType.ERROR, IS_GREATER);
            return;
        }

        if (description.isEmpty()) {
            txtDescriptionProduct.requestFocus();
            Animations.shake(txtDescriptionProduct);
            return;
        }

        if (imageFile != null && imageFile.length() > LIMIT) {
            Animations.shake(imageContainer);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_IMAGE_LARGE);
            return;
        }

        Products products = new Products();
        products.setBarcode(barcode);
        products.setProductName(productName);
        products.setDescriptionProduct(description);
        products.setPurchasePrice(Double.parseDouble(purchasePrice));
        products.setPorcentage(Integer.valueOf(porcentage));
        products.setSalePrice(Double.parseDouble(salePrice));
        products.setMinimalPrice(Double.parseDouble(minPrice));
        products.setProductImage(getInputStream());

        listProducts.add(products);
        loadData();
        cleanControls();
        closeDialogAddProduct();
        AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_ADDED);
    }

    private InputStream getInputStream() {
        InputStream is;
        try {
            if (imageFile != null) {
                is = new FileInputStream(imageFile);
            } else {
                is = ProductsController.class.getResourceAsStream(Constants.NO_IMAGE_AVAILABLE);
            }
        } catch (FileNotFoundException ex) {
            is = ProductsController.class.getResourceAsStream(Constants.NO_IMAGE_AVAILABLE);
        }
        return is;
    }

    private Image getImage(int id) {
        // Placeholder for image fetching logic
        return new Image(Constants.NO_IMAGE_AVAILABLE, 200, 200, true, true);
    }

    @FXML
    private void updateProduct() {
        String barcode = txtBarCode.getText().trim();
        String productName = txtNameProduct.getText().trim();
        String purchasePrice = txtPurchasePrice.getText().trim();
        String porcentage = txtPorcentage.getText().trim();
        String salePrice = txtSalePrice.getText().trim();
        String minPrice = txtMinPrice.getText().trim();
        String description = txtDescriptionProduct.getText().trim();
        Products selectedProduct = tblProducts.getSelectionModel().getSelectedItem();

        if (barcode.isEmpty()) {
            txtBarCode.requestFocus();
            Animations.shake(txtBarCode);
            return;
        }

        for (Products p : listProducts) {
            if (p.getBarcode().equals(barcode) && p != selectedProduct) {
                txtBarCode.requestFocus();
                Animations.shake(txtBarCode);
                NotificationsBuilder.create(NotificationType.ERROR, ALREADY_EXISTS);
                return;
            }
        }

        if (productName.isEmpty()) {
            txtNameProduct.requestFocus();
            Animations.shake(txtNameProduct);
            return;
        }

        if (purchasePrice.isEmpty()) {
            txtPurchasePrice.requestFocus();
            Animations.shake(txtPurchasePrice);
            return;
        }

        if (porcentage.isEmpty()) {
            txtPorcentage.requestFocus();
            Animations.shake(txtPorcentage);
            return;
        }

        if (salePrice.isEmpty()) {
            txtSalePrice.requestFocus();
            Animations.shake(txtSalePrice);
            return;
        }

        if (minPrice.isEmpty()) {
            txtMinPrice.requestFocus();
            Animations.shake(txtMinPrice);
            return;
        }

        if (Double.parseDouble(minPrice) > Double.parseDouble(salePrice)) {
            txtMinPrice.requestFocus();
            Animations.shake(txtMinPrice);
            NotificationsBuilder.create(NotificationType.ERROR, IS_GREATER);
            return;
        }

        if (description.isEmpty()) {
            txtDescriptionProduct.requestFocus();
            Animations.shake(txtDescriptionProduct);
            return;
        }

        if (imageFile != null && imageFile.length() > LIMIT) {
            Animations.shake(imageContainer);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_IMAGE_LARGE);
            return;
        }

        selectedProduct.setBarcode(barcode);
        selectedProduct.setProductName(productName);
        selectedProduct.setDescriptionProduct(description);
        selectedProduct.setPurchasePrice(Double.parseDouble(purchasePrice));
        selectedProduct.setPorcentage(Integer.valueOf(porcentage));
        selectedProduct.setSalePrice(Double.parseDouble(salePrice));
        selectedProduct.setMinimalPrice(Double.parseDouble(minPrice));
        selectedProduct.setProductImage(getInputStream());

        tblProducts.refresh();
        closeDialogAddProduct();
        loadData();
        cleanControls();
        AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_UPDATED);
    }

    @FXML
    private void deleteProducts() {
        Products selectedProduct = tblProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            listProducts.remove(selectedProduct);
            tblProducts.refresh();
            loadData();
            cleanControls();
            hideDialogDeleteProduct();
            AlertsBuilder.create(AlertType.SUCCES, stckProducts, rootProducts, tblProducts, Constants.MESSAGE_DELETED);
        }
    }

    private void cleanControls() {
        imageFile = null;
        txtPurchasePrice.clear();
        txtMinPrice.clear();
        txtBarCode.clear();
        txtNameProduct.clear();
        txtPorcentage.clear();
        txtSalePrice.clear();
        txtDescriptionProduct.clear();
        imageProduct.setImage(new Image(Constants.NO_IMAGE_AVAILABLE));
    }

    private void disableEditControls() {
        txtBarCode.setEditable(false);
        txtDescriptionProduct.setEditable(false);
        txtNameProduct.setEditable(false);
        txtPurchasePrice.setEditable(false);
        txtSalePrice.setEditable(false);
        txtPorcentage.setEditable(false);
        txtMinPrice.setEditable(false);
    }

    private void enableEditControls() {
        txtBarCode.setEditable(true);
        txtNameProduct.setEditable(true);
        txtSalePrice.setEditable(true);
        txtMinPrice.setEditable(true);
        txtPorcentage.setEditable(true);
        txtPurchasePrice.setEditable(true);
        txtDescriptionProduct.setEditable(true);
    }

    private void disableTable() {
        tblProducts.setDisable(true);
    }

    private void resetValidation() {
        txtBarCode.resetValidation();
        txtPorcentage.resetValidation();
        txtSalePrice.resetValidation();
        txtMinPrice.resetValidation();
        txtNameProduct.resetValidation();
        txtPurchasePrice.resetValidation();
        txtDescriptionProduct.resetValidation();
    }

    private void closeDialogWithEscapeKey() {
        rootProducts.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                hideDialogDeleteProduct();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                tblProducts.setDisable(false);
                rootProducts.setEffect(null);
                AlertsBuilder.close();
            }

        });
    }

    private void closeDialogWithTextFields() {
        txtBarCode.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtNameProduct.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtPurchasePrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtSalePrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtDescriptionProduct.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
            }
        });

        txtPorcentage.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
                tblProducts.setDisable(false);
            }
        });

        txtMinPrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddProduct();
                tblProducts.setDisable(false);
            }
        });
    }

    public static void closeStage() {
        if (stage != null) {
            stage.hide();
        }
    }

    @FXML
    private void filterNameProduct() {
        String filterName = txtSearchProduct.getText().trim();
        if (filterName.isEmpty()) {
            tblProducts.setItems(listProducts);
        } else {
            filterProducts.clear();
            for (Products p : listProducts) {
                if (p.getProductName().toLowerCase().contains(filterName.toLowerCase())) {
                    filterProducts.add(p);
                }
            }
            tblProducts.setItems(filterProducts);
        }
    }

    @FXML
    private void filterCodeBar() {
        String filterCodeBar = txtSearchBarCode.getText().trim();
        if (filterCodeBar.isEmpty()) {
            tblProducts.setItems(listProducts);
        } else {
            filterProducts.clear();
            for (Products p : listProducts) {
                if (p.getBarcode().toLowerCase().contains(filterCodeBar.toLowerCase())) {
                    filterProducts.add(p);
                }
            }
            tblProducts.setItems(filterProducts);
        }
    }

    private void calculateSalePrice() {
        txtPurchasePrice.setOnKeyReleased(ev -> {
            if (txtPurchasePrice.getText().isEmpty()) {
                txtPurchasePrice.setText("0");
            }

            if (txtPurchasePrice.isFocused() && txtPurchasePrice.getText().equals("0")) {
                txtPurchasePrice.selectAll();
            }

            if (txtPorcentage.getText().isEmpty()) {
                txtPorcentage.setText("0");
            }

            double purchasePrice = Double.valueOf(txtPurchasePrice.getText());
            int porcentage = Integer.parseInt(txtPorcentage.getText());
            double salePrice = ((purchasePrice * porcentage) / 100) + purchasePrice;
            txtSalePrice.setText(String.valueOf(salePrice));
        });

        txtPorcentage.setOnKeyReleased(ev -> {
            if (txtPorcentage.isFocused() && txtPorcentage.getText().isEmpty()) {
                txtPorcentage.setText("0");
            }

            if (txtPorcentage.isFocused() && txtPorcentage.getText().equals("0")) {
                txtPorcentage.selectAll();
            }

            if (txtPurchasePrice.getText().isEmpty()) {
                txtPurchasePrice.setText("0");
            }

            double purchasePrice = Double.valueOf(txtPurchasePrice.getText());
            int porcentage = Integer.parseInt(txtPorcentage.getText());
            double salePrice = ((purchasePrice * porcentage) / 100) + purchasePrice;
            txtSalePrice.setText(String.valueOf(salePrice));
        });
    }

    @FXML
    private void showFileChooser() {
        imageFile = getImageFromFileChooser(getStage());
        if (imageFile != null) {
            Image image = new Image(imageFile.toURI().toString(), 200, 200, true, true);
            imageProduct.setImage(image);
        }
    }

    private Stage getStage() {
        return (Stage) btnCancelAddProduct.getScene().getWindow();
    }

    private File getImageFromFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterImages = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().addAll(extFilterImages);
        fileChooser.setTitle("Select an image");

        return fileChooser.showOpenDialog(stage);
    }

    private void expandImage(int id, String title) {
        paneContainer.hoverProperty().addListener((o, oldV, newV) -> {
            if (newV) {
                colorAdjust.setBrightness(0.25);
                imageProduct.setEffect(colorAdjust);
            } else {
                imageProduct.setEffect(null);
            }
        });

        paneContainer.setOnMouseClicked(ev -> {
            final ImageView imageView = new ImageView("image");
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(550);

            final BorderPane boderPane = new BorderPane(imageView);
            boderPane.setStyle("-fx-background-color: white");
            boderPane.setCenter(imageView);

            final ScrollPane root = new ScrollPane(boderPane);
            root.setStyle("-fx-background-color: white");
            root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            root.getStylesheets().add(Constants.LIGHT_THEME);
            root.getStyleClass().add("scroll-bar");

            root.setFitToHeight(true);
            root.setFitToWidth(true);

            stage.getIcons().add(new Image(Constants.STAGE_ICON));
            stage.setScene(new Scene(root, 550, 555));
            stage.setTitle(title);
            stage.show();
        });
    }
}
