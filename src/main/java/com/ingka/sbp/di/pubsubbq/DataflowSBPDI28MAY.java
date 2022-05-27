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
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.ingka.sbp.di.poslogparse.xml.TransactionGOOD;
import com.ingka.sbp.di.poslogparse.xml.TransactionINGKA;
import com.ingka.sbp.di.poslogparse.xml.TransactionINGKASTR;
import com.ingka.sbp.di.pubsubbq.ReadXmlStAXCursorParser;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataflowSBPDI28MAY {
	
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	public static LocalDateTime xxx; //parsedBeginDateTime, updatedBeginDateTime;
	
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	public static LocalDate yyy; //businessDayDate;
	
	public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
	    @Override
	    public void serialize(LocalDateTime arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
	        arg1.writeString(arg0.toString());
	    }
	}

	public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
	    @Override
	    public LocalDateTime deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException {
	        return LocalDateTime.parse(arg0.getText());
	    }
	}
	
	
	public class LocalDateSerializer extends JsonSerializer<LocalDate> {
	    @Override
	    public void serialize(LocalDate arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
	        arg1.writeString(arg0.toString());
	    }
	}

	public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
	    @Override
	    public LocalDate deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException {
	        return LocalDate.parse(arg0.getText());
	    }
	}
	
	
	
	public static final String RUB = "RUB";
    public static final String RETAIL_STORE_ID_CODE = "335";
	
	
	public static void main(String[] args) {
		
		valGCSoptions options =
		          PipelineOptionsFactory.fromArgs(args).withValidation()
		            .as(valGCSoptions.class);
		
		DataflowPipelineOptions dataflowPipeLineOptions = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
		dataflowPipeLineOptions.setJobName("StreamingIngestion28");
		dataflowPipeLineOptions.setProject("cpskk2021-03-1615568275864");
		
		dataflowPipeLineOptions.setRegion("us-central1");
	
		
		dataflowPipeLineOptions.setGcpTempLocation("gs://pubsubbb/tmp");
		options.setTempLocation("gs://pubsubbb/tmp");
		
		
		dataflowPipeLineOptions.setRunner(DataflowRunner.class);
		
		Pipeline pipeline = Pipeline.create(dataflowPipeLineOptions);
	
		PCollection<String>  pubsubmessage = pipeline.apply(PubsubIO.readStrings().fromTopic("projects/cpskk2021-03-1615568275864/topics/pubsubbqmay2022"));
	
		PCollection<String> to_parse = pubsubmessage;
		
		
		PCollection<TableRow> bqrow =  pubsubmessage.apply( ParDo.of(new ConvertStringBqA()) );
		
		bqrow.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.pubsubStream28").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
				.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER));
		
		
		
		//PCollection<TableRow> bqrow2 =  pubsubmessage.apply( ParDo.of(new ConvertStringBqB()) );
		PCollection<TableRow> bqrow2 =  pubsubmessage.apply( ParDo.of(new ConvertStringBqB()) );
	
		
		
		//bqrow2.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.pubsubStream24B").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
		//		.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER));
		
		
		bqrow2.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.cc_tra_ctm_testgood").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
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
			//26MAY out ArrayList<TransactionINGKASTR> parsed_list = printXmlByXmlCursorReaderPOSLOG(processContext.element());
			ArrayList<TransactionGOOD> parsed_list = printXmlByXmlCursorReaderPOSLOG(processContext.element()); 
			
			for (int i = 0; i < parsed_list.size(); i++) {
		       // TableFieldSchema col = getTableSchema().getFields().get(i);
		       // row.set(col.getName(), split[i]);
				TableRow tableRow = new TableRow()
						.set("BUS_DAY", parsed_list.get(i).getBUS_DAY().toString())
						.set("STO_NUM", parsed_list.get(i).getSTO_NO())
						.set("WS_ID", parsed_list.get(i).getWS_ID())
						.set("TRA_SEQ_NO", parsed_list.get(i).getTRA_SEQ_NO())
						.set("TRA_STA_DTM", parsed_list.get(i).getTRA_STA_DTM().toString())
						.set("TILL_TYPE", parsed_list.get(i).getTILL_TYPE())
						.set("CURCY_CODE", parsed_list.get(i).getCURCY_CODE())
						.set("TRA_STAT", parsed_list.get(i).getTRA_STAT())
						.set("CANC_FLG", parsed_list.get(i).getCANC_FLG())
						.set("OFLN_FLG", parsed_list.get(i).getOFLN_FLG())
						.set("ETL_INS_DTM", parsed_list.get(i).getETL_INS_DTM().toString());			
				
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
		
		
		//26 private ArrayList<TransactionINGKASTR> printXmlByXmlCursorReaderPOSLOG(String element) throws XMLStreamException, IOException {
		//26	 XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			 
		private ArrayList<TransactionGOOD> printXmlByXmlCursorReaderPOSLOG(String element) throws XMLStreamException, IOException {
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
		        
		        ArrayList<TransactionGOOD> tg_list = new ArrayList<>();
		        
		        ArrayList<LineItem> lineItemList = new ArrayList<>();
		        Transaction.TransactionBuilder transactionBuilder = null;
		        
		        TransactionGOOD tg = null;
		        
		        TransactionFIN.TransactionFINBuilder transactionBuilderFIN = null;
		        
		        LineItem.LineItemBuilder lineItemBuilder = null;
		        RetailTransaction.RetailTransactionBuilder retailTransactionBuilder = null;
		        
		        ArrayList<Sale> salesList = new ArrayList<>();
		        ArrayList<Tax> taxesList = new ArrayList<>();
		        
		   //zaraz     Tender.TenderBuilder tenderBuilder = null;
		   //zaraz     Sale.SaleBuilder saleBuilder = null;
		   //zaraz     Tax.TaxBuilder taxBuilder = null;
		        
		        
		        
		        //FAX 
		        LocalDateTime parsedBeginDateTime = null;
		        ObjectMapper o = new ObjectMapper();
		        o.writeValueAsString(parsedBeginDateTime);
		        //parsedBeginDateTime = null;
		        String retailStoreId = null;
		        String currencyCode = null;
		        boolean isLineItem = false;
		        boolean isTax = false;
		        boolean isTransaction = false;
		        
		        boolean isTender = false;
		        
		        boolean isSale = false;
		        
		        
		      /*  Article a = Article.builder()
		        		  .id(1L)
		        		  .title("Test Article")
		        		  .tags(Collections.singletonList("Demo"))
		        		  .build(); */
		        
		        
		       

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
		                    //	transactionBuilderFIN = TransactionFIN.builder();
		                    	
		                    //	TransactionFIN trans = TransactionFIN.builder();
		                    	
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
		                        
		                        
		                        //GOOD goes here
		                        
		                        tg = new TransactionGOOD();
		                        
		                        Instant now = Instant.now();
		                        LocalDateTime utcDateTime = LocalDateTime.ofInstant(now, ZoneId.of("UTC"));
		                        //VAL
		                        //Transaction transaction = transactionBuilder.now(utcDateTime)
		                        //        .build();
		                        
		                        
		                        //za wczesnie ... 21:52 ... czwartek
		                        
		                       //ORG 
		                        tg.setETL_INS_DTM(utcDateTime);       // niee setBUS_DAY(utcDateTime);
		                        
		                        
		                        //DateTime noww = new DateTime(); 
		                        //tg.setETL_INS_DTM(noww);
		                        
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
		                     
		                    case "BeginDateTime":
		                        nextEvent = reader.nextEvent();
		                        String beginDateTime = nextEvent.asCharacters().getData();
		                        parsedBeginDateTime = LocalDateTime.parse(beginDateTime, DateTimeFormatter.ISO_DATE_TIME);
		                        
		                        //VAL transactionBuilder.beginDateTime(parsedBeginDateTime);
		                        //KK
		                       //org 
		                        tg.setTRA_STA_DTM(parsedBeginDateTime);
		                        //DateTime dt_TRA_STA_DTM = new DateTime(beginDateTime);
		                        //tg.setTRA_STA_DTM(dt_TRA_STA_DTM);
		                        break;
		                    case "CurrencyCode":
		                        nextEvent = reader.nextEvent();
		                        currencyCode = nextEvent.asCharacters().getData();
		                        //VAL
		                        //transactionBuilder.currencyCode(currencyCode);
		                        //KK
		                        tg.setCURCY_CODE(currencyCode);
		                        
		                        int hours = getHours(retailStoreId, currencyCode);
		                        
		                        //@JsonSerialize(using = LocalDateTimeSerializer.class)
		                    	//@JsonDeserialize(using = LocalDateTimeDeserializer.class)
		                      //FAX  
		                        LocalDateTime updatedBeginDateTime = parsedBeginDateTime.minusHours(hours);
		                        //updatedBeginDateTime = parsedBeginDateTime.minusHours(hours);
		                        String businessDayDateString = updatedBeginDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
		                      //FAX  
		                        LocalDate businessDayDate = LocalDate.parse(businessDayDateString, DateTimeFormatter.ISO_LOCAL_DATE);
		                        //businessDayDate = LocalDate.parse(businessDayDateString, DateTimeFormatter.ISO_LOCAL_DATE);
		                        // VAL transactionBuilder.businessDayDate(businessDayDate);
		                        
		                       // Date dd = new Date();
		                     //ORG   
		                        tg.setBUS_DAY(businessDayDate);
		                        //
		                        //tg.setBUS_DAY(dd.setDate(hours));
		                        
		                       // Date date = new Date();  
		                       // SimpleDateFormat formatter = new SimpleDateFormat("YYYY/MM/DD");  
		                       // String strDate= formatter.format(date);  
		                       // tg.setBUS_DAY(date.);
		                        
		                    //    Date input = new Date();
		                    //    LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		                    //    tg.setBUS_DAY(input);
		                        break;    
		        	  
		                }
		            
		            }
		        	
		        	if (nextEvent.isEndElement()) {
		                EndElement endElement = nextEvent.asEndElement();
		                switch (endElement.getName().getLocalPart()) {
		                    case"Transaction" :
		                    //    isTransaction = false;
		                    //    Instant now = Instant.now();
		                    //    LocalDateTime utcDateTime = LocalDateTime.ofInstant(now, ZoneId.of("UTC"));
		                     //   Transaction transaction = transactionBuilder.now(utcDateTime)
		                     //           .build();
		                     //   transactionsList.add(transaction);
		                     //   transactionBuilder = null;
		                    	 tg_list.add(tg);
			                        
			                     tg = null;
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
				return tg_list; //transactionsList_ingka_str; //salary_text + "_KKB";
		} 
		
		private XMLEventReader buildXMLEventReader(byte[] xmlContent) throws IOException, XMLStreamException {
	        XMLInputFactory xmlInputFactory = newInstance();
	        try (InputStream inputStream = ByteSource.wrap(xmlContent).openStream()) {
	            return xmlInputFactory.createXMLEventReader(inputStream);
	        }
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
		
		
		
		
	}

}

