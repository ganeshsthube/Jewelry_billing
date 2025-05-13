package customer;

public class PurchaseItem {
    private String productName;
    private String hsnCode;
    private int pieces;
    private double netWeight;
    private double rate;
    private double labourCost;
    private double amount;

    // Constructor
    public PurchaseItem(String productName, String hsnCode, int pieces, double netWeight, double rate, double labourCost) {
        this.productName = productName;
        this.hsnCode = hsnCode;
        this.pieces = pieces;
        this.netWeight = netWeight;
        this.rate = rate;
        this.labourCost = labourCost;
        this.amount = calculateAmount();
    }

    // Calculate the amount based on the formula
    public double calculateAmount() {
        return ((netWeight * rate) / 10.0) + (netWeight * labourCost);
    }

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public int getPieces() {
        return pieces;
    }

    public double getNetWeight() {
        return netWeight;
    }

    public double getRate() {
        return rate;
    }

    public double getLabourCost() {
        return labourCost;
    }

    public double getAmount() {
        return amount;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public void setNetWeight(double netWeight) {
        this.netWeight = netWeight;
        this.amount = calculateAmount();
    }

    public void setRate(double rate) {
        this.rate = rate;
        this.amount = calculateAmount();
    }

    public void setLabourCost(double labourCost) {
        this.labourCost = labourCost;
        this.amount = calculateAmount();
    }
}