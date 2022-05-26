package com.ingka.sbp.di.poslogparse.xml;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionFIN implements Serializable {
   
	private String retailStoreID;
    private Integer workstationID;
    private Integer sequenceNumber;
    private LocalDateTime beginDateTime;
    private String currencyCode;
    private LocalDate businessDayDate;
    private String cancelFlag;
    private String offlineFlag;
    private String transactionStatus;
    private String tillId;
    private LocalDateTime now;
    private RetailTransaction retailTransaction;
    
    public class TransactionFINBuilder {
    	private String retailStoreID;
        private Integer workstationID;
        private Integer sequenceNumber;
        private LocalDateTime beginDateTime;
        private String currencyCode;
        private LocalDate businessDayDate;
        private String cancelFlag;
        private String offlineFlag;
        private String transactionStatus;
        private String tillId;
        private LocalDateTime now;
        private RetailTransaction retailTransaction;
        
        public TransactionFINBuilder retailStoreID(String retailStoreID) {
            this.retailStoreID = retailStoreID;
            //verifyTextOrFile();
            return this;
        }
        
        public TransactionFINBuilder cancelFlag(String cancelFlag) {
            this.cancelFlag = cancelFlag;
            //verifyTextOrFile();
            return this;
        }
        
        public TransactionFINBuilder offlineFlag(String offlineFlag) {
            this.offlineFlag = offlineFlag;
            //verifyTextOrFile();
            return this;
        }
        
        public TransactionFINBuilder builderKK() {
            
        	this.retailStoreID = retailStoreID;
            //private Integer workstationID;
            //private Integer sequenceNumber;
            //private LocalDateTime beginDateTime;
            //private String currencyCode;
            //private LocalDate businessDayDate;
            //private String cancelFlag;
            //private String offlineFlag;
            //private String transactionStatus;
            //private String tillId;
            //private LocalDateTime now;
            //private RetailTransaction retailTransaction;
            return this;
        }

   	}
}

