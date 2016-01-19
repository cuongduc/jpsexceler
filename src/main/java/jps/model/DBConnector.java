/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jps.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author japanshop
 */
public class DBConnector {
    
    private final String DRIVER_NAME = "org.sqlite.JDBC";
    
    private final String DATABASE_NAME = "jdbc:sqlite:jps.db";
    
    private Connection connection; 
    
    private Statement statement;
    
    public DBConnector() {
        connection = null;
        establishConnection();
//        statement = connection.createStatement();
    }
    
    /**
     * Try to connect to database
     */
    private void establishConnection() {
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(DATABASE_NAME);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int createProductsTable() throws SQLException {
        // TODO: check if table exist;
        statement = connection.createStatement();
        String sql = "CREATE TABLE products (" +
                     "productType VARCHAR(50) NOT NULL," +
                     "category VARCHAR(100) NOT NULL," +
                     "id VARCHAR(50) NOT NULL PRIMARY KEY," +
                     "name VARCHAR(100) NOT NULL," + 
                     "primePrice FLOAT, " +
                     "salePrice FLOAT, " +
                     "competentPrice FLOAT," +
                     "inventory INT, " +
                     "minInventory INT, " +
                     "maxInventory INT, " + 
                     "unit VARCHAR(20), " +
                     "basicUnit VARCHAR(20), " +
                     "conversionRate DECIMAL(6,1), " +
                     "properties VARCHAR(100), " +
                     "relatedProduct VARCHAR(50), " +
                     "image VARCHAR(255), " +
                     "avatar VARCHAR(255), " +
                     "imeiUsed BOOLEAN, " +
                     "weight DECIMAL(6,2), " +
                     "description TEXT" + 
                     ")";
        return statement.executeUpdate(sql);
    }
    
    public void close() throws SQLException {
        connection.close();
    }
    
    public Connection getConnection() {
        return this.connection;
    }
    
    public boolean synchronizeProduct(KiotProduct p) throws SQLException, SQLException {
        // If the product exists, update it
        if (isProductExisted(p).next())
            return updateProduct(p);
        else // otherwise, insert new product to db
            return insertProduct(p);
           
    }
    
    /**
     * Check if product exists by id
     * 
     * @param p
     * @return
     * @throws SQLException 
     */
    public ResultSet isProductExisted(KiotProduct p) throws SQLException {
        statement = connection.createStatement();

        String sql = "SELECT * FROM products WHERE id = '" + p.getId() + "' AND id != ''";
        System.out.println(sql);
        return statement.executeQuery(sql);
    }
    
    /**
     * Insert new product to database
     * 
     * @param p
     * @return
     * @throws SQLException 
     */
    public boolean insertProduct(KiotProduct p) throws SQLException {
        statement = connection.createStatement();

        String sql = "INSERT INTO products(productType, category, id, name, primePrice," +
                     "salePrice, inventory, minInventory, maxInventory, unit, basicUnit, " +
                     "conversionRate, properties, relatedProduct, image, imeiUsed, weight) " +
                     "VALUES('" + p.getProductType() + "','" + 
                                 p.getCategory() + "','" +
                                 p.getId() + "','" +
                                 p.getName() + "'," +
                                 p.getPrimePrice() + "," +
                                 p.getSalePrice() + "," +
                                 p.getInventory() + "," +
                                 p.getMinInventory() + "," +
                                 p.getMaxInventory() + ",'" +
                                 p.getUnit() + "','" +
                                 p.getBasicUnit() + "'," +
                                 p.getConversionRate() + ",'" +
                                 p.getProperties() + "','" +
                                 p.getRelatedProduct() + "','" +
                                 p.getImage() + "','" +
                                 p.getImeiUsed() + "'," +
                                 p.getWeight() +
                      ")";
        System.out.println(sql);
//        Statement stmt = connection.prepareStatement(sql);
        return statement.execute(sql);
    }
    
    /**
     * Update existing product
     * 
     * @param p
     * @return
     * @throws SQLException 
     */
    public boolean updateProduct(KiotProduct p) throws SQLException {
        statement = connection.createStatement();

        String sql = "UPDATE products "
                   + "SET productType = '" + p.getProductType() + "',"
                       + "category = '" + p.getCategory() + "'," 
//                       + "id = " + p.getId() + ","
                       + "name = '" + p.getName() + "'," 
                       + "primePrice = " + p.getPrimePrice() + "," 
                       + "salePrice = " + p.getSalePrice() + ","
                       + "inventory = " + p.getInventory() + "," 
                       + "minInventory = " + p.getMinInventory() + "," 
                       + "maxInventory = " + p.getMaxInventory() + "," 
                       + "unit = '" + p.getUnit() + "',"
                       + "basicUnit = '" + p.getBasicUnit() + "',"
                       + "conversionRate = " + p.getConversionRate() + ","
                       + "properties = '" + p.getProperties() + "',"
                       + "relatedProduct = '" + p.getRelatedProduct() + "',"
                       + "image = '" + p.getImage() + "',"
                       + "imeiUsed = " + p.getImeiUsed() + ","
                       + "weight = " + p.getWeight() 
                       + " WHERE id = '" + p.getId() + "';";
        System.out.println(sql);
//        Statement stmt = connection.prepareStatement(sql);
        return statement.execute(sql);
    }
}
