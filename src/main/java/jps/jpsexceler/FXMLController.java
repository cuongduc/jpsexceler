package jps.jpsexceler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    private TextField imageTextField;
    
    
//    @FXML
//    private Button createProductTable;
    
    private final TableView<KiotProduct> kiotProductTableView = new TableView<>();
    
    public FXMLController() {
        this.app = new MainApp();
    }
    
    @FXML
    private void exportBNCButtonClickedHandler(ActionEvent event) throws IOException, InvalidFormatException {
        FileChooser fc = new FileChooser();
        File file = fc.showSaveDialog(app.getStage());
        
        if (file == null)
            return;
        ExcelWriter ew = new ExcelWriter();
        ew.createSheet("default");
        ew.write(kiotProduct, file.getAbsolutePath());
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
        if (flag == true) {
            Alert info = new Alert(AlertType.INFORMATION);
            info.setTitle("JPS Exceler");
            info.setHeaderText("Đồng bộ dữ liệu");
            info.setContentText("Đồng bộ dữ liệu thành công");
        } else {
            Alert info = new Alert(AlertType.INFORMATION);
            info.setTitle("JPS Exceler");
            info.setHeaderText("Đồng bộ dữ liệu");
            info.setContentText("Đẫ có lỗi xảy ra trong quá trình đồng bộ dữ liệu. Xin hãy kiểm tra lại");
        }
    }
    
    @FXML
    private void importFileButtonClickHandler(ActionEvent event) throws IOException, InvalidFormatException {
        FileChooser fc = new FileChooser();
        Stage primaryStage = app.getStage();
        File in = fc.showOpenDialog(primaryStage);
        
        if(in == null)
            return;
        
        System.out.println("Reading Excel ");
        // Reading another file => clear the list, reinitialize
        clearProductObservableList();
        removeProductTableView();
        readExcel(in);
        attachKiotProductTableView();

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
                
    }    
    
    public void readExcel(File in) throws IOException, InvalidFormatException {
        eReader = new ExcelReader(in);
        wb = eReader.getWorkbook();
        
        initKiotProductTableView();
        
    }
    
    private void initKiotProductTableView() {
        if (!productContainerStackPane.getChildren().contains(kiotProductTableView)) {
            initKiotProductTableViewColums();
        } else {
            kiotProductTableView.getItems().setAll(populateTableViewRows());
        }
    }
    
    private void initKiotProductTableViewColums() {
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

        TableColumn imageCol = new TableColumn("Hình ảnh");
        imageCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("image"));
        
        TableColumn imeiUsedCol = new TableColumn("Sử dụng imei");
        imeiUsedCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("imeiUsed"));

        TableColumn weightCol = new TableColumn("Trọng lượng");
        weightCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, String>("weight"));

        kiotProductTableView.getColumns().addAll(productTypeCol, categoryCol, idCol,
                                                 nameCol, primeCostCol, salePriceCol,
                                                 inventoryCol, minInventoryCol, maxInventoryCol,
                                                 unitCol, basicUnitCol, conversionRateCol,
                                                 propertiesCol, relatedProductCol,imageCol,
                                                 imeiUsedCol, weightCol);
        kiotProductTableView.getItems().setAll(populateTableViewRows());
    }
    
    private ObservableList<KiotProduct> populateTableViewRows() {
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
//                competentPriceTextField.setText(p.getCo);
                inventoryTextField.setText(p.getInventory().toString());
                imageTextField.setText(p.getImage());
            }
        }
        
    }
}
