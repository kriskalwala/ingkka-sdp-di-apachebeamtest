package com.ingka.sbp.di.poslogparse.xml;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/*import com.ikea.sbp.di.xml.Sale;
import com.ikea.sbp.di.xml.Tax;
import com.ikea.sbp.di.xml.Tender;*/

public class LineItemGOOD {
	
	LocalDate BUS_DAY;
	//Date BUS_DAY;
	String STO_NO;
	Integer WS_ID;
    Integer TRA_SEQ_NO;
    LocalDateTime TRA_STA_DTM;
    //DateTime TRA_STA_DTM;
    Integer TRA_LINE_SEQ_NO;
    Integer TRA_LINE_TAX_SEQ_NO;		
    String TAX_TYPE;		
    Integer TAXBL_AMT;		
    String TAXBL_AMT_INCL_VAT_FLG;		
    Integer TAX_AMT;		
    Integer TAX_RT;		
    LocalDateTime ETL_INS_DTM;
	public LocalDate getBUS_DAY() {
		return BUS_DAY;
	}
	public void setBUS_DAY(LocalDate bUS_DAY) {
		BUS_DAY = bUS_DAY;
	}
	public String getSTO_NO() {
		return STO_NO;
	}
	public void setSTO_NO(String sTO_NO) {
		STO_NO = sTO_NO;
	}
	public Integer getWS_ID() {
		return WS_ID;
	}
	public void setWS_ID(Integer wS_ID) {
		WS_ID = wS_ID;
	}
	public Integer getTRA_SEQ_NO() {
		return TRA_SEQ_NO;
	}
	public void setTRA_SEQ_NO(Integer tRA_SEQ_NO) {
		TRA_SEQ_NO = tRA_SEQ_NO;
	}
	public LocalDateTime getTRA_STA_DTM() {
		return TRA_STA_DTM;
	}
	public void setTRA_STA_DTM(LocalDateTime tRA_STA_DTM) {
		TRA_STA_DTM = tRA_STA_DTM;
	}
	public Integer getTRA_LINE_SEQ_NO() {
		return TRA_LINE_SEQ_NO;
	}
	public void setTRA_LINE_SEQ_NO(Integer tRA_LINE_SEQ_NO) {
		TRA_LINE_SEQ_NO = tRA_LINE_SEQ_NO;
	}
	public Integer getTRA_LINE_TAX_SEQ_NO() {
		return TRA_LINE_TAX_SEQ_NO;
	}
	public void setTRA_LINE_TAX_SEQ_NO(Integer tRA_LINE_TAX_SEQ_NO) {
		TRA_LINE_TAX_SEQ_NO = tRA_LINE_TAX_SEQ_NO;
	}
	public String getTAX_TYPE() {
		return TAX_TYPE;
	}
	public void setTAX_TYPE(String tAX_TYPE) {
		TAX_TYPE = tAX_TYPE;
	}
	public Integer getTAXBL_AMT() {
		return TAXBL_AMT;
	}
	public void setTAXBL_AMT(Integer tAXBL_AMT) {
		TAXBL_AMT = tAXBL_AMT;
	}
	public String getTAXBL_AMT_INCL_VAT_FLG() {
		return TAXBL_AMT_INCL_VAT_FLG;
	}
	public void setTAXBL_AMT_INCL_VAT_FLG(String tAXBL_AMT_INCL_VAT_FLG) {
		TAXBL_AMT_INCL_VAT_FLG = tAXBL_AMT_INCL_VAT_FLG;
	}
	public Integer getTAX_AMT() {
		return TAX_AMT;
	}
	public void setTAX_AMT(Integer tAX_AMT) {
		TAX_AMT = tAX_AMT;
	}
	public Integer getTAX_RT() {
		return TAX_RT;
	}
	public void setTAX_RT(Integer tAX_RT) {
		TAX_RT = tAX_RT;
	}
	public LocalDateTime getETL_INS_DTM() {
		return ETL_INS_DTM;
	}
	public void setETL_INS_DTM(LocalDateTime eTL_INS_DTM) {
		ETL_INS_DTM = eTL_INS_DTM;
	}	
    
    

}


/*private Integer sequenceNumber;
private String voidFlag;
private String entryMethod;
private Tender tender;*/
//private List<Sale> saleList;
//private List<Tax> taxList;


/*BUS_DAY	DATE	NULLABLE		
STO_NO	STRING	NULLABLE		
WS_ID	NUMERIC	NULLABLE		
TRA_SEQ_NO	NUMERIC	NULLABLE		
TRA_STA_DTM	DATETIME	NULLABLE

TRA_LINE_SEQ_NO	NUMERIC	NULLABLE		
TRA_LINE_TAX_SEQ_NO	NUMERIC	NULLABLE		
TAX_TYPE	STRING	NULLABLE		
TAXBL_AMT	NUMERIC	NULLABLE		
TAXBL_AMT_INCL_VAT_FLG	STRING	NULLABLE		
TAX_AMT	NUMERIC	NULLABLE		
TAX_RT	NUMERIC	NULLABLE		
ETL_INS_DTM	TIMESTAMP	NULLABLE*/	