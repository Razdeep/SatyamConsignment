
package satyamconsignment.ui.Input.BillEntry;


public class LR {
    private String billNo;
    private String lrNo;
    private String pm;
    public LR(String billNo,String lrNo,String pm)
    {
            this.billNo=billNo;
            this.lrNo=lrNo;
            this.pm=pm;
    }

    public String getBillNo() {
        return billNo;
    }

    public String getLrNo() {
        return lrNo;
    }

    public String getPm() {
        return pm;
    }
    
}
