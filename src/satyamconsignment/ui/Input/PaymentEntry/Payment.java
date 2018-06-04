
package satyamconsignment.ui.Input.PaymentEntry;




public class Payment {
    private String billNo,billAmount,billDate,buyerName,gr,rd,cd,bc,due,amountPaid,bank,ddNo,ddDate;

    public Payment(String billNo, String billAmount, String billDate, String buyerName, String gr, String rd, String cd, String bc, String due, String amountPaid, String bank, String ddNo, String ddDate) {
        this.billNo = billNo;
        this.billAmount = billAmount;
        this.billDate = billDate;
        this.buyerName = buyerName;
        this.gr = gr;
        this.rd = rd;
        this.cd = cd;
        this.bc = bc;
        this.due = due;
        this.amountPaid = amountPaid;
        this.bank = bank;
        this.ddNo = ddNo;
        this.ddDate = ddDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public String getBillDate() {
        return billDate;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getGr() {
        return gr;
    }

    public String getRd() {
        return rd;
    }

    public String getCd() {
        return cd;
    }

    public String getBc() {
        return bc;
    }

    public String getDue() {
        return due;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public String getDdNo() {
        return ddNo;
    }

    public String getDdDate() {
        return ddDate;
    }

    public String getBank() {
        return bank;
    }
    
    
}
