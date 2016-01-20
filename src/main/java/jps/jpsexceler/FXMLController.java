package jps.jpsexceler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import jps.model.DBConnector;
import jps.model.ExcelReader;
import jps.model.ExcelWriter;
import jps.model.KiotProduct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FXMLController implements Initializable {
    
    private final MainApp app;
    
    private ExcelReader eReader;
    
    private XSSFWorkbook wb;
    
    // List of KiotProducts
    private ObservableList<KiotProduct> kiotProduct = FXCollections.observableArrayList();
    
    @FXML
    private BorderPane rootLayoutBorderPane;

    @FXML
    private Button importFileButton;
    
    @FXML
    private Button synchronizeDataButton;
    
    @FXML
    private Button exportBNCButton;
    
    @FXML
    private StackPane productContainerStackPane;
    
    // Detail panel controls
    @FXML
    private Button productPhotoButton;
    
    @FXML
    private Button saveChangeButton;
    
    @FXML
    private ImageView avatarImageView;
    
    @FXML
    private TextField idTextField;
    
    @FXML
    private TextField nameTextField;
    
    @FXML
    private TextField categoryTextField;
    
    @FXML
    private TextField salePriceTextField;
    
    @FXML
    private TextField primeSaleTextField;
    
    @FXML
    private TextField competentPriceTextField;
    
    @FXML
    private TextField inventoryTextField;
    
    @FXML
    private TextField avatarTextField;
    
    @FXML
    private TextField imageTextField;
    
    private DBConnector db;
    
    public FXMLController() {
        this.db = new DBConnector();
        this.app = new MainApp();
    }
//    @FXML
//    private Button createProductTable;
    
    private final TableView<KiotProduct> kiotProductTableView = new TableView<>();
    
    
    @FXML
    private void exportBNCButtonClickedHandler(ActionEvent event) throws IOException, InvalidFormatException, SQLException {
        FileChooser fc = new FileChooser();
        File file = fc.showSaveDialog(app.getStage());
        
        if (file == null)
            return;
        
        DBConnector db = new DBConnector();
        ObservableList<KiotProduct> productToExport = db.getAllProducts();
        ExcelWriter ew = new ExcelWriter();
        ew.createSheet("default");
        ew.write(productToExport, file.getAbsolutePath());
    }
//    @FXML
//    private void createProductTableClickedHandler(ActionEvent event) throws SQLException {
//        DBConnector conn = this.app.getDbConnector();
//        try {
//        Integer flag = conn.createProductsTable();
//        if (flag == Statement.EXECUTE_FAILED) {
//            Alert errorAlert = new Alert(AlertType.ERROR);
//            errorAlert.setTitle("JPS Exceler Error");
//            errorAlert.setHeaderText("Database error");
//            errorAlert.setContentText("Oops, there was error why trying to create Products table");
//            errorAlert.showAndWait();
//        } else {
//            Alert errorAlert = new Alert(AlertType.INFORMATION);
//            errorAlert.setTitle("JPS Exceler");
//            errorAlert.setHeaderText("Database Information");
//            errorAlert.setContentText("'Products' table was successfully created!");
//            errorAlert.showAndWait();
//        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            conn.close();
//        }
//    }
    
    @FXML
    private void synchronizeDataButtonClickedHandler(ActionEvent event) throws SQLException {
        System.out.println("Synchronize data");
        DBConnector db = MainApp.dbConnector;
        boolean flag = true;
        for (KiotProduct p : kiotProduct) {
            if (!db.synchronizeProduct(p))
                flag = false;
                
        }
//        if (flag == true) {
//            Alert info = new Alert(AlertType.INFORMATION);
//            info.setTitle("JPS Exceler");
//            info.setHeaderText("Đồng bộ dữ liệu");
//            info.setContentText("Đồng bộ dữ liệu thành công");
//            info.showAndWait();
//        } else {
//            Alert info = new Alert(AlertType.INFORMATION);
//            info.setTitle("JPS Exceler");
//            info.setHeaderText("Đồng bộ dữ liệu");
//            info.setContentText("Đẫ có lỗi xảy ra trong quá trình đồng bộ dữ liệu. Xin hãy kiểm tra lại");
//            info.showAndWait();
//
//        }
    }
    
    @FXML
    private void importFileButtonClickHandler(ActionEvent event) throws IOException, InvalidFormatException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Chọn file Excel");
        Stage primaryStage = app.getStage();
        File in = fc.showOpenDialog(primaryStage);
//        
        if(in == null)
            return;
        
        Alert importConfirm = new Alert(AlertType.CONFIRMATION);
        importConfirm.setTitle("JPS Exceler");
        importConfirm.setHeaderText("Đồng bộ dữ liệu");
        importConfirm.setContentText("Bạn có chắc chắn muốn đồng bộ dữ liệu từ file '" + in.getName()
                + "' không? Các sản phẩm trong cơ sở dữ liệu sẽ được cập nhật theo giá trị trong file");
        Optional<ButtonType> confirm = importConfirm.showAndWait();
        
        boolean synced = false;
        if(confirm.get() == ButtonType.OK) {
            readExcel(in);
            kiotProduct = loadProductsToKiotProductList();
            synced = synchronizeData(kiotProduct);
        } else {
            
        }
        
        if (synced) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("JPS Exceler");
            alert.setHeaderText("Đồng bộ dữ liệu");
            alert.setContentText("Đồng bộ dữ liệu thành công");
            alert.showAndWait();
            
            kiotProduct.clear();
            try {
                kiotProduct = db.getAllProducts();
                kiotProductTableView.getItems().clear();
                kiotProductTableView.setItems(kiotProduct);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("JPS Exceler");
            alert.setHeaderText("Đồng bộ dữ liệu");
            alert.setContentText("Đã có lỗi khi đồng bộ dữ liệu");
            alert.showAndWait();
        }
//        
//        System.out.println("Reading Excel ");
//        // Reading another file => clear the list, reinitialize
//        clearProductObservableList();
//        removeProductTableView();
//        readExcel(in);
//        attachKiotProductTableView();

    }
    
    private boolean synchronizeData(ObservableList<KiotProduct> list) {
        for (KiotProduct p: list) {
            try {
                boolean synced = db.synchronizeProduct(p);
                if (false != synced)
                    return false;
            } catch (SQLException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return true;
    }
    
    private void clearProductObservableList() {
        this.kiotProduct.clear();
    }
    
    private void removeProductTableView() {
        this.productContainerStackPane.getChildren().remove(kiotProductTableView);
    }
    
    /**
     * Attach kiotProductTableView to the product container
     */
    private void attachKiotProductTableView() {
        addTableViewListener();
        this.productContainerStackPane.getChildren().add(kiotProductTableView);
    }
    
    /**
     * Handle when user select on a row
     */
    private void addTableViewListener() {
        kiotProductTableView.getSelectionModel().selectedIndexProperty().addListener(new RowSelectedChangeListener());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productPhotoButton.setDisable(true);
        synchronizeDataButton.setDisable(true);
//        avatarImageView.maxWidth(200);
        imageTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                
                String first = newValue.substring(0, newValue.indexOf(','));
                avatarTextField.setText(first);
            }
        });
        
        avatarTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                
                if (!newValue.equals("")) {
                    Image img = new Image(newValue);
//                    avatarImageView.maxWidth(150);
//                    avatarImageView.setImage(img);
                }
            }
        });
        
        initTableViewFromDB();
    }    
    
    private void initTableViewFromDB() {
        try {
            kiotProduct = db.getAllProducts();
            kiotProductTableView.getItems().setAll(kiotProduct);
            this.productContainerStackPane.getChildren().add(kiotProductTableView);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TableColumn productTypeCol = new TableColumn("Hàng hóa");
        productTypeCol.setCellValueFactory(
            new PropertyValueFactory<KiotProduct, String>("productType")
        );
        
        TableColumn categoryCol = new TableColumn("Nhóm sản phẩm");
        categoryCol.setCellValueFactory(
            new PropertyValueFactory<KiotProduct, String>("category")
        );
        
        TableColumn idCol = new TableColumn("Mã sản phẩm");
        idCol.setCellValueFactory(
            new PropertyValueFactory<KiotProduct, String>("id")
        );
        
        TableColumn nameCol = new TableColumn("Tên sản phẩm");
        nameCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("name"));
        
        TableColumn primeCostCol = new TableColumn("Giá vốn");
        primeCostCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("primePrice"));
        
        TableColumn salePriceCol = new TableColumn("Giá bán");
        salePriceCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("salePrice"));
        
        TableColumn competentPriceCol = new TableColumn("Giá thị trường");
        competentPriceCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("competentPrice"));
        
        TableColumn inventoryCol = new TableColumn("Tồn");
        inventoryCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("inventory"));
        
        TableColumn minInventoryCol = new TableColumn("Tồn nhỏ nhất");
        minInventoryCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("minInventory"));
        
        TableColumn maxInventoryCol = new TableColumn("Tồn lớn nhất");
        maxInventoryCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("maxInventory"));
        
        TableColumn unitCol = new TableColumn("Đơn vị tính");
        unitCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("unit"));
        
        TableColumn basicUnitCol = new TableColumn("Mã đơn vị tính cơ bản");
        basicUnitCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("basicUnit"));
        
        TableColumn conversionRateCol = new TableColumn("Quy đổi");
        conversionRateCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("conversionRate"));
        
        TableColumn propertiesCol = new TableColumn("Thuộc tính");
        propertiesCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("properties"));
        
        TableColumn relatedProductCol = new TableColumn("Mã hàng hóa liên quan");
        relatedProductCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("relatedProduct"));
        
        TableColumn avatarCol = new TableColumn("Avatar");
        avatarCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("avatar"));
        
        TableColumn imageCol = new TableColumn("Hình ảnh");
        imageCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("image"));
        
        TableColumn imeiUsedCol = new TableColumn("Sử dụng imei");
        imeiUsedCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("imeiUsed"));

        TableColumn weightCol = new TableColumn("Trọng lượng");
        weightCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("weight"));

        kiotProductTableView.getColumns().addAll(productTypeCol, categoryCol, idCol,
                                                 nameCol, primeCostCol, salePriceCol, competentPriceCol,
                                                 inventoryCol, minInventoryCol, maxInventoryCol,
                                                 unitCol, basicUnitCol, conversionRateCol,
                                                 propertiesCol, relatedProductCol, avatarCol, imageCol,
                                                 imeiUsedCol, weightCol);
        addTableViewListener();
    }
    public void readExcel(File in) throws IOException, InvalidFormatException {
        eReader = new ExcelReader(in);
        wb = eReader.getWorkbook();
        
//        initKiotProductTableView();
        
    }
    
    private void initKiotProductTableView() {
        if (!productContainerStackPane.getChildren().contains(kiotProductTableView)) {
//            initKiotProductTableViewColums();
        } else {
            kiotProductTableView.getItems().setAll(loadProductsToKiotProductList());
        }
    }
    
//    private void initKiotProductTableViewColums() {
//        TableColumn productTypeCol = new TableColumn("Hàng hóa");
//        productTypeCol.setCellValueFactory(
//            new PropertyValueFactory<KiotProduct, String>("productType")
//        );
//        
//        TableColumn categoryCol = new TableColumn("Nhóm sản phẩm");
//        categoryCol.setCellValueFactory(
//            new PropertyValueFactory<KiotProduct, String>("category")
//        );
//        
//        TableColumn idCol = new TableColumn("Mã sản phẩm");
//        idCol.setCellValueFactory(
//            new PropertyValueFactory<KiotProduct, String>("id")
//        );
//        
//        TableColumn nameCol = new TableColumn("Tên sản phẩm");
//        nameCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("name"));
//        
//        TableColumn primeCostCol = new TableColumn("Giá vốn");
//        primeCostCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("primePrice"));
//        
//        TableColumn salePriceCol = new TableColumn("Giá bán");
//        salePriceCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("salePrice"));
//        
//        TableColumn inventoryCol = new TableColumn("Tồn");
//        inventoryCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("inventory"));
//        
//        TableColumn minInventoryCol = new TableColumn("Tồn nhỏ nhất");
//        minInventoryCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("minInventory"));
//        
//        TableColumn maxInventoryCol = new TableColumn("Tồn lớn nhất");
//        maxInventoryCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("maxInventory"));
//        
//        TableColumn unitCol = new TableColumn("Đơn vị tính");
//        unitCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("unit"));
//        
//        TableColumn basicUnitCol = new TableColumn("Mã đơn vị tính cơ bản");
//        basicUnitCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("basicUnit"));
//        
//        TableColumn conversionRateCol = new TableColumn("Quy đổi");
//        conversionRateCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("conversionRate"));
//        
//        TableColumn propertiesCol = new TableColumn("Thuộc tính");
//        propertiesCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("properties"));
//        
//        TableColumn relatedProductCol = new TableColumn("Mã hàng hóa liên quan");
//        relatedProductCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("relatedProduct"));
//
//        TableColumn imageCol = new TableColumn("Hình ảnh");
//        imageCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("image"));
//        
//        TableColumn imeiUsedCol = new TableColumn("Sử dụng imei");
//        imeiUsedCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("imeiUsed"));
//
//        TableColumn weightCol = new TableColumn("Trọng lượng");
//        weightCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("weight"));
//
//        kiotProductTableView.getColumns().addAll(productTypeCol, categoryCol, idCol,
//                                                 nameCol, primeCostCol, salePriceCol,
//                                                 inventoryCol, minInventoryCol, maxInventoryCol,
//                                                 unitCol, basicUnitCol, conversionRateCol,
//                                                 propertiesCol, relatedProductCol,imageCol,
//                                                 imeiUsedCol, weightCol);
//        kiotProductTableView.getItems().setAll(loadProductsToKiotProductList());
//    }
    
    private ObservableList<KiotProduct> loadProductsToKiotProductList() {
        XSSFSheet sheet = wb.getSheetAt(0);
        
        Iterator<Row> rowIterator = sheet.rowIterator();
        
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Ignore the header
            if (row.getRowNum() == 0)
                continue;
            
            String lproductType = 
                    row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
            String lcategory = 
                    row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
            String lid = 
                    row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
            String lname = 
                    row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "";
            Double lsalePrice = 
                    row.getCell(4) != null ? row.getCell(4).getNumericCellValue() : 0.0;
            Double lprimeCost = 
                    row.getCell(5) != null ? row.getCell(5).getNumericCellValue() : 0.0;
            Double linventory = 
                    row.getCell(6) != null ? row.getCell(6).getNumericCellValue() : 0.0;
            Double lminInventory = 
                    row.getCell(7) != null ? row.getCell(7).getNumericCellValue() : 0.0;
            Double lmaxInventory =
                    row.getCell(8) != null ? row.getCell(8).getNumericCellValue() : 0.0;
            String lunit = 
                    row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "";
            String lbasicUnit = 
                    row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "";
            Double lconversionRate = 
                    row.getCell(11) != null ? row.getCell(11).getNumericCellValue() : 0.0;
            String lproperties = 
                    row.getCell(12) != null ? row.getCell(12).getStringCellValue() : "";
            String lrelatedProduct = 
                    row.getCell(13) != null ? row.getCell(13).getStringCellValue() : "";
            String limage = 
                    row.getCell(14) != null ? row.getCell(14).getStringCellValue() : "";
            Double limeiUsed = 
                    row.getCell(15) != null ? row.getCell(15).getNumericCellValue() : 0;
            Double lweight = 
                    row.getCell(16) != null ? row.getCell(16).getNumericCellValue() : 0.0;
            
            if (!lid.equals("")) {
                KiotProduct product = new KiotProduct(lproductType, lcategory, lid, lname, lprimeCost,
                                                  lsalePrice, linventory, lminInventory, lmaxInventory,
                                                  lunit, lbasicUnit, lconversionRate, lproperties,
                                                  lrelatedProduct, limage, limeiUsed, lweight);
                kiotProduct.add(product);
            }

        }
        
        addTableViewListener();
        return kiotProduct;

    }
    
    private class RowSelectedChangeListener implements ChangeListener {
        
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            KiotProduct p = (KiotProduct)kiotProductTableView.getSelectionModel().getSelectedItem();
            if (p != null) {
                idTextField.setText(p.getId());
                nameTextField.setText(p.getName());
                categoryTextField.setText(p.getCategory());
                salePriceTextField.setText(p.getSalePrice().toString());
                primeSaleTextField.setText(p.getPrimePrice().toString());
                if ((p.getCompetentPrice() != null) || (!p.getCompetentPrice().equals("")))
                   competentPriceTextField.setText(p.getCompetentPrice());
                inventoryTextField.setText(p.getInventory().toString());
                if (p.getAvatar() != null && !p.getAvatar().equals(""))
                    avatarTextField.setText(p.getAvatar());
                else avatarTextField.setText("");
                if (p.getImage() != null && !p.getImage().equals(""))
                    imageTextField.setText(p.getImage());
                else imageTextField.setText("");
                try {
                    if (p.getAvatar() == null || p.getAvatar().equals("")) {
                        avatarImageView.setImage(null);
                    }
                    else {
                        javafx.scene.image.Image img = new javafx.scene.image.Image(p.getAvatar());
                        avatarImageView.setImage(img);
                    }
                  
                } catch (Exception ex) {
                    
                }
                
                // Enable image button
                productPhotoButton.setDisable(false);

            }
        }
        
    }
  
    /**
     * When use click on Image... button
     * shows the PhotoUploader view
     * 
     * @param event 
     */
    @FXML
    private void productPhotoButtonClickedHandler(ActionEvent event) {
        
        try {
            FXMLLoader loader = new FXMLLoader(FXMLController.class.getResource("/fxml/PhotoUploader.fxml"));
            BorderPane photoUploaderRoot = (BorderPane) loader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(photoUploaderRoot));
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setTitle("Hình ảnh sản phẩm");
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void saveChangeButtonClickedHandler(ActionEvent event) throws SQLException {
        KiotProduct p;
        p = this.kiotProductTableView.getSelectionModel().getSelectedItem();
        
        updateProduct(p);
        
        kiotProduct.clear();
        kiotProductTableView.getItems().clear();
        kiotProduct = db.getAllProducts();
        kiotProductTableView.setItems(kiotProduct);
    }
    
    private void updateProduct(KiotProduct p) throws SQLException {
        String id = this.idTextField.getText();
        String name = this.nameTextField.getText();
        String category = this.categoryTextField.getText();
        String salePrice = this.salePriceTextField.getText();
        String primePrice = this.primeSaleTextField.getText();
        String competentPrice = this.competentPriceTextField.getText();
        String image = this.imageTextField.getText();
        String avatar = this.avatarTextField.getText();
        
        Map<String, String> data = new HashMap<>();
        if (!id.equals("")) {
            data.put("id", id);
            p.setId(id);
        }
        if (!name.equals("")) {
            data.put("name", name);
            p.setName(name);
        }
        if (!category.equals("")) {
            data.put("category", category);
            p.setCategory(category);
        }
        if (!salePrice.equals("")) {
            data.put("salePrice", salePrice);
            p.setSalePrice(new Double(salePrice));
        }
        if (!primePrice.equals("")) {
            data.put("primePrice", primePrice);
            p.setPrimePrice(new Double(primePrice));
        }
        if (!competentPrice.equals("")) {
            data.put("competentPrice", competentPrice);
            p.setCompetentPrice(competentPrice);
        }
        if (!image.equals("")) {
            data.put("image", image);
            p.setImage(image);
        }
        if (!avatar.equals("")) {
            data.put("avatar", avatar);
            p.setAvatar(avatar);
        }
        
        DBConnector db = new DBConnector();
        int count = db.updateProduct(p, data);
        if (count != 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("JPS Exceler");
            alert.setHeaderText("Cập nhật sản phẩm");
            alert.setContentText("Thông tin sản phẩm đã được cập nhật vào cơ sở dữ liệu thành công");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("JPS Exceler");
            alert.setHeaderText("Cập nhật sản phẩm");
            alert.setContentText("Có lỗi xảy ra khi cập nhật thông tin sản phẩm vào cơ sở dữ liệu");
            alert.showAndWait();
        }
        System.out.println(data);
    }
    
    @FXML
    private void imageTextFieldActionHandler(ActionEvent event) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            this.imageTextField.setText(clipboard.getString());
        }
        System.out.println(clipboard.getString());
    }
}
