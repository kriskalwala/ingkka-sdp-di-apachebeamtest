package com.ingka.sbp.di.pubsubbq;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
//not now import org.apache.beam.sdk.Pipeline;
// not now import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

import com.google.api.services.bigquery.model.Table;
import com.google.api.services.bigquery.model.TableRow;
import com.google.common.io.ByteSource;
import com.ingka.sbp.di.poslogparse.xml.Sale;
import com.ingka.sbp.di.poslogparse.xml.Tax;
import com.ingka.sbp.di.poslogparse.xml.Tender;

import static javax.xml.stream.XMLInputFactory.newInstance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

import com.ingka.sbp.di.poslogparse.xml.Article;
import com.ingka.sbp.di.poslogparse.xml.LineItem;
import com.ingka.sbp.di.poslogparse.xml.RetailTransaction;
import com.ingka.sbp.di.poslogparse.xml.Transaction;
import com.ingka.sbp.di.poslogparse.xml.TransactionFIN;
import com.ingka.sbp.di.poslogparse.xml.TransactionFIN.TransactionFINBuilder;
import com.ingka.sbp.di.poslogparse.xml.TransactionINGKA;
import com.ingka.sbp.di.poslogparse.xml.TransactionINGKASTR;
import com.ingka.sbp.di.pubsubbq.ReadXmlStAXCursorParser;

public class DataflowSBPDI26 {
	
	
	public static void main(String[] args) {
		
		valGCSoptions options =
		          PipelineOptionsFactory.fromArgs(args).withValidation()
		            .as(valGCSoptions.class);
		
		DataflowPipelineOptions dataflowPipeLineOptions = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
		dataflowPipeLineOptions.setJobName("StreamingIngestion26MAY");
		dataflowPipeLineOptions.setProject("cpskk2021-03-1615568275864");
		
		dataflowPipeLineOptions.setRegion("us-central1");
	
		
		dataflowPipeLineOptions.setGcpTempLocation("gs://pubsubbb/tmp");
		options.setTempLocation("gs://pubsubbb/tmp");
		
		
		dataflowPipeLineOptions.setRunner(DataflowRunner.class);
		
		Pipeline pipeline = Pipeline.create(dataflowPipeLineOptions);
	
		PCollection<String>  pubsubmessage = pipeline.apply(PubsubIO.readStrings().fromTopic("projects/cpskk2021-03-1615568275864/topics/pubsubbqmay2022"));
	
		PCollection<String> to_parse = pubsubmessage;
		
		
		PCollection<TableRow> bqrow =  pubsubmessage.apply( ParDo.of(new ConvertStringBqA()) );
		
		bqrow.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.pubsubStream24A").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
				.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER));
		
		
		
		//PCollection<TableRow> bqrow2 =  pubsubmessage.apply( ParDo.of(new ConvertStringBqB()) );
		PCollection<TableRow> bqrow2 =  pubsubmessage.apply( ParDo.of(new ConvertStringBqB()) );
	
		
		
		//bqrow2.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.pubsubStream24B").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
		//		.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER));
		
		
		bqrow2.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.CC_TRA_CTM_test").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
				.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)); 
		
		
		// here goes POSLOG stuff
		
		
		
		
		pipeline.run();
		
	}
	
	
	public static class ConvertStringBqA extends DoFn<String, TableRow> {
		
		@ProcessElement
		public void processing(ProcessContext processContext) throws XMLStreamException {
			
			
			//parse XML
			
			//String parsed = printXmlByXmlCursorReaderA(processContext.element());
			ArrayList<Transaction> parsed_list = printXmlByXmlCursorReaderA(processContext.element());
			
		//	ArrayList<String> arr = new ArrayList<String>();
			
			
			
			
			for (int i = 0; i < parsed_list.size(); i++) {
			       // TableFieldSchema col = getTableSchema().getFields().get(i);
			       // row.set(col.getName(), split[i]);
					TableRow tableRow = new TableRow().set("message", parsed_list.get(i).getMessage())
							.set("messageid", parsed_list.get(i).getMessageid())
							.set("messageprocessingtime", parsed_list.get(i).getMessageprocessingtime());
					
					
					
					processContext.output(tableRow);	
					
			}
			
		
		}

		private ArrayList<Transaction> printXmlByXmlCursorReaderA(String element) throws XMLStreamException {
			 XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		     //   XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(
		     //           new FileInputStream(path.toFile()));
		        
		        String salary_text = "S ";
		        
		        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
		        
		        //String pubsubHERE= pubsubMessage.getData();
		        
		        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(element.getBytes()));

		        int eventType = reader.getEventType();
		        System.out.println(eventType);   // 7, START_DOCUMENT
		        System.out.println(reader);      // xerces

		        while (reader.hasNext()) {

		            eventType = reader.next();

		            if (eventType == XMLEvent.START_ELEMENT) {

		                switch (reader.getName().getLocalPart()) {

		                    case "staff":
		                        String id = reader.getAttributeValue(null, "id");
		                        System.out.printf("Staff id : %s%n", id);
		                        break;
		                        
		                    case "Transaction":
		                        //String id2 = reader.getAttributeValue(null, "id");
		                        //System.out.printf("Staff id : %s%n", id2);
		                        Transaction t = new Transaction();
		                        t.setMessage("messageA");
		                        t.setMessageid("messageidA");
		                        t.setMessageprocessingtime("timeA");
		                        transactionList.add(t); 
		                        break;     

		                    case "name":
		                        eventType = reader.next();
		                        if (eventType == XMLEvent.CHARACTERS) {
		                            System.out.printf("Name : %s%n", reader.getText());
		                        }
		                        break;

		                    case "role":
		                        eventType = reader.next();
		                        if (eventType == XMLEvent.CHARACTERS) {
		                            System.out.printf("Role : %s%n", reader.getText());
		                        }
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
		                        break;

		                    case "bio":
		                        eventType = reader.next();
		                        if (eventType == XMLEvent.CHARACTERS) {
		                            System.out.printf("Bio : %s%n", reader.getText());
		                        }
		                        break;
		                }

		            }

		            if (eventType == XMLEvent.END_ELEMENT) {
		                // if </staff>
		                if (reader.getName().getLocalPart().equals("staff")) {
		                    System.out.printf("%n%s%n%n", "---");
		                }
		            }

		        }
				return transactionList; //salary_text + "_KKA";
		} 
		
		
	}
	
	
	
public static class ConvertStringBqB extends DoFn<String, TableRow> {
		
		@ProcessElement
		public void processing(ProcessContext processContext) throws XMLStreamException, IOException {
			
			
			//parse XML
			
			// 24 MAY evening ArrayList<Transaction> parsed_list = printXmlByXmlCursorReaderB(processContext.element());
			ArrayList<TransactionINGKASTR> parsed_list = printXmlByXmlCursorReaderPOSLOG(processContext.element());
			 
			
			for (int i = 0; i < parsed_list.size(); i++) {
		       // TableFieldSchema col = getTableSchema().getFields().get(i);
		       // row.set(col.getName(), split[i]);
				TableRow tableRow = new TableRow()
						.set("BUS_DAY", parsed_list.get(i).getBUS_DAY())
						.set("STO_NUM", parsed_list.get(i).getSTO_NO())
						.set("WS_ID", parsed_list.get(i).getWS_ID())
						.set("TRA_SEQ_NO", parsed_list.get(i).getTRA_SEQ_NO())
						.set("CANC_FLG", parsed_list.get(i).getCANC_FLG())
						.set("OFLN_FLG", parsed_list.get(i).getOFLN_FLG())
						.set("ETL_INS_DTM", parsed_list.get(i).getETL_INS_DTM());			
				
				processContext.output(tableRow);	
				
		    }
			
			
		}

		private ArrayList<Transaction> printXmlByXmlCursorReaderB(String element) throws XMLStreamException {
			 XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		     //   XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(
		     //           new FileInputStream(path.toFile()));
			 
			    
		        
		        String salary_text = "S ";
		        
		        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
		        
		        //String pubsubHERE= pubsubMessage.getData();
		        
		        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(element.getBytes()));

		        int eventType = reader.getEventType();
		        System.out.println(eventType);   // 7, START_DOCUMENT
		        System.out.println(reader);      // xerces

		        while (reader.hasNext()) {

		            eventType = reader.next();

		            if (eventType == XMLEvent.START_ELEMENT) {

		                switch (reader.getName().getLocalPart()) {

		                    case "staff":
		                        String id = reader.getAttributeValue(null, "id");
		                        System.out.printf("Staff id : %s%n", id);
		                        break;
		                        
		                    case "Transaction":
		                        //String id2 = reader.getAttributeValue(null, "id");
		                        //System.out.printf("Staff id : %s%n", id2);
		                        Transaction t = new Transaction();
		                        t.setMessage("messageB");
		                        t.setMessageid("messageidB");
		                        t.setMessageprocessingtime("timeB");
		                        transactionList.add(t); 
		                        break;    

		                    case "name":
		                        eventType = reader.next();
		                        if (eventType == XMLEvent.CHARACTERS) {
		                            System.out.printf("Name : %s%n", reader.getText());
		                        }
		                        break;

		                    case "role":
		                        eventType = reader.next();
		                        if (eventType == XMLEvent.CHARACTERS) {
		                            System.out.printf("Role : %s%n", reader.getText());
		                        }
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
		                        break;

		                    case "bio":
		                        eventType = reader.next();
		                        if (eventType == XMLEvent.CHARACTERS) {
		                            System.out.printf("Bio : %s%n", reader.getText());
		                        }
		                        break;
		                }

		            }

		            if (eventType == XMLEvent.END_ELEMENT) {
		                // if </staff>
		                if (reader.getName().getLocalPart().equals("staff")) {
		                    System.out.printf("%n%s%n%n", "---");
		                }
		            }
		            
		            
		            
		          /*  Transaction t2 = new Transaction();
                    t2.setMessage("message");
                    t2.setMessageid("messageid");
                    t2.setMessageprocessingtime("time");
                    transactionList.add(t2);
                    
                    Transaction t3 = new Transaction();
                    t3.setMessage("message");
                    t3.setMessageid("messageid");
                    t3.setMessageprocessingtime("time");
                    transactionList.add(t3); */
		           

		        }
				return transactionList; //salary_text + "_KKB";
		} 
		
		
		private ArrayList<TransactionINGKASTR> printXmlByXmlCursorReaderPOSLOG(String element) throws XMLStreamException, IOException {
			 XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		     //   XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(
		     //           new FileInputStream(path.toFile()));
		        
		        String salary_text = "S ";
		        Map<String , ArrayList<Object>> output = new HashMap();
		        
		        //String pubsubHERE= pubsubMessage.getData();
		        
		      //KK  XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(element.getBytes()));
		       // VAL 
		        XMLEventReader reader = buildXMLEventReader(element.getBytes(StandardCharsets.UTF_8));

		      //KK  int eventType = reader.getEventType();
		      //KK  System.out.println(eventType);   // 7, START_DOCUMENT
		      //KK  System.out.println(reader);      // xerces
		        
		        
		        ArrayList<Transaction> transactionsList = new ArrayList<>();
		        ArrayList<TransactionINGKA> transactionsList_ingka = new ArrayList<>();
		        ArrayList<TransactionINGKASTR> transactionsList_ingka_str = new ArrayList<>();
		        
		        ArrayList<LineItem> lineItemList = new ArrayList<>();
		        Transaction.TransactionBuilder transactionBuilder = null;
		        
		        TransactionFIN.TransactionFINBuilder transactionBuilderFIN = null;
		        
		        LineItem.LineItemBuilder lineItemBuilder = null;
		        RetailTransaction.RetailTransactionBuilder retailTransactionBuilder = null;
		        
		        ArrayList<Sale> salesList = new ArrayList<>();
		        ArrayList<Tax> taxesList = new ArrayList<>();
		        
		   //zaraz     Tender.TenderBuilder tenderBuilder = null;
		   //zaraz     Sale.SaleBuilder saleBuilder = null;
		   //zaraz     Tax.TaxBuilder taxBuilder = null;
		        
		        LocalDateTime parsedBeginDateTime = null;
		        String retailStoreId = null;
		        String currencyCode = null;
		        boolean isLineItem = false;
		        boolean isTax = false;
		        boolean isTransaction = false;
		        
		        boolean isTender = false;
		        
		        boolean isSale = false;
		        
		        
		        Article a = Article.builder()
		        		  .id(1L)
		        		  .title("Test Article")
		        		  .tags(Collections.singletonList("Demo"))
		        		  .build();
		        
		        
		       

		        while (reader.hasNext()) {
		        	
		        	//VAL
		        	//XMLEvent nextEvent = ((XMLEventReader) reader).nextEvent();
		        	//XMLEvent nextEvent = reader.nextEvent();
		        	XMLEvent nextEvent = reader.nextEvent();
		        	
		        	
		        	
		        	if (nextEvent.isStartElement()) {
		                StartElement startElement = nextEvent.asStartElement();
		                switch (startElement.getName().getLocalPart()) {
		                    case "Transaction":
		                        isTransaction = true;
		                    	
		                    	transactionBuilder = Transaction.builder();
		                    	transactionBuilderFIN = TransactionFIN.builder();
		                    	
		                    	TransactionFIN trans = TransactionFIN.builder();
		                    	
		        		    //po co dwa razy    transactionFINBuilder = TransactionFIN.builder();
		                        TransactionINGKA transaction_ingka = new TransactionINGKA();
		                        TransactionINGKASTR transaction_ingka_str = new TransactionINGKASTR();
		                        /*
		                        BUS_DAY STRING,
		                        STO_NUM STRING,
		                        WS_ID STRING,
		                        TRA_SEQ_NO STRING,
		                        CANC_FLG STRING, 
		                        OFLN_FLG STRING,
		                        ETL_INS_DTM STRING,*/
		                        transaction_ingka_str.setBUS_DAY("bus day");
		                        transaction_ingka_str.setSTO_NO("sto no 1");
		                        transaction_ingka_str.setWS_ID("ws id"); 
		                        transaction_ingka_str.setTRA_SEQ_NO("setTRA_SEQ_NO");
		                        transaction_ingka_str.setCANC_FLG("setCANC_FLG");
		                        transaction_ingka_str.setOFLN_FLG("setOFLN_FLG");
		                        transaction_ingka_str.setETL_INS_DTM("setETL_INS_DTM");
		                        
		                        transactionsList_ingka_str.add(transaction_ingka_str); 
		                        transactionBuilder = Transaction.builder();
		                       /* Attribute cancelFlagAttribute = startElement.getAttributeByName(new QName("CancelFlag"));
		                        if (cancelFlagAttribute != null) {
		                            transactionBuilder.cancelFlag(cancelFlagAttribute.getValue());
		                        }
		                        Attribute offlineFlagAttribute = startElement.getAttributeByName(new QName("OfflineFlag"));
		                        if (offlineFlagAttribute != null) {
		                            transactionBuilder.offlineFlag(offlineFlagAttribute.getValue());
		                        }*/
		                        
		                        Attribute cancelFlagAttribute = startElement.getAttributeByName(new QName("CancelFlag"));
		                        if (cancelFlagAttribute != null) {
		                            transactionBuilderFIN.cancelFlag(cancelFlagAttribute.getValue());
		                        }
		                        Attribute offlineFlagAttribute = startElement.getAttributeByName(new QName("OfflineFlag"));
		                        if (offlineFlagAttribute != null) {
		                            transactionBuilderFIN.offlineFlag(offlineFlagAttribute.getValue());
		                        }
		                     break;
		        	  
		                }
		            
		            }
		        	
		        	if (nextEvent.isEndElement()) {
		                EndElement endElement = nextEvent.asEndElement();
		                switch (endElement.getName().getLocalPart()) {
		                    case"Transaction" :
		                        isTransaction = false;
		                        Instant now = Instant.now();
		                        LocalDateTime utcDateTime = LocalDateTime.ofInstant(now, ZoneId.of("UTC"));
		                        Transaction transaction = transactionBuilder.now(utcDateTime)
		                                .build();
		                        transactionsList.add(transaction);
		                        transactionBuilder = null;
		                        break;

		                   
		                }
		            }

		           // if (eventType == XMLEvent.END_ELEMENT) {
		           //     // if </staff>
		           //     if (reader.getName().getLocalPart().equals("staff")) {
		           //         System.out.printf("%n%s%n%n", "---");
		           //     }
		           // }

		        }
				return transactionsList_ingka_str; //salary_text + "_KKB";
		} 
		
		private XMLEventReader buildXMLEventReader(byte[] xmlContent) throws IOException, XMLStreamException {
	        XMLInputFactory xmlInputFactory = newInstance();
	        try (InputStream inputStream = ByteSource.wrap(xmlContent).openStream()) {
	            return xmlInputFactory.createXMLEventReader(inputStream);
	        }
	    }
		
		
		
		
	}

}

