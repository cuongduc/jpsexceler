/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jps.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author japanshop
 */
public class ExcelReader {
    
    
    public final Integer SHEET_POS = 0;
    
    public final String INPUT_EXCEL_FILE = "/files/Products.xlsx";

    private XSSFWorkbook workbook;
    
    private InputStream in;
    
    public ExcelReader() throws IOException {
        in = ExcelReader.class.getResourceAsStream(INPUT_EXCEL_FILE);
        
        this.workbook = new XSSFWorkbook(in);
    }
    
    public ExcelReader(File in) throws IOException, InvalidFormatException {
        this.workbook = new XSSFWorkbook(in);
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }
    
    public void glimpseFileContent() throws IOException {
        XSSFSheet sheet = workbook.getSheetAt(SHEET_POS);
        
        // Get interator to all the rows in current sheet
        Iterator<Row> rowIterator = sheet.iterator();
        
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            
            while(cellIterator.hasNext()) {
                
                Cell cell = cellIterator.next();
                
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK:
                        System.out.println("\t\t");
                        break;
                    case Cell.CELL_TYPE_STRING:
                        System.out.print(cell.getStringCellValue() + "\t\t");
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t\t");
                        break;
                }
            }
            System.out.println("");
        }
        
        in.close();
    }
}

