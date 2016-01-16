package jps.jpsexceler;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import jps.model.ExcelReader;
import jps.model.KiotProduct;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FXMLController implements Initializable {
    
    private ExcelReader eReader;
    
    private XSSFWorkbook wb;
    
    // List of KiotProducts
    private ObservableList<KiotProduct> kiotProduct = FXCollections.observableArrayList();
    
    @FXML
    private BorderPane rootLayoutBorderPane;

    @FXML
    private Button importFileButton;
    
    private final TableView<KiotProduct> kiotProductTableView = new TableView<>();
    
    @FXML
    private void importFileButtonClickHandler(ActionEvent event) throws IOException {
        System.out.println("Reading Excel ");
        readExcel();
        attachKiotProductTableView();

    }
    
    private void attachKiotProductTableView() {
        this.rootLayoutBorderPane.setCenter(kiotProductTableView);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
    }    
    
    public void readExcel() throws IOException {
        eReader = new ExcelReader();
        wb = eReader.getWorkbook();

        initKiotProductTableView();
        
    }
    
    private void initKiotProductTableView() {
        initKiotProductTableViewColums();
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
        primeCostCol.setCellValueFactory(new PropertyValueFactory<KiotProduct, Double>("primeCost"));
        
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
            
            String lproductType = row.getCell(0).getStringCellValue();
            String lcategory = row.getCell(1).getStringCellValue();
            String lid = row.getCell(2).getStringCellValue();
            String lname = row.getCell(3).getStringCellValue();
            Double lprimeCost = row.getCell(4).getNumericCellValue();
            Double lsalePrice = row.getCell(5).getNumericCellValue();
            Double linventory = row.getCell(6).getNumericCellValue();
            Double lminInventory = row.getCell(7).getNumericCellValue();
            Double lmaxInventory = row.getCell(8).getNumericCellValue();
            String lunit = row.getCell(9).getStringCellValue();
            String lbasicUnit = row.getCell(10).getStringCellValue();
            Double lconversionRate = row.getCell(11).getNumericCellValue();
            String lproperties = row.getCell(12).getStringCellValue();
            String lrelatedProduct = row.getCell(13).getStringCellValue();
            String limage = row.getCell(14).getStringCellValue();
            Double limeiUsed = row.getCell(15).getNumericCellValue();
            Double lweight = row.getCell(16).getNumericCellValue();
            
            KiotProduct product = new KiotProduct(lproductType, lcategory, lid, lname, lprimeCost,
                                                  lsalePrice, linventory, lminInventory, lmaxInventory,
                                                  lunit, lbasicUnit, lconversionRate, lproperties,
                                                  lrelatedProduct, limage, limeiUsed, lweight);
            kiotProduct.add(product);

        }
        return kiotProduct;

    }
}
