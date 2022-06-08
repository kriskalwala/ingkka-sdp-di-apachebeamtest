package com.ingka.sbp.di.poslogparse.xml;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Tax implements Serializable {
    private String type;
    private Integer sequenceNumber;
    private Double taxableAmount;
    private String taxIncludedInTaxableAmountFlag;
    private Double amount;
    private Double percent;
}
