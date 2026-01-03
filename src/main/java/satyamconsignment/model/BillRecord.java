package satyamconsignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BillRecord {
    private String supplierName;
    private String buyerName;
    private String billNo;
    private String billDate;
    private String transport;
    private String lrDate;
    private String billAmount;
}
