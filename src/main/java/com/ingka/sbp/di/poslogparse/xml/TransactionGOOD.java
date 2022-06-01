package com.ingka.sbp.di.poslogparse.xml;

import java.sql.Time;
import java.util.Date;

import org.joda.time.DateTime;

//import com.ikea.sbp.di.xml.RetailTransaction;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@Data
//@Builder

public class TransactionGOOD {
	
	/*Field name
	Type
	Mode
	Collation
	Policy Tags 
	Description
	
	BUS_DAY	DATE	NULLABLE		
	STO_NUM	INTEGER	NULLABLE		
	WS_ID	INTEGER	NULLABLE		
	TRA_SEQ_NO	NUMERIC	NULLABLE		
	TRA_STA_DTM	TIMESTAMP	NULLABLE		
	CANC_FLG	STRING	NULLABLE		
	OFLN_FLG	STRING	NULLABLE		
	ETL_INS_DTM	DATETIME	NULLABLE*/
	
	//26 MAY update:
		
/*	BUS_DAY	DATE	NULLABLE		
	STO_NUM	STRING	NULLABLE		
	WS_ID	NUMERIC	NULLABLE		
	TRA_SEQ_NO	NUMERIC	NULLABLE		
	TRA_STA_DTM	DATETIME	NULLABLE		
	TILL_TYPE	STRING	NULLABLE		
	CURCY_CODE	STRING	NULLABLE		
	TRA_STAT	STRING	NULLABLE		
	CANC_FLG	STRING	NULLABLE		
	OFLN_FLG	STRING	NULLABLE		
	ETL_INS_DTM	TIMESTAMP	NULLABLE	*/	
	
	LocalDate BUS_DAY;
	//Date BUS_DAY;
	String STO_NO;
	Integer WS_ID;
    Integer TRA_SEQ_NO;
    LocalDateTime TRA_STA_DTM;
    //DateTime TRA_STA_DTM;
    String TILL_TYPE;
    String CURCY_CODE;
    String TRA_STAT;
    String CANC_FLG;
    String OFLN_FLG;
    LocalDateTime ETL_INS_DTM;
    //DateTime ETL_INS_DTM;
    private RetailTransaction retailTransaction;
    
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
	public String getTILL_TYPE() {
		return TILL_TYPE;
	}
	public void setTILL_TYPE(String tILL_TYPE) {
		TILL_TYPE = tILL_TYPE;
	}
	public String getCURCY_CODE() {
		return CURCY_CODE;
	}
	public void setCURCY_CODE(String cURCY_CODE) {
		CURCY_CODE = cURCY_CODE;
	}
	public String getTRA_STAT() {
		return TRA_STAT;
	}
	public void setTRA_STAT(String tRA_STAT) {
		TRA_STAT = tRA_STAT;
	}
	public String getCANC_FLG() {
		return CANC_FLG;
	}
	public void setCANC_FLG(String cANC_FLG) {
		CANC_FLG = cANC_FLG;
	}
	public String getOFLN_FLG() {
		return OFLN_FLG;
	}
	public void setOFLN_FLG(String oFLN_FLG) {
		OFLN_FLG = oFLN_FLG;
	}
	public LocalDateTime getETL_INS_DTM() {
		return ETL_INS_DTM;
	}
	public void setETL_INS_DTM(LocalDateTime eTL_INS_DTM) {
		ETL_INS_DTM = eTL_INS_DTM;
	}
    
    
	
    

    
    
}
