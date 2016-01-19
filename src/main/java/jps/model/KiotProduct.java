/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jps.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author japanshop
 */
public class KiotProduct {
    
    private SimpleStringProperty productType;
    private SimpleStringProperty category;
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty primePrice; // Giá vốn
    private SimpleDoubleProperty salePrice; // Giá bán
    private SimpleDoubleProperty inventory; // Tồn kho
    private SimpleDoubleProperty minInventory; // Tồn nhỏ nhất
    private SimpleDoubleProperty maxInventory; // Tồn lớn nhất
    private SimpleStringProperty unit; // Đơn vị tính
    private SimpleStringProperty basicUnit; // Mã đơn vị tính cơ bản
    private SimpleDoubleProperty conversionRate; // Quy đổi
    private SimpleStringProperty properties; // Thuộc tính
    private SimpleStringProperty relatedProduct; // Mã hàng hóa liên quan
    private SimpleStringProperty image; // URL hình ảnh
    private SimpleDoubleProperty imeiUsed; // Sử dụng imei
    private SimpleDoubleProperty weight; // Trọng lượng

    /**
     * Constructor
     * 
     * @param productType
     * @param category
     * @param id
     * @param name
     * @param primeCost
     * @param salePrice
     * @param inventory
     * @param minInventory
     * @param maxInventory
     * @param unit
     * @param basicUnit
     * @param conversionRate
     * @param properties
     * @param relatedProduct
     * @param image
     * @param imeiUsed
     * @param weight 
     */
    public KiotProduct(String productType, String category, 
                       String id, String name, Double primeCost, 
                       Double salePrice, Double inventory, Double minInventory, 
                       Double maxInventory, String unit, String basicUnit, 
                       Double conversionRate, String properties, 
                       String relatedProduct, String image, 
                       Double imeiUsed, Double weight) {
        this.productType = new SimpleStringProperty(productType);
        this.category = new SimpleStringProperty(category);
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.primePrice = new SimpleDoubleProperty(primeCost);
        this.salePrice = new SimpleDoubleProperty(salePrice);
        this.inventory = new SimpleDoubleProperty(inventory);
        this.minInventory = new SimpleDoubleProperty(minInventory);
        this.maxInventory = new SimpleDoubleProperty(maxInventory);
        this.unit = new SimpleStringProperty(unit);
        this.basicUnit = new SimpleStringProperty(basicUnit);
        this.conversionRate = new SimpleDoubleProperty(conversionRate);
        this.properties = new SimpleStringProperty(properties);
        this.relatedProduct = new SimpleStringProperty(relatedProduct);
        this.image = new SimpleStringProperty(image);
        this.imeiUsed = new SimpleDoubleProperty(imeiUsed);
        this.weight = new SimpleDoubleProperty(weight);
    }

    public String getProductType() {
        return productType.get();
    }
    
    public String getCategory() {
        return category.get();
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public Double getPrimePrice() {
        return primePrice.get();
    }

    public Double getSalePrice() {
        return salePrice.get();
    }

    public Double getInventory() {
        return inventory.get();
    }

    public Double getMinInventory() {
        return minInventory.get();
    }

    public Double getMaxInventory() {
        return maxInventory.get();
    }

    public String getUnit() {
        return unit.get();
    }

    public String getBasicUnit() {
        return basicUnit.get();
    }

    public Double getConversionRate() {
        return conversionRate.get();
    }

    public String getProperties() {
        return properties.get();
    }

    public String getRelatedProduct() {
        return relatedProduct.get();
    }

    public String getImage() {
        return image.get();
    }

    public Double getImeiUsed() {
        return imeiUsed.get();
    }

    public Double getWeight() {
        return weight.get();
    }    
    
    
    
    @Override
    public String toString() {
        return this.id.get() + " | " + this.name.get() + " | " + this.salePrice.get();
    }
    
}
