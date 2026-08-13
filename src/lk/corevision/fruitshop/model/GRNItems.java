package lk.corevision.fruitshop.model;

/**
 * Model class representing a Goods Received Note (GRN) item.
 * Contains information about items received from suppliers including quantity, unit price, and raw material details.
 * 
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class GRNItems {
     private String grnItemId;    // Unique ID for the GRN item
        private String grnId;        // GRN Item ID or Reference ID
        private String qty;          // Quantity of the item
        private String unitPrice;    // Unit price of the item
        private String rawmaterialName;

    /**
     * Gets the unique ID for this GRN item.
     * @return the GRN item ID
     */
    public String getGrnItemId() {
        return grnItemId;
    }

    /**
     * Sets the unique ID for this GRN item.
     * @param grnItemId the GRN item ID to set
     */
    public void setGrnItemId(String grnItemId) {
        this.grnItemId = grnItemId;
    }

    /**
     * Gets the GRN reference ID.
     * @return the GRN ID
     */
    public String getGrnId() {
        return grnId;
    }

    /**
     * Sets the GRN reference ID.
     * @param grnId the GRN ID to set
     */
    public void setGrnId(String grnId) {
        this.grnId = grnId;
    }

    /**
     * Gets the quantity of the item.
     * @return the quantity
     */
    public String getQty() {
        return qty;
    }

    /**
     * Sets the quantity of the item.
     * @param qty the quantity to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * Gets the unit price of the item.
     * @return the unit price
     */
    public String getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price of the item.
     * @param unitPrice the unit price to set
     */
    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Gets the raw material name.
     * @return the raw material name
     */
    public String getRawmaterialName() {
        return rawmaterialName;
    }

    /**
     * Sets the raw material name.
     * @param rawmaterialName the raw material name to set
     */
    public void setRawmaterialName(String rawmaterialName) {
        this.rawmaterialName = rawmaterialName;
    }

}