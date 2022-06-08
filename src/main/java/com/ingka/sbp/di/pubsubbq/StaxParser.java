package com.ingka.sbp.di.pubsubbq;

import com.google.common.io.ByteSource;
import com.ingka.sbp.di.poslogparse.xml.LineItem1;
import com.ingka.sbp.di.poslogparse.xml.PayLoadd;
import com.ingka.sbp.di.poslogparse.xml.RetailTransactionORG;
import com.ingka.sbp.di.poslogparse.xml.Transaction;
import com.ingka.sbp.di.poslogparse.xml.XMLParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static javax.xml.stream.XMLInputFactory.newInstance;

public class StaxParser implements XMLParser {

    public static final String RUB = "RUB";
    public static final String RETAIL_STORE_ID_CODE = "335";

    @Override
    public PayLoadd parsePubsubMessage(byte[] xmlContent) throws IOException, XMLStreamException {
        XMLEventReader reader = buildXMLEventReader(xmlContent);

        ArrayList<Transaction> transactionsList = new ArrayList<>();
        ArrayList<LineItem1> lineItemList = new ArrayList<>();
        Transaction.TransactionBuilder transactionBuilder = null;
        LineItem1.LineItemBuilder lineItemBuilder = null;
        //RetailTransaction.RetailTransactionBuilder retailTransactionBuilder = null;
        LocalDateTime parsedBeginDateTime = null;
        String retailStoreId = null;
        String currencyCode = null;
        boolean isLineItem = false;
        boolean isTax = false;
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
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

                    case "BeginDateTime":
                        nextEvent = reader.nextEvent();
                        String beginDateTime = nextEvent.asCharacters().getData();
                        parsedBeginDateTime = LocalDateTime.parse(beginDateTime);
                        transactionBuilder.beginDateTime(beginDateTime);
                        break;

                    case "CurrencyCode":
                        nextEvent = reader.nextEvent();
                        currencyCode = nextEvent.asCharacters().getData();
                        transactionBuilder.currencyCode(currencyCode);
                        int hours = getHours(retailStoreId, currencyCode);
                        LocalDateTime updatedBeginDateTime = parsedBeginDateTime.minusHours(hours);
                        String businessDayDate = updatedBeginDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
                        transactionBuilder.businessDayDate(businessDayDate);
                        break;

                    case "TillID":
                        nextEvent = reader.nextEvent();
                        transactionBuilder.tillId(nextEvent.asCharacters()
                                .getData());
                        break;

                    case "RetailTransaction":
                        //retailTransactionBuilder = RetailTransaction.builder();
                        Attribute transactionStatusAttribute = startElement.getAttributeByName(new QName("TransactionStatus"));
                        if (transactionStatusAttribute != null) {
                            transactionBuilder.transactionStatus(transactionStatusAttribute.getValue());
                        }
                        break;

                    case "LineItem":
                        isLineItem = true;
                        lineItemBuilder = LineItem1.builder();
                        Attribute voidFlagAttribute = startElement.getAttributeByName(new QName("VoidFlag"));
                        if (voidFlagAttribute != null) {
                            lineItemBuilder.voidFlag(voidFlagAttribute.getValue());
                        }
                        Attribute entryMethodAttribute = startElement.getAttributeByName(new QName("EntryMethod"));
                        if (entryMethodAttribute != null) {
                            lineItemBuilder.voidFlag(entryMethodAttribute.getValue());
                        }
                        break;

//                    case "SequenceNumber":
//                        nextEvent = reader.nextEvent();
//                        transactionBuilder.tillId(nextEvent.asCharacters()
//                                .getData());
//                        break;
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
        }

        return null; //PayLoadd.builder();
                //.transactionList(transactionsList)
                //.build();
    }

    private int getHours(String retailStoreId, String currencyCode) {
        int hours = 0;
        if (currencyCode.equals(RUB) && retailStoreId.equals(RETAIL_STORE_ID_CODE)) {
            hours = 2;
        } else if (currencyCode.equals(RUB)) {
            hours = 1;
        }

        return hours;
    }

    private XMLEventReader buildXMLEventReader(byte[] xmlContent) throws IOException, XMLStreamException {
        XMLInputFactory xmlInputFactory = newInstance();
        try (InputStream inputStream = ByteSource.wrap(xmlContent).openStream()) {
            return xmlInputFactory.createXMLEventReader(inputStream);
        }
    }
}