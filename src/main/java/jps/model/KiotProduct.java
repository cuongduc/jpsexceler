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
    
    private SimpleStringProperty competentPrice;
    private SimpleStringProperty avatar;
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
        this.competentPrice = new SimpleStringProperty("");
        this.avatar = new SimpleStringProperty("");
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

    public String getCompetentPrice() {
        return competentPrice.get();
    }

    public void setCompetentPrice(String competentPrice) {
        this.competentPrice.set(competentPrice);
    }

    public String getAvatar() {
        return avatar.get();
    }

    public void setAvatar(String avatar) {
        this.avatar.set(avatar);
    }

    public void setProductType(String productType) {
        this.productType.set(productType);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPrimePrice(Double primePrice) {
        this.primePrice.set(primePrice);
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice.set(salePrice);
    }

    public void setInventory(Double inventory) {
        this.inventory.set(inventory);
    }

    public void setMinInventory(Double minInventory) {
        this.minInventory.set(minInventory);
    }

    public void setMaxInventory(Double maxInventory) {
        this.maxInventory.set(maxInventory);
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public void setBasicUnit(String basicUnit) {
        this.basicUnit.set(basicUnit);
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate.set(conversionRate);
    }

    public void setProperties(String properties) {
        this.properties.set(properties);
    }

    public void setRelatedProduct(String relatedProduct) {
        this.relatedProduct.set(relatedProduct);
    }

    public void setImage(String image) {
        this.image.set(image);
    }

    public void setImeiUsed(Double imeiUsed) {
        this.imeiUsed.set(imeiUsed);
    }

    public void setWeight(Double weight) {
        this.weight.set(weight);
    }

    
    
    
    
    @Override
    public String toString() {
        return this.id.get() + " | " + this.name.get() + " | " + this.salePrice.get() + " | " + this.getCompetentPrice();
    }
    
}
