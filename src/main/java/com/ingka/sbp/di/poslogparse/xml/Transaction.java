package com.ingka.sbp.di.poslogparse.xml;

import javax.xml.crypto.dsig.XMLObject;

public class Transaction {
	
	
	String message;
	String messageid;
	String messageprocessingtime;
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public String getMessageprocessingtime() {
		return messageprocessingtime;
	}

	public void setMessageprocessingtime(String messageprocessingtime) {
		this.messageprocessingtime = messageprocessingtime;
	}

	public class TransactionBuilder {

		public void cancelFlag(String value) {
			// TODO Auto-generated method stub
			
		}

		public void offlineFlag(String value) {
			// TODO Auto-generated method stub
			
		}

		public void retailStoreID(String retailStoreId) {
			// TODO Auto-generated method stub
			
		}

		public void workstationID(String data) {
			// TODO Auto-generated method stub
			
		}

		public void sequenceNumber(String sequenceNumber) {
			// TODO Auto-generated method stub
			
		}

		public void beginDateTime(String beginDateTime) {
			// TODO Auto-generated method stub
			
		}

		public void currencyCode(String currencyCode) {
			// TODO Auto-generated method stub
			
		}

		public void businessDayDate(String businessDayDate) {
			// TODO Auto-generated method stub
			
		}

		public void tillId(String data) {
			// TODO Auto-generated method stub
			
		}

		public void transactionStatus(String value) {
			// TODO Auto-generated method stub
			
		}

		public void retailTransaction(RetailTransaction retailTransaction) {
			// TODO Auto-generated method stub
			
		}

		public Object now(String stringNow) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	XMLObject TransactionBuilder;
	
	public static com.ingka.sbp.di.poslogparse.xml.Transaction.TransactionBuilder builder() {
		return null;
		
	}

}
