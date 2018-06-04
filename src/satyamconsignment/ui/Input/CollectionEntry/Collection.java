/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satyamconsignment.ui.Input.CollectionEntry;


public class Collection {
    private String billNo,billDate,billAmount,supplierName,gr,rd,cd,bc,due,amountCollected,ddNo,ddDate,bank;
    public Collection(String billNo,String billDate,String billAmount,String supplierName,
                        String gr,String rd,String cd,String bc, String due, String amountCollected,
                        String bank,String ddNo,String ddDate)
    {
        this.billNo=billNo;
        this.billDate=billDate;
        this.billAmount=billAmount;
        this.supplierName=supplierName;
        this.gr=gr;
        this.rd=rd;
        this.cd=cd;
        this.bc=bc;
        this.due=due;
        this.amountCollected=amountCollected;
        this.bank=bank;
        this.ddNo=ddNo;
        this.ddDate=ddDate;
        
    }
    public String getBillNo() {
        return billNo;
    }

    

    public String getSupplierName() {
        return supplierName;
    }

    public String getBillAmount() {
        return billAmount;
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

    public String getAmountCollected() {
        return amountCollected;
    }

    public String getDdNo() {
        return ddNo;
    }

    public String getBank() {
        return bank;
    }

    public String getDdDate() {
        return ddDate;
    }

    public String getBillDate() {
        return billDate;
    }

    public String getDue() {
        return due;
    }
    
    
}
