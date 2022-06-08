package com.ingka.sbp.di.poslogparse.xml;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class LineItem implements Serializable {
    private Integer sequenceNumber;
    private String voidFlag;
    private String entryMethod;
    private Tender tender;
    private List<Sale> saleList;
    private List<Tax> taxList;
}
