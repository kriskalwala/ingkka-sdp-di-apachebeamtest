package com.ingka.sbp.di.poslogparse.xml;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Tax implements Serializable {
    private String type;
    private String sequenceNumber;
    private String taxableAmount;
    private String taxIncludedInTaxableAmountFlag;
    private String amount;
    private String percent;
}
