/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lk.corevision.fruitshop.model;

/**
 * Model class representing an order item.
 * Contains information about products in customer orders including fruit details, packing information, and pricing.
 * 
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class OrderItems {
 private String fruitId;   
 private String fruitName;   
 private String packingId;   
 private String mfd;   
 private String sellingPrice;   
 private String discountPrice;   
 private String qty;   

    /**
     * Gets the fruit ID.
     * @return the fruit ID
     */
    public String getFruitId() {
        return fruitId;
    }

    /**
     * Sets the fruit ID.
     * @param fruitId the fruit ID to set
     */
    public void setFruitId(String fruitId) {
        this.fruitId = fruitId;
    }

    /**
     * Gets the fruit name.
     * @return the fruit name
     */
    public String getFruitName() {
        return fruitName;
    }

    /**
     * Sets the fruit name.
     * @param fruitName the fruit name to set
     */
    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    /**
     * Gets the packing ID.
     * @return the packing ID
     */
    public String getPackingId() {
        return packingId;
    }

    /**
     * Sets the packing ID.
     * @param packingId the packing ID to set
     */
    public void setPackingId(String packingId) {
        this.packingId = packingId;
    }

    /**
     * Gets the manufacturing date.
     * @return the manufacturing date
     */
    public String getMfd() {
        return mfd;
    }

    /**
     * Sets the manufacturing date.
     * @param mfd the manufacturing date to set
     */
    public void setMfd(String mfd) {
        this.mfd = mfd;
    }

    /**
     * Gets the selling price.
     * @return the selling price
     */
    public String getSellingPrice() {
        return sellingPrice;
    }

    /**
     * Sets the selling price.
     * @param sellingPrice the selling price to set
     */
    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * Gets the discount price.
     * @return the discount price
     */
    public String getDiscountPrice() {
        return discountPrice;
    }

    /**
     * Sets the discount price.
     * @param discountPrice the discount price to set
     */
    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    /**
     * Gets the quantity.
     * @return the quantity
     */
    public String getQty() {
        return qty;
    }

    /**
     * Sets the quantity.
     * @param qty the quantity to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }
}
