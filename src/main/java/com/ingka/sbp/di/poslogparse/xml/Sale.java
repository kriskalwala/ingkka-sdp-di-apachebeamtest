package com.ingka.sbp.di.poslogparse.xml;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Sale implements Serializable {
    private String itemId;
    private Double quantity;
    private Double unitListPrice;
    private Double regularSalesUnitPrice;
    private Double actualSalesUnitPrice;
    private Double extendedAmount;
    private Double discountAmount;
    private Double extendedDiscountAmount;
    private String cancelledPrepayment;
    private String returnIDNumber;
    private String transactionType;
}
