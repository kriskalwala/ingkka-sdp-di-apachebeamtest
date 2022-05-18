package com.ingka.sbp.di.poslogparse.xml;

import com.google.common.io.ByteSource;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import 

//import 
static javax.xml.stream.XMLInputFactory.newInstance;

public class StaxParser implements XMLParser {
    @Override
    public PayLoadd parsePubsubMessage(byte[] xmlContent) throws IOException, XMLStreamException {
        XMLEventReader reader = buildXMLEventReader(xmlContent);

        ArrayList<Transaction> transactionsList = new ArrayList<>();
        Transaction.TransactionBuilder builder = null;
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
//                    # Business Day to be derived from transaction start time
//                    #BusinessDayDate = getTagValue(Transaction, ns_arts, 'BusinessDayDate')
//                    BeginDateTime   = getTagValue(Transaction, ns_arts, 'BeginDateTime')
//                    CurrencyCode = getTagValue(Transaction, ns_arts, 'CurrencyCode')

                    case "Transaction":
                        //builder = Transaction.builder();
                        break;
                   /* case "RetailStoreID":
                        nextEvent = reader.nextEvent();
                        builder.retailStoreID(nextEvent.asCharacters()
                                .getData());
                        break;
                    case "WorkstationID":
                        nextEvent = reader.nextEvent();
                        builder.workstationID(nextEvent.asCharacters()
                                .getData());
                        break;
                    case "SequenceNumber":
                        nextEvent = reader.nextEvent();
                        builder.sequenceNumber(nextEvent.asCharacters()
                                .getData());
                        break; */
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("Transaction")) {
                    transactionsList.add(null);
                }
            }
        }

        return new PayLoadd();
    }

    private XMLEventReader buildXMLEventReader(byte[] xmlContent) throws IOException, XMLStreamException {
        XMLInputFactory xmlInputFactory = newInstance();
        try (InputStream inputStream = ByteSource.wrap(xmlContent).openStream()) {
            return xmlInputFactory.createXMLEventReader(inputStream);
        }
    }
}
