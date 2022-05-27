package com.ingka.sbp.di.poslogparse.xml;

import java.sql.Time;
import java.util.Date;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@Data
//@Builder

public class TransactionINGKASTR {
	
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
	
	String BUS_DAY;
	String STO_NO;
	String WS_ID;
    String TRA_SEQ_NO;
    String TRA_STA_DTM;
    String CANC_FLG;
    String OFLN_FLG;
    String ETL_INS_DTM;
	public String getBUS_DAY() {
		return BUS_DAY;
	}
	public void setBUS_DAY(String bUS_DAY) {
		BUS_DAY = bUS_DAY;
	}
	public String getSTO_NO() {
		return STO_NO;
	}
	public void setSTO_NO(String sTO_NO) {
		STO_NO = sTO_NO;
	}
	public String getWS_ID() {
		return WS_ID;
	}
	public void setWS_ID(String wS_ID) {
		WS_ID = wS_ID;
	}
	public String getTRA_SEQ_NO() {
		return TRA_SEQ_NO;
	}
	public void setTRA_SEQ_NO(String tRA_SEQ_NO) {
		TRA_SEQ_NO = tRA_SEQ_NO;
	}
	public String getTRA_STA_DTM() {
		return TRA_STA_DTM;
	}
	public void setTRA_STA_DTM(String tRA_STA_DTM) {
		TRA_STA_DTM = tRA_STA_DTM;
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
	public String getETL_INS_DTM() {
		return ETL_INS_DTM;
	}
	public void setETL_INS_DTM(String eTL_INS_DTM) {
		ETL_INS_DTM = eTL_INS_DTM;
	}
    

    
    
}
