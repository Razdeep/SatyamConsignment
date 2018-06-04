package satyamconsignment.ui.Main.InputHistory;

public class Record {
    private String supplierName,buyerName,billNo,billDate,transport,lrDate,billAmount;

    public Record(String supplierName, String buyerName, String billNo, String billDate, String transport, String lrDate, String billAmount) {
        this.supplierName = supplierName;
        this.buyerName = buyerName;
        this.billNo = billNo;
        this.billDate = billDate;
        this.transport = transport;
        this.lrDate = lrDate;
        this.billAmount = billAmount;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBillNo() {
        return billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public String getTransport() {
        return transport;
    }

    public String getLrDate() {
        return lrDate;
    }

    public String getBillAmount() {
        return billAmount;
    }
    
}
