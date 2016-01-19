/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jps.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author japanshop
 */
public class ExcelWriter {
    
    private XSSFWorkbook workbook;
    
    private XSSFSheet sheet;
    
    private Map<Integer, Object> productData;
    
    public ExcelWriter() {
        this.workbook = new XSSFWorkbook();
        this.productData = new TreeMap<>();
    }
    
    public void createSheet(String title) {
        String sheetTitle = "Default";
        if(!title.equals(""))
            sheetTitle = title;
        sheet = workbook.createSheet(sheetTitle);
    }
    
    public void write(ObservableList<KiotProduct> p, String fName) {
        // Fill map
        fillMap(p, productData);
        fillRows(productData);
        // Write to file
        writeToFile(fName);
    }
    
    private void fillMap(ObservableList<KiotProduct> products, Map<Integer, Object> m) {
        int i = 2;
        m.put(1, new Object[] {"idsp", "Tên sản phẩm","Mã sản phẩm", "Giá sản phẩm","Giá thị trường", "Mô tả ngắn","Số lượng","Ảnh đại diện",	"Ảnh sản phẩm"});
        
        for (KiotProduct p : products) {
            i++;
            m.put(i, new Object[] {"", p.getName(), p.getId(), p.getSalePrice(), "", "", p.getInventory(), p.getImage(), ""});
        }
    }
    
    private void fillRows(Map<Integer, Object> m) {
        SortedSet<Integer> keys = new TreeSet<>(m.keySet());
        int rownum = 0;
        for (Integer key : keys) {
            System.out.println(key);
            Row row = sheet.createRow(rownum++);
            Object [] objArr = (Object[]) m.get(key);
            System.out.println(Arrays.toString(objArr));
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if(obj instanceof Date) 
                    cell.setCellValue((Date)obj);
                else if(obj instanceof Boolean)
                    cell.setCellValue((Boolean)obj);
                else if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Double)
                    cell.setCellValue((Double)obj);
            }
        }
    }
    
    private void writeToFile(String fn) {
        try {
            FileOutputStream out = 
                    new FileOutputStream(new File(fn));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
