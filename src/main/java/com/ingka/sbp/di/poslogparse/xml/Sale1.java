package com.ingka.sbp.di.poslogparse.xml;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Sale1 implements Serializable {
    private String itemId;
    private String quantity;
    private String unitListPrice;
    private String regularSalesUnitPrice;
    private String actualSalesUnitPrice;
    private String extendedAmount;
    private String discountAmount;
    private String extendedDiscountAmount;
    private String cancelledPrepayment;
    private String returnIDNumber;
}
