package com.ingka.sbp.di.pubsubbq;

import static javax.xml.stream.XMLInputFactory.newInstance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.google.common.io.ByteSource;
import com.ingka.sbp.di.poslogparse.xml.LineItem1;
import com.ingka.sbp.di.poslogparse.xml.RetailTransactionORG;
import com.ingka.sbp.di.poslogparse.xml.Transaction;

public class StaxParserTest {
	
	
	public static void main(String[] args) {
		
		
		String sss ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<POSLog xmlns=\"http://www.nrf-arts.org/IXRetail/namespace/\" xmlns:IKEA=\"ikea.com/pip/poslog/1.0/\" xmlns:WN=\"http://www.wincor-nixdorf.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
				+ "   <Transaction>\n"
				+ "      <RetailStoreID>367</RetailStoreID>\n"
				+ "      <BusinessUnit>\n"
				+ "         <UnitID TypeCode=\"RetailStore\">367</UnitID>\n"
				+ "      </BusinessUnit>\n"
				+ "      <OrganizationHierarchy Level=\"Corporation\" ID=\"PT\">IKEASetCountryCode2PT</OrganizationHierarchy>\n"
				+ "      <WorkstationID>10</WorkstationID>\n"
				+ "      <TillID>REGULAR</TillID>\n"
				+ "      <SequenceNumber>189</SequenceNumber>\n"
				+ "      <BusinessDayDate>2015-06-13</BusinessDayDate>\n"
				+ "      <BeginDateTime>2015-06-13T08:52:38</BeginDateTime>\n"
				+ "      <EndDateTime>2015-06-13T08:52:39</EndDateTime>\n"
				+ "      <CurrencyCode>EUR</CurrencyCode>\n"
				+ "      <RetailTransaction Version=\"2.2\">\n"
				+ "         <IKEA:TransactionSpecifics>\n"
				+ "            <IKEA:IsHomeDelivery>false</IKEA:IsHomeDelivery>\n"
				+ "         </IKEA:TransactionSpecifics>\n"
				+ "      </RetailTransaction>\n"
				+ "      <WN:PosVersion>4.5.2.12</WN:PosVersion>\n"
				+ "      <WN:SessionID>unknown</WN:SessionID>\n"
				+ "      <WN:WorkstationGroupID>REGULAR</WN:WorkstationGroupID>\n"
				+ "      <WN:Version>4.0.0.0</WN:Version>\n"
				+ "      <WN:CustomerID>IKEA</WN:CustomerID>\n"
				+ "      <WN:CustomerVersion>1.0</WN:CustomerVersion>\n"
				+ "      <IKEA:ReceiptImageSpecifics>\n"
				+ "         <IKEA:ReceiptPrintBuffer ImageType=\"string\">IE</IKEA:ReceiptPrintBuffer>\n"
				+ "         <IKEA:CustomerSignatureList />\n"
				+ "      </IKEA:ReceiptImageSpecifics>\n"
				+ "   </Transaction>\n"
				+ "   <Transaction>\n"
				+ "      <RetailStoreID>367</RetailStoreID>\n"
				+ "      <BusinessUnit>\n"
				+ "         <UnitID TypeCode=\"RetailStore\">367</UnitID>\n"
				+ "      </BusinessUnit>\n"
				+ "      <OrganizationHierarchy Level=\"Corporation\" ID=\"PT\">IKEASetCountryCode2PT</OrganizationHierarchy>\n"
				+ "      <WorkstationID>10</WorkstationID>\n"
				+ "      <TillID>REGULAR</TillID>\n"
				+ "      <SequenceNumber>26</SequenceNumber>\n"
				+ "      <BusinessDayDate>2015-06-13</BusinessDayDate>\n"
				+ "      <BeginDateTime>2015-06-13T12:33:51</BeginDateTime>\n"
				+ "      <EndDateTime>2015-06-13T12:34:41</EndDateTime>\n"
				+ "      <OperatorID OperatorName=\"Ana Filipa Silva\">19101573</OperatorID>\n"
				+ "      <CurrencyCode>EUR</CurrencyCode>\n"
				+ "      <RetailTransaction Version=\"2.2\">\n"
				+ "         <LineItem EntryMethod=\"Scanned\">\n"
				+ "            <SequenceNumber>5</SequenceNumber>\n"
				+ "            <Sale ItemType=\"Stock\">\n"
				+ "               <ItemID>80271714</ItemID>\n"
				+ "               <POSIdentity POSIDType=\"EAN\">\n"
				+ "                  <POSItemID>80271714</POSItemID>\n"
				+ "               </POSIdentity>\n"
				+ "               <MerchandiseHierarchy Level=\"MerchandiseHierarchyLevel\">1</MerchandiseHierarchy>\n"
				+ "               <MerchandiseHierarchy Level=\"MerchandiseStructure\">1</MerchandiseHierarchy>\n"
				+ "               <MerchandiseHierarchy Level=\"POSDepartment\">1121</MerchandiseHierarchy>\n"
				+ "               <Description>GRUSBLAD edred</Description>\n"
				+ "               <UnitListPrice>27.99</UnitListPrice>\n"
				+ "               <RegularSalesUnitPrice>27.99</RegularSalesUnitPrice>\n"
				+ "               <ActualSalesUnitPrice>27.99</ActualSalesUnitPrice>\n"
				+ "               <ExtendedAmount>27.99</ExtendedAmount>\n"
				+ "               <Quantity Units=\"1\" UnitOfMeasureCode=\"EA\">1</Quantity>\n"
				+ "               <Tax TaxType=\"VAT\">\n"
				+ "                  <SequenceNumber>1</SequenceNumber>\n"
				+ "                  <TaxAuthority>Portugal</TaxAuthority>\n"
				+ "                  <TaxableAmount TaxIncludedInTaxableAmountFlag=\"true\">27.99</TaxableAmount>\n"
				+ "                  <TaxablePercentage>100</TaxablePercentage>\n"
				+ "                  <Amount>5.2339</Amount>\n"
				+ "                  <Percent>23</Percent>\n"
				+ "                  <TaxRuleID>0</TaxRuleID>\n"
				+ "                  <TaxGroupID>0</TaxGroupID>\n"
				+ "                  <IKEA:TaxSpecifics>\n"
				+ "                     <IKEA:TaxCalcMethod>PERCENTAGE</IKEA:TaxCalcMethod>\n"
				+ "                     <IKEA:TaxLocation>Portugal</IKEA:TaxLocation>\n"
				+ "                     <IKEA:PrintedAmount>5.23</IKEA:PrintedAmount>\n"
				+ "                  </IKEA:TaxSpecifics>\n"
				+ "               </Tax>\n"
				+ "               <WN:AdditionalDesc>GRUSBLAD edred fresc 240x220</WN:AdditionalDesc>\n"
				+ "               <WN:BarcodeTemplate>80271714223509</WN:BarcodeTemplate>\n"
				+ "               <IKEA:ItemSpecifics BulkyItem=\"false\" FamilyItem=\"false\" BargainItem=\"false\">\n"
				+ "                  <IKEA:SupplierID>22350</IKEA:SupplierID>\n"
				+ "               </IKEA:ItemSpecifics>\n"
				+ "            </Sale>\n"
				+ "         </LineItem>\n"
				+ "         <LineItem EntryMethod=\"Scanned\">\n"
				+ "            <SequenceNumber>7</SequenceNumber>\n"
				+ "            <Sale ItemType=\"Stock\">\n"
				+ "               <ItemID>80240505</ItemID>\n"
				+ "               <POSIdentity POSIDType=\"EAN\">\n"
				+ "                  <POSItemID>80240505</POSItemID>\n"
				+ "               </POSIdentity>\n"
				+ "               <MerchandiseHierarchy Level=\"MerchandiseHierarchyLevel\">1</MerchandiseHierarchy>\n"
				+ "               <MerchandiseHierarchy Level=\"MerchandiseStructure\">1</MerchandiseHierarchy>\n"
				+ "               <MerchandiseHierarchy Level=\"POSDepartment\">1051</MerchandiseHierarchy>\n"
				+ "               <Description>ALKALISK pilh</Description>\n"
				+ "               <UnitListPrice>2.5</UnitListPrice>\n"
				+ "               <RegularSalesUnitPrice>2.5</RegularSalesUnitPrice>\n"
				+ "               <ActualSalesUnitPrice>2.5</ActualSalesUnitPrice>\n"
				+ "               <ExtendedAmount>2.5</ExtendedAmount>\n"
				+ "               <Quantity Units=\"1\" UnitOfMeasureCode=\"EA\">1</Quantity>\n"
				+ "               <Tax TaxType=\"VAT\">\n"
				+ "                  <SequenceNumber>1</SequenceNumber>\n"
				+ "                  <TaxAuthority>Portugal</TaxAuthority>\n"
				+ "                  <TaxableAmount TaxIncludedInTaxableAmountFlag=\"true\">2.5</TaxableAmount>\n"
				+ "                  <TaxablePercentage>100</TaxablePercentage>\n"
				+ "                  <Amount>0.4675</Amount>\n"
				+ "                  <Percent>23</Percent>\n"
				+ "                  <TaxRuleID>0</TaxRuleID>\n"
				+ "                  <TaxGroupID>0</TaxGroupID>\n"
				+ "                  <IKEA:TaxSpecifics>\n"
				+ "                     <IKEA:TaxCalcMethod>PERCENTAGE</IKEA:TaxCalcMethod>\n"
				+ "                     <IKEA:TaxLocation>Portugal</IKEA:TaxLocation>\n"
				+ "                     <IKEA:PrintedAmount>0.47</IKEA:PrintedAmount>\n"
				+ "                  </IKEA:TaxSpecifics>\n"
				+ "               </Tax>\n"
				+ "               <WN:AdditionalDesc>ALKALISK pilh alcal LR03 AAA 1.5V10uds</WN:AdditionalDesc>\n"
				+ "               <WN:BarcodeTemplate>80240505188629</WN:BarcodeTemplate>\n"
				+ "               <IKEA:ItemSpecifics BulkyItem=\"false\" FamilyItem=\"false\" BargainItem=\"false\">\n"
				+ "                  <IKEA:SupplierID>18862</IKEA:SupplierID>\n"
				+ "               </IKEA:ItemSpecifics>\n"
				+ "            </Sale>\n"
				+ "         </LineItem>\n"
				+ "         <LineItem EntryMethod=\"Keyed\">\n"
				+ "            <SequenceNumber>9</SequenceNumber>\n"
				+ "            <Sale ItemType=\"Stock\">\n"
				+ "               <ItemID>17228340</ItemID>\n"
				+ "               <POSIdentity POSIDType=\"EAN\">\n"
				+ "                  <POSItemID>17228340</POSItemID>\n"
				+ "               </POSIdentity>\n"
				+ "               <MerchandiseHierarchy Level=\"MerchandiseHierarchyLevel\">1</MerchandiseHierarchy>\n"
				+ "               <MerchandiseHierarchy Level=\"MerchandiseStructure\">1</MerchandiseHierarchy>\n"
				+ "               <MerchandiseHierarchy Level=\"POSDepartment\">1921</MerchandiseHierarchy>\n"
				+ "               <Description>FRAKTA saco,</Description>\n"
				+ "               <UnitListPrice>0.6</UnitListPrice>\n"
				+ "               <RegularSalesUnitPrice>0.6</RegularSalesUnitPrice>\n"
				+ "               <ActualSalesUnitPrice>0.5</ActualSalesUnitPrice>\n"
				+ "               <ExtendedAmount>1</ExtendedAmount>\n"
				+ "               <Quantity Units=\"2\" UnitOfMeasureCode=\"EA\">2</Quantity>\n"
				+ "               <RetailPriceModifier MethodCode=\"WN:CustomerPrice\">\n"
				+ "                  <SequenceNumber>6</SequenceNumber>\n"
				+ "                  <Amount Action=\"Replace\">0.5</Amount>\n"
				+ "                  <PreviousPrice>0.6</PreviousPrice>\n"
				+ "                  <ReasonCode>Customer_Price</ReasonCode>\n"
				+ "               </RetailPriceModifier>\n"
				+ "               <Tax TaxType=\"VAT\">\n"
				+ "                  <SequenceNumber>1</SequenceNumber>\n"
				+ "                  <TaxAuthority>Portugal</TaxAuthority>\n"
				+ "                  <TaxableAmount TaxIncludedInTaxableAmountFlag=\"true\">1</TaxableAmount>\n"
				+ "                  <TaxablePercentage>100</TaxablePercentage>\n"
				+ "                  <Amount>0.187</Amount>\n"
				+ "                  <Percent>23</Percent>\n"
				+ "                  <TaxRuleID>0</TaxRuleID>\n"
				+ "                  <TaxGroupID>0</TaxGroupID>\n"
				+ "                  <IKEA:TaxSpecifics>\n"
				+ "                     <IKEA:TaxCalcMethod>PERCENTAGE</IKEA:TaxCalcMethod>\n"
				+ "                     <IKEA:TaxLocation>Portugal</IKEA:TaxLocation>\n"
				+ "                     <IKEA:PrintedAmount>0.19</IKEA:PrintedAmount>\n"
				+ "                  </IKEA:TaxSpecifics>\n"
				+ "               </Tax>\n"
				+ "               <WN:AdditionalDesc>FRAKTA saco, grd 71 l azul</WN:AdditionalDesc>\n"
				+ "               <IKEA:ItemSpecifics BulkyItem=\"false\" FamilyItem=\"true\" BargainItem=\"false\" />\n"
				+ "            </Sale>\n"
				+ "         </LineItem>\n"
				+ "         <LineItem EntryMethod=\"Scanned\">\n"
				+ "            <SequenceNumber>11</SequenceNumber>\n"
				+ "            <Sale ItemType=\"Stock\">\n"
				+ "               <ItemID>50097995</ItemID>\n"
				+ "               <POSIdentity POSIDType=\"EAN\">\n"
				+ "                  <POSItemID>50097995</POSItemID>\n"
				+ "               </POSIdentity>\n"
				+ "               <MerchandiseHierarchy Level=\"MerchandiseHierarchyLevel\">1</MerchandiseHierarchy>\n"
				+ "               <MerchandiseHierarchy Level=\"MerchandiseStructure\">1</MerchandiseHierarchy>\n"
				+ "               <MerchandiseHierarchy Level=\"POSDepartment\">1632</MerchandiseHierarchy>\n"
				+ "               <Description>GLIMMA vel peq</Description>\n"
				+ "               <UnitListPrice>3.5</UnitListPrice>\n"
				+ "               <RegularSalesUnitPrice>3.5</RegularSalesUnitPrice>\n"
				+ "               <ActualSalesUnitPrice>3.5</ActualSalesUnitPrice>\n"
				+ "               <ExtendedAmount>3.5</ExtendedAmount>\n"
				+ "               <Quantity Units=\"1\" UnitOfMeasureCode=\"EA\">1</Quantity>\n"
				+ "               <Tax TaxType=\"VAT\">\n"
				+ "                  <SequenceNumber>1</SequenceNumber>\n"
				+ "                  <TaxAuthority>Portugal</TaxAuthority>\n"
				+ "                  <TaxableAmount TaxIncludedInTaxableAmountFlag=\"true\">3.5</TaxableAmount>\n"
				+ "                  <TaxablePercentage>100</TaxablePercentage>\n"
				+ "                  <Amount>0.6545</Amount>\n"
				+ "                  <Percent>23</Percent>\n"
				+ "                  <TaxRuleID>0</TaxRuleID>\n"
				+ "                  <TaxGroupID>0</TaxGroupID>\n"
				+ "                  <IKEA:TaxSpecifics>\n"
				+ "                     <IKEA:TaxCalcMethod>PERCENTAGE</IKEA:TaxCalcMethod>\n"
				+ "                     <IKEA:TaxLocation>Portugal</IKEA:TaxLocation>\n"
				+ "                     <IKEA:PrintedAmount>0.65</IKEA:PrintedAmount>\n"
				+ "                  </IKEA:TaxSpecifics>\n"
				+ "               </Tax>\n"
				+ "               <WN:AdditionalDesc>GLIMMA vel peq s/prf 100uds</WN:AdditionalDesc>\n"
				+ "               <WN:BarcodeTemplate>50097995216559</WN:BarcodeTemplate>\n"
				+ "               <IKEA:ItemSpecifics BulkyItem=\"false\" FamilyItem=\"false\" BargainItem=\"false\">\n"
				+ "                  <IKEA:SupplierID>21655</IKEA:SupplierID>\n"
				+ "               </IKEA:ItemSpecifics>\n"
				+ "            </Sale>\n"
				+ "         </LineItem>\n"
				+ "         <LineItem>\n"
				+ "            <SequenceNumber>14</SequenceNumber>\n"
				+ "            <Discount>\n"
				+ "               <Amount>0.2</Amount>\n"
				+ "               <PriceDerivationRule Reference=\"Customer\">\n"
				+ "                  <PriceDerivationRuleID />\n"
				+ "               </PriceDerivationRule>\n"
				+ "               <IKEA:DiscountSpecifics>\n"
				+ "                  <IKEA:DiscountableAmount>1.2</IKEA:DiscountableAmount>\n"
				+ "                  <IKEA:DiscountReference>6275980230292499398</IKEA:DiscountReference>\n"
				+ "               </IKEA:DiscountSpecifics>\n"
				+ "            </Discount>\n"
				+ "         </LineItem>\n"
				+ "         <LineItem>\n"
				+ "            <SequenceNumber>15</SequenceNumber>\n"
				+ "            <BeginDateTime>2015-06-13T12:34:21</BeginDateTime>\n"
				+ "            <Tender TenderType=\"CreditDebit\" TypeCode=\"Sale\" WN:Media=\"400\" WN:MediaMember=\"400\">\n"
				+ "               <Amount>34.99</Amount>\n"
				+ "               <CreditDebit CardType=\"Debit\" WN:MMType=\"VI CNEEAP\">\n"
				+ "                  <IssuerIdentificationNumber>422003</IssuerIdentificationNumber>\n"
				+ "                  <PrimaryAccountNumber>****************</PrimaryAccountNumber>\n"
				+ "                  <WN:ExpirationDate>2012-12</WN:ExpirationDate>\n"
				+ "               </CreditDebit>\n"
				+ "               <WN:Quantity>1</WN:Quantity>\n"
				+ "               <IKEA:TenderSpecifics ExternalTenderType=\"EFTONLINE\" />\n"
				+ "            </Tender>\n"
				+ "         </LineItem>\n"
				+ "         <LineItem>\n"
				+ "            <SequenceNumber>19</SequenceNumber>\n"
				+ "            <Tax TaxType=\"VAT\" TypeCode=\"Sale\">\n"
				+ "               <SequenceNumber>1</SequenceNumber>\n"
				+ "               <TaxAuthority>Portugal</TaxAuthority>\n"
				+ "               <TaxableAmount TaxIncludedInTaxableAmountFlag=\"false\">28.4472</TaxableAmount>\n"
				+ "               <TaxablePercentage>100</TaxablePercentage>\n"
				+ "               <Amount>6.5428</Amount>\n"
				+ "               <Percent>23</Percent>\n"
				+ "               <TaxRuleID>0</TaxRuleID>\n"
				+ "               <TaxGroupID>0</TaxGroupID>\n"
				+ "               <IKEA:TaxSpecifics>\n"
				+ "                  <IKEA:TaxCalcMethod>PERCENTAGE</IKEA:TaxCalcMethod>\n"
				+ "                  <IKEA:TaxLocation>Portugal</IKEA:TaxLocation>\n"
				+ "                  <IKEA:PrintedAmount>6.54</IKEA:PrintedAmount>\n"
				+ "               </IKEA:TaxSpecifics>\n"
				+ "            </Tax>\n"
				+ "         </LineItem>\n"
				+ "         <Total TotalType=\"TransactionNetAmount\" WN:DateTime=\"2015-06-13T12:34:17\">28.45</Total>\n"
				+ "         <Total TotalType=\"TransactionTaxAmount\" WN:DateTime=\"2015-06-13T12:34:17\">6.54</Total>\n"
				+ "         <Total TotalType=\"TransactionGrandAmount\" WN:DateTime=\"2015-06-13T12:34:17\">34.99</Total>\n"
				+ "         <LoyaltyAccount>\n"
				+ "            <CustomerID>ikeaFamily</CustomerID>\n"
				+ "            <LoyaltyProgram>\n"
				+ "               <LoyaltyProgramID>FAMILY CARD</LoyaltyProgramID>\n"
				+ "               <LoyaltyAccountID>6275980230292499398</LoyaltyAccountID>\n"
				+ "            </LoyaltyProgram>\n"
				+ "         </LoyaltyAccount>\n"
				+ "         <IKEA:TransactionSpecifics>\n"
				+ "            <IKEA:IsHomeDelivery>false</IKEA:IsHomeDelivery>\n"
				+ "         </IKEA:TransactionSpecifics>\n"
				+ "      </RetailTransaction>\n"
				+ "      <WN:CustomerSurvey>\n"
				+ "         <WN:SequenceNumber>4</WN:SequenceNumber>\n"
				+ "         <WN:CustomerSurveyID>zip_code</WN:CustomerSurveyID>\n"
				+ "         <WN:Name>ZIP Code</WN:Name>\n"
				+ "         <WN:Query>\n"
				+ "            <WN:SequenceNumber>1</WN:SequenceNumber>\n"
				+ "            <WN:Header>ZIP Code</WN:Header>\n"
				+ "            <WN:StringValue>2720468</WN:StringValue>\n"
				+ "         </WN:Query>\n"
				+ "      </WN:CustomerSurvey>\n"
				+ "      <WN:PosVersion>4.5.2.12</WN:PosVersion>\n"
				+ "      <WN:SessionID>367:10:2:19101573:20150613</WN:SessionID>\n"
				+ "      <WN:WorkstationGroupID>REGULAR</WN:WorkstationGroupID>\n"
				+ "      <WN:Version>4.0.0.0</WN:Version>\n"
				+ "      <WN:CustomerID>IKEA</WN:CustomerID>\n"
				+ "      <WN:CustomerVersion>1.0</WN:CustomerVersion>\n"
				+ "      <IKEA:ReceiptImageSpecifics>\n"
				+ "         <IKEA:ReceiptPrintBuffer ImageType=\"string\">IE9</IKEA:ReceiptPrintBuffer>\n"
				+ "         <IKEA:CustomerSignatureList />\n"
				+ "      </IKEA:ReceiptImageSpecifics>\n"
				+ "   </Transaction>\n"
				+ "</POSLog>";

        try {

        	try {
				System.out.println("from MAIN : " + printXmlByXmlCursorReaderPOSLOG(sss.getBytes()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

        } 
        //catch (FileNotFoundException | XMLStreamException e) {
        catch (XMLStreamException e) {
            e.printStackTrace();
        }
		
		
	}
	
	
	
	private static String printXmlByXmlCursorReaderPOSLOG(byte[] element) throws XMLStreamException, IOException {
		 XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
	     //   XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(
	     //           new FileInputStream(path.toFile()));
	        
	        String salary_text = "S ";
	        
	        //String pubsubHERE= pubsubMessage.getData();
	        
	        byte[] xmlContent = element;
			//KK
	        //XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(element.getBytes()));
	       // VAL 
	     //  
	        //XMLEventReader readerV = buildXMLEventReader(reader);
	        XMLEventReader reader = buildXMLEventReader(xmlContent );

	       // int eventType = reader.getEventType();
	       // System.out.println(eventType);   // 7, START_DOCUMENT
	        System.out.println(reader);      // xerces
	        
	        
	        ArrayList<Transaction> transactionsList = new ArrayList<>();
	        ArrayList<LineItem1> lineItemList = new ArrayList<>();
	        Transaction.TransactionBuilder transactionBuilder = null;
	        LineItem1.LineItemBuilder lineItemBuilder = null;
	       // RetailTransaction.RetailTransactionBuilder retailTransactionBuilder = null;
	        LocalDateTime parsedBeginDateTime = null;
	        String retailStoreId = null;
	        String currencyCode = null;
	        boolean isLineItem = false;
	        boolean isTax = false;
	        
	        //KK23
	        //int startElement = 1;

	        while (reader.hasNext()) {
	        	
	        	//VAL
	        	//XMLEvent nextEvent = ((XMLEventReader) reader).nextEvent();
	        	//XMLEvent nextEvent = reader.
	        	XMLEvent nextEvent = reader.nextEvent();
	        	

	            //eventType = reader.next();

	           // if (eventType == XMLEvent.START_ELEMENT) {
	        	if (nextEvent.isStartElement()) {
	            	
	            	//VAL
	            	//
	            	StartElement startElement = nextEvent.asStartElement();
	            	
	            	
	            	//KK23
	            	//startElement = XMLEvent.START_ELEMENT;

	              //KK  switch (reader.getName().getLocalPart()) {
	            	//VAL
	            	switch (startElement.getName().getLocalPart()) {
	                
	                    //VAL
		                case "Transaction":
	                        transactionBuilder = Transaction.builder();
	                        Attribute cancelFlagAttribute = startElement.getAttributeByName(new QName("CancelFlag"));
	                        if (cancelFlagAttribute != null) {
	                            transactionBuilder.cancelFlag(cancelFlagAttribute.getValue());
	                        }
	                        Attribute offlineFlagAttribute = startElement.getAttributeByName(new QName("OfflineFlag"));
	                        if (offlineFlagAttribute != null) {
	                            transactionBuilder.offlineFlag(offlineFlagAttribute.getValue());
	                        }
	                        break;
	                        
		                case "RetailStoreID":
	                        nextEvent = reader.nextEvent();
	                        retailStoreId = nextEvent.asCharacters().getData();
	                        transactionBuilder.retailStoreID(retailStoreId);
	                        break;
	                    case "WorkstationID":
	                        nextEvent = reader.nextEvent();
	                        transactionBuilder.workstationID(nextEvent.asCharacters()
	                                .getData());
	                        break;
	                    case "SequenceNumber":
	                        nextEvent = reader.nextEvent();
	                        String sequenceNumber = nextEvent.asCharacters().getData();
	                        if (!isLineItem && !isTax) {
	                            transactionBuilder.sequenceNumber(sequenceNumber);
	                        } else if (isLineItem) {
	                            lineItemBuilder.sequenceNumber(sequenceNumber);
	                        }
	                        break;     

	                 /*   case "staff":
	                        String id = reader.getAttributeValue(null, "id");
	                        System.out.printf("Staff id : %s%n", id);
	                        break;

	                    case "salary":
	                        String currency = reader.getAttributeValue(null, "currency");
	                        eventType = reader.next();
	                        salary_text = reader.getText();;
	                        if (eventType == XMLEvent.CHARACTERS) {
	                            String salary = reader.getText();
	                            System.out.printf("Salary [Currency] : %,.2f [%s]%n",
	                              Float.parseFloat(salary), currency);
	                        }
	                        break; */

	                    
	                }

	            }

	        	if (nextEvent.isEndElement()) {
	                EndElement endElement = nextEvent.asEndElement();
	                switch (endElement.getName().getLocalPart()) {
	                    case"Transaction" :
	                        Instant now = Instant.now();
	                        LocalDateTime utcDateTime = LocalDateTime.ofInstant(now, ZoneId.of("UTC"));
	                        String stringNow = utcDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
	                        //KK Transaction transaction = transactionBuilder.now(stringNow)
	                        //KK        .build();
	                        //KK transactionsList.add(transaction);
	                        break;

	                    case "LineItem":
	                        isLineItem = false;
	                        LineItem1 lineItem = lineItemBuilder.build();
	                        lineItemList.add(lineItem);
	                        break;

	                    case "RetailTransaction":
	                       //KK RetailTransaction retailTransaction = retailTransactionBuilder.lineItemList(lineItemList).build();
	                       //KK transactionBuilder.retailTransaction(retailTransaction);
	                       //KK lineItemList.clear();
	                        break;
	                }
	            }	
	        	
	        	
	        	
	          //  if (eventType == XMLEvent.END_ELEMENT) {
	          //      // if </staff>
	          //      if (reader.getName().getLocalPart().equals("staff")) {
	          //          System.out.printf("%n%s%n%n", "---");
	          //      }
	          //  }

	        }
			return salary_text + "_KKB";
	} 
	
	
	/* private int getHours(String retailStoreId, String currencyCode) {
	        int hours = 0;
	        if (currencyCode.equals(RUB) && retailStoreId.equals(RETAIL_STORE_ID_CODE)) {
	            hours = 2;
	        } else if (currencyCode.equals(RUB)) {
	            hours = 1;
	        }

	        return hours;
	    }*/

	    private static XMLEventReader buildXMLEventReader(byte[] xmlContent) throws IOException, XMLStreamException {
	        XMLInputFactory xmlInputFactory = newInstance();
	        try (InputStream inputStream = ByteSource.wrap(xmlContent).openStream()) {
	            return xmlInputFactory.createXMLEventReader(inputStream);
	        }
	    }

}
