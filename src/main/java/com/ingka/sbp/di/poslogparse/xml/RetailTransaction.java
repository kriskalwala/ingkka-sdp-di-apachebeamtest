package com.ingka.sbp.di.poslogparse.xml;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class RetailTransaction implements Serializable {
    private List<LineItem> lineItemList;
}
