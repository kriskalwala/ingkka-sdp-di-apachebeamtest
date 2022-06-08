package com.ingka.sbp.di.poslogparse.xml;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Tender implements Serializable {
    private String type;
    private String typeCode;
    private Double amount;
    private Double quantity;
    private String externalType;
    private String cardType;
    private String issuerIdentificationNumber;
    private String primaryAccountNumber;
}
