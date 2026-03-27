package satyamconsignment.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CollectionItemEntity {
    private String billNo;
    private String billDate;
    private String billAmount;
    private String supplierName;
    private String due;
    private String amountCollected;
    private String bank;
    private String ddNo;
    private String ddDate;
}
