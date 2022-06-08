package com.ingka.sbp.di.poslogparse.xml;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class LineItemGOOD {
   
    LocalDate BUS_DAY;
    String STO_NO;
    Integer WS_ID;
    Integer TRA_SEQ_NO;
    LocalDateTime TRA_STA_DTM;
    Integer TRA_LINE_SEQ_NO;
    String TRA_TYPE;
    String ITEM_NO;
    Integer UNIT_ITEM_PRIC;
    String REG_ITEM_PRIC;
    Integer ACT_ITEM_PRIC;
    Integer SALE_VAL;
    Integer DISC_AMT;
    Integer TOT_DISC_VAL;
    Integer ITEM_QTY;
    String CANC_PREPAY_FLG;
    String RETURN_NO;
    String VOID_FLG;
    LocalDateTime GCP_LANDING_DTM;

   public String getSTO_NO() {
      return STO_NO;
   }

   public void setSTO_NO(String STO_NO) {
      this.STO_NO = STO_NO;
   }

   public Integer getWS_ID() {
      return WS_ID;
   }

   public void setWS_ID(Integer WS_ID) {
      this.WS_ID = WS_ID;
   }

   public Integer getTRA_SEQ_NO() {
      return TRA_SEQ_NO;
   }

   public void setTRA_SEQ_NO(Integer TRA_SEQ_NO) {
      this.TRA_SEQ_NO = TRA_SEQ_NO;
   }

   public LocalDateTime getTRA_STA_DTM() {
      return TRA_STA_DTM;
   }

   public void setTRA_STA_DTM(LocalDateTime TRA_STA_DTM) {
      this.TRA_STA_DTM = TRA_STA_DTM;
   }

   public Integer getTRA_LINE_SEQ_NO() {
      return TRA_LINE_SEQ_NO;
   }

   public void setTRA_LINE_SEQ_NO(Integer TRA_LINE_SEQ_NO) {
      this.TRA_LINE_SEQ_NO = TRA_LINE_SEQ_NO;
   }

   public String getTRA_TYPE() {
      return TRA_TYPE;
   }

   public void setTRA_TYPE(String TRA_TYPE) {
      this.TRA_TYPE = TRA_TYPE;
   }

   public String getITEM_NO() {
      return ITEM_NO;
   }

   public void setITEM_NO(String ITEM_NO) {
      this.ITEM_NO = ITEM_NO;
   }

   public Integer getUNIT_ITEM_PRIC() {
      return UNIT_ITEM_PRIC;
   }

   public void setUNIT_ITEM_PRIC(Integer UNIT_ITEM_PRIC) {
      this.UNIT_ITEM_PRIC = UNIT_ITEM_PRIC;
   }

   public String getREG_ITEM_PRIC() {
      return REG_ITEM_PRIC;
   }

   public void setREG_ITEM_PRIC(String REG_ITEM_PRIC) {
      this.REG_ITEM_PRIC = REG_ITEM_PRIC;
   }

   public Integer getACT_ITEM_PRIC() {
      return ACT_ITEM_PRIC;
   }

   public void setACT_ITEM_PRIC(Integer ACT_ITEM_PRIC) {
      this.ACT_ITEM_PRIC = ACT_ITEM_PRIC;
   }

   public Integer getSALE_VAL() {
      return SALE_VAL;
   }

   public void setSALE_VAL(Integer SALE_VAL) {
      this.SALE_VAL = SALE_VAL;
   }

   public Integer getDISC_AMT() {
      return DISC_AMT;
   }

   public void setDISC_AMT(Integer DISC_AMT) {
      this.DISC_AMT = DISC_AMT;
   }

   public Integer getTOT_DISC_VAL() {
      return TOT_DISC_VAL;
   }

   public void setTOT_DISC_VAL(Integer TOT_DISC_VAL) {
      this.TOT_DISC_VAL = TOT_DISC_VAL;
   }

   public Integer getITEM_QTY() {
      return ITEM_QTY;
   }

   public void setITEM_QTY(Integer ITEM_QTY) {
      this.ITEM_QTY = ITEM_QTY;
   }

   public String getCANC_PREPAY_FLG() {
      return CANC_PREPAY_FLG;
   }

   public void setCANC_PREPAY_FLG(String CANC_PREPAY_FLG) {
      this.CANC_PREPAY_FLG = CANC_PREPAY_FLG;
   }

   public String getRETURN_NO() {
      return RETURN_NO;
   }

   public void setRETURN_NO(String RETURN_NO) {
      this.RETURN_NO = RETURN_NO;
   }

   public String getVOID_FLG() {
      return VOID_FLG;
   }

   public void setVOID_FLG(String VOID_FLG) {
      this.VOID_FLG = VOID_FLG;
   }

   public LocalDateTime getGCP_LANDING_DTM() {
      return GCP_LANDING_DTM;
   }

   public void setGCP_LANDING_DTM(LocalDateTime GCP_LANDING_DTM) {
      this.GCP_LANDING_DTM = GCP_LANDING_DTM;
   }

   public LocalDate getBUS_DAY() {
      return BUS_DAY;
   }

   public void setBUS_DAY(LocalDate BUS_DAY) {
      this.BUS_DAY = BUS_DAY;
   }
}
  