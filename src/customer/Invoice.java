package customer;

import java.util.Date;
import java.util.List;

public class Invoice {
    private int invoiceNumber;
    private Date date;
    private Customer customer;
    private List<PurchaseItem> items;
    private double oldJewelryWeight;
    private double oldJewelryRate;

    // Constructor
    public Invoice(int invoiceNumber, Date date, Customer customer, List<PurchaseItem> items,
                   double oldJewelryWeight, double oldJewelryRate) {
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.customer = customer;
        this.items = items;
        this.oldJewelryWeight = oldJewelryWeight;
        this.oldJewelryRate = oldJewelryRate;
    }

    // Getters and Setters
    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public double getOldJewelryWeight() {
        return oldJewelryWeight;
    }

    public void setOldJewelryWeight(double oldJewelryWeight) {
        this.oldJewelryWeight = oldJewelryWeight;
    }

    public double getOldJewelryRate() {
        return oldJewelryRate;
    }

    public void setOldJewelryRate(double oldJewelryRate) {
        this.oldJewelryRate = oldJewelryRate;
    }

    // Calculate total purchase amount
    public double getTotalPurchaseAmount() {
        return items.stream().mapToDouble(PurchaseItem::getAmount).sum();
    }

    // Calculate return amount
    public double getReturnAmount() {
        return oldJewelryWeight * oldJewelryRate;
    }

    // Calculate final bill = (Total Amount + 3% GST) - Return Amount
    public double getFinalAmount() {
        double total = getTotalPurchaseAmount();
        double gst = total * 0.03;
        double returnAmount = getReturnAmount();
        return (total + gst) - returnAmount;
    }
}