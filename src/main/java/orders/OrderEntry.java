package orders;

import javafx.beans.property.*;

public class OrderEntry {

    private IntegerProperty oderNo = new SimpleIntegerProperty();
    private StringProperty itemName = new SimpleStringProperty();
    private DoubleProperty unitPrice = new SimpleDoubleProperty();
    private IntegerProperty quantity = new SimpleIntegerProperty();
    private DoubleProperty totalAmt = new SimpleDoubleProperty();

    public OrderEntry() {
        setItemName("-");
    }

    public OrderEntry(int orderNo, String itemName, double unitPrice, int quantity, double totalAmt) {
        setOrderNo(orderNo);
        setItemName(itemName);
        setUnitPrice(unitPrice);
        setQuantity(quantity);
        setTotalAmt(totalAmt);
    }

    public IntegerProperty orderNoProperty() {
        return this.oderNo;
    }

    public int getOrderNo() {
        return this.orderNoProperty().get();
    }

    public void setOrderNo(int orderNo) {
        this.orderNoProperty().set(orderNo);
    }

    public StringProperty itemNameProperty() {
        return this.itemName;
    }

    public String getItemName() {
        return this.itemNameProperty().get();
    }

    public void setItemName(String itemName) {
        this.itemNameProperty().set(itemName);
    }

    public DoubleProperty unitPriceProperty() {
        return this.unitPrice;
    }

    public double getUnitPrice() {
        return this.unitPriceProperty().get();
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPriceProperty().set(unitPrice);
    }

    public IntegerProperty quantityProperty() {
        return this.quantity;
    }

    public int getQuantity() {
        return this.quantityProperty().get();
    }

    public void setQuantity(int quantity) {
        this.quantityProperty().set(quantity);
    }

    public DoubleProperty totalAmtProperty() {
        return this.totalAmt;
    }

    public double getTotalAmt() {
        return this.totalAmtProperty().get();
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmtProperty().set(totalAmt);
    }

    @Override
    public String toString() {
        return getOrderNo() + " " + getItemName() + " " + getUnitPrice() + " " + getQuantity() + " " + getTotalAmt();
    }
}
