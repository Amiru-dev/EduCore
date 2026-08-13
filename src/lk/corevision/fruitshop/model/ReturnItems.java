package lk.corevision.fruitshop.model;

/**
 * Model class representing return items. Contains information about items
 * returned by customers including return reasons, quantities, and refund
 * details.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class ReturnItems {

    private String fruitId;
    private String fruitName;
    private String packingId;
    private String mfd;
    private String sellingPrice;
    private String discountPrice;
    private String qty;

    /**
     * @return the fruitName
     */
    public String getFruitName() {
        return fruitName;
    }

    /**
     * @param fruitName the fruitName to set
     */
    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    /**
     * @return the packingId
     */
    public String getPackingId() {
        return packingId;
    }

    /**
     * @param packingId the packingId to set
     */
    public void setPackingId(String packingId) {
        this.packingId = packingId;
    }

    /**
     * @return the mfd
     */
    public String getMfd() {
        return mfd;
    }

    /**
     * @param mfd the mfd to set
     */
    public void setMfd(String mfd) {
        this.mfd = mfd;
    }

    /**
     * @return the sellingPrice
     */
    public String getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param sellingPrice the sellingPrice to set
     */
    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * @return the discountPrice
     */
    public String getDiscountPrice() {
        return discountPrice;
    }

    /**
     * @param discountPrice the discountPrice to set
     */
    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the fruitId
     */
    public String getFruitId() {
        return fruitId;
    }

    /**
     * @param fruitId the fruitId to set
     */
    public void setFruitId(String fruitId) {
        this.fruitId = fruitId;
    }
}
