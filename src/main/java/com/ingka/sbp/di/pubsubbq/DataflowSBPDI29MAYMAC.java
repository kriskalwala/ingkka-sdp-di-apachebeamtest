package com.ingka.sbp.di.pubsubbq;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.api.services.bigquery.model.TableRow;
import com.google.common.io.ByteSource;
//import com.ikea.sbp.di.xml.LineItem;
//import com.ikea.sbp.di.xml.RetailTransaction;
import com.ingka.sbp.di.poslogparse.xml.*;
import com.ingka.sbp.di.poslogparse.xml.TransactionFIN.TransactionFINBuilder;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javax.xml.stream.XMLInputFactory.newInstance;

//not now import org.apache.beam.sdk.Pipeline;
// not now import org.apache.beam.sdk.options.PipelineOptions;

public class DataflowSBPDI29MAYMAC {


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


   //should be private
   public static final String RUB = "RUB";
   public static final String RETAIL_STORE_ID_CODE = "335";
   public static final String IXRETAIL_NAMESPACE = "http://www.nrf-arts.org/IXRetail/namespace/";


   public static void main(String[] args) {

      valGCSoptions options =
                PipelineOptionsFactory.fromArgs(args).withValidation()
                  .as(valGCSoptions.class);

      DataflowPipelineOptions dataflowPipeLineOptions = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
      //INGKA dataflowPipeLineOptions.setJobName("poslogtestjob28");
      //INGKA dataflowPipeLineOptions.setProject("ingka-sbp-di-dev");
      dataflowPipeLineOptions.setJobName("StreamingIngestion28");
	  dataflowPipeLineOptions.setProject("cpskk2021-03-1615568275864");
      
      
      
      dataflowPipeLineOptions.setRegion("europe-west1"); //us-central1


      //INGKA dataflowPipeLineOptions.setGcpTempLocation("gs://ingka-sbp-di-dev/poslog/krzys");
      //INGKA options.setTempLocation("gs://ingka-sbp-di-dev/poslog/krzys");
      
      dataflowPipeLineOptions.setGcpTempLocation("gs://pubsubbb/tmp");
	  options.setTempLocation("gs://pubsubbb/tmp");


      dataflowPipeLineOptions.setRunner(DataflowRunner.class);

      Pipeline pipeline = Pipeline.create(dataflowPipeLineOptions);

      //INGKA PCollection<String>  pubsubmessage = pipeline.apply(PubsubIO.readStrings().fromTopic("projects/ingka-sbp-di-dev/topics/test_pubsub_for_bg_poslog_parallel"));
      PCollection<String>  pubsubmessage = pipeline.apply(PubsubIO.readStrings().fromTopic("projects/cpskk2021-03-1615568275864/topics/pubsubbqmay2022"));

      PCollection<String> to_parse = pubsubmessage;


   //DELL sobota PCollection<TableRow> bqrow =  pubsubmessage.apply( ParDo.of(new ConvertStringBqA()) );

   //DELL sobota bqrow.apply(BigQueryIO.writeTableRows().to("ingka-sbp-di-dev:playground.test_poslog_B").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
   //DELL sobota       .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
   //DELL sobota       .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)););




      PCollection<TableRow> bqrow2 =  pubsubmessage.apply( ParDo.of(new ParseTransactionXMLData()) );




      bqrow2.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.cc_tra_ctm_testgood").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
            .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
            .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));
      
    //INGKA bqrow2.apply(BigQueryIO.writeTableRows().to("ingka-sbp-di-dev:playground.cc_tra_ctm_parallel").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
    //INGKA           .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
    //INGKA           .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));



      // here goes POSLOG stuff




      pipeline.run();

   }


   public static class ConvertStringBqA extends DoFn<String, TableRow> {

      @ProcessElement
      public void processing(ProcessContext processContext) throws XMLStreamException {


         //parse XML

         //String parsed = printXmlByXmlCursorReaderA(processContext.element());
         ArrayList<Transaction> parsed_list = printXmlByXmlCursorReaderA(processContext.element());

      // ArrayList<String> arr = new ArrayList<String>();




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



public static class ParseTransactionXMLData extends DoFn<String, TableRow> {

      @ProcessElement
      public void processing(ProcessContext processContext) throws XMLStreamException, IOException {


         //parse XML

         // 24 MAY evening ArrayList<Transaction> parsed_list = printXmlByXmlCursorReaderB(processContext.element());
         //26MAY out ArrayList<TransactionINGKASTR> parsed_list = printXmlByXmlCursorReaderPOSLOG(processContext.element());
         ArrayList<TransactionGOOD> parsed_list = parsePubsubMessagePOSLOG(processContext.element());

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

      

      private ArrayList<TransactionGOOD> parsePubsubMessagePOSLOG(String element) throws XMLStreamException, IOException {
             XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
          
             
             ArrayList<TransactionGOOD> tg_list = new ArrayList<>();
              
              Map<String , ArrayList<Object>> output = new HashMap();
              //Map<String, DynamicTypeValue> theMap = new HashMap<>();
              //output.put("transactionList", tg_list);

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

              TransactionGOOD tg = null;

              TransactionFINBuilder transactionBuilderFIN = null;
              
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
                      QName startElementName = startElement.getName();
                      switch (startElement.getName().getLocalPart()) {
                          case "Transaction":
                              isTransaction = true;
                              
                              //GOOD goes here
                              
                              tg = new TransactionGOOD();
                              
                              Instant now = Instant.now();
                              LocalDateTime utcDateTime = LocalDateTime.ofInstant(now, ZoneId.of("UTC"));
                              //VAL
                              //Transaction transaction = transactionBuilder.now(utcDateTime)
                              //        .build();
                              
                            
                              tg.setETL_INS_DTM(utcDateTime);                                                                
                           
                              // transactionBuilder = Transaction.builder();
                                                          
                              Attribute cancelFlagAttribute = startElement.getAttributeByName(new QName("CancelFlag"));
                              if (cancelFlagAttribute != null) {                 
                            	  	tg.setCANC_FLG(cancelFlagAttribute.getValue());
                              }
                              Attribute offlineFlagAttribute = startElement.getAttributeByName(new QName("OfflineFlag"));
                              if (offlineFlagAttribute != null) {
                            	  	tg.setOFLN_FLG(offlineFlagAttribute.getValue());
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
                             
                              break;
                          case "CurrencyCode":
                              nextEvent = reader.nextEvent();
                              currencyCode = nextEvent.asCharacters().getData();
                              //VAL
                              //transactionBuilder.currencyCode(currencyCode);
                              //KK
                              tg.setCURCY_CODE(currencyCode);
                              
                              int hours = getHours(retailStoreId, currencyCode);
                              
                             
                              LocalDateTime updatedBeginDateTime = parsedBeginDateTime.minusHours(hours);                          
                              String businessDayDateString = updatedBeginDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);                          
                              LocalDate businessDayDate = LocalDate.parse(businessDayDateString, DateTimeFormatter.ISO_LOCAL_DATE);
                              //businessDayDate = LocalDate.parse(businessDayDateString, DateTimeFormatter.ISO_LOCAL_DATE);
                              // VAL transactionBuilder.businessDayDate(businessDayDate);
                                                   
                              tg.setBUS_DAY(businessDayDate);                                               
                              break;  
                              
                          case "RetailStoreID":
                              nextEvent = reader.nextEvent();
                              retailStoreId = nextEvent.asCharacters().getData();
                              //transactionBuilder.retailStoreID(retailStoreId);
                              tg.setSTO_NO(retailStoreId);
                              break;
                          case "WorkstationID":
                              nextEvent = reader.nextEvent();
                              //transactionBuilder.workstationID(Integer.valueOf(nextEvent.asCharacters()
                              //        .getData()));
                              tg.setWS_ID(Integer.valueOf(nextEvent.asCharacters().getData()));
                              break;
                          case "SequenceNumber":
                              nextEvent = reader.nextEvent();
                              String sequenceNumber = nextEvent.asCharacters().getData();
                              if (isTax) {
                                  //taxBuilder.sequenceNumber(Integer.valueOf(sequenceNumber));
                            	  tg.setTRA_SEQ_NO(Integer.valueOf(sequenceNumber));
                              } else if (isLineItem) {
                                  //lineItemBuilder.sequenceNumber(Integer.valueOf(sequenceNumber));
                            	  //TODO: Here put for the LINEITEM
                              } else if (isTransaction
                                      && startElementName.getNamespaceURI().equals(IXRETAIL_NAMESPACE)
                                      && startElementName.getPrefix().isEmpty())  {
                                 // transactionBuilder.sequenceNumber(Integer.valueOf(sequenceNumber));
                            	  tg.setTRA_SEQ_NO(Integer.valueOf(sequenceNumber));
                              }
                              break;   
                              

                         // case "BeginDateTime":
                         //     nextEvent = reader.nextEvent();
                         //     String beginDateTime = nextEvent.asCharacters().getData();
                         //     parsedBeginDateTime = LocalDateTime.parse(beginDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                         //     //parsedBeginDateTime = LocalDateTime.parse(beginDateTime);
                         //     transactionBuilder.beginDateTime(parsedBeginDateTime);
                         //     break;

                         // case "CurrencyCode":
                         //     nextEvent = reader.nextEvent();
                         //     currencyCode = nextEvent.asCharacters().getData();
                         //     transactionBuilder.currencyCode(currencyCode);
                         //     int hours = getHours(retailStoreId, currencyCode);
                         //     LocalDateTime updatedBeginDateTime = parsedBeginDateTime.minusHours(hours);
                         //     String businessDayDateString = updatedBeginDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
                         //     LocalDate businessDayDate = LocalDate.parse(businessDayDateString, DateTimeFormatter.ISO_LOCAL_DATE);
                         //     transactionBuilder.businessDayDate(businessDayDate);
                         //     break;

                          case "TillID":
                              nextEvent = reader.nextEvent();
                              //transactionBuilder.tillId(nextEvent.asCharacters()
                              //        .getData());
                              tg.setTILL_TYPE(nextEvent.asCharacters().getData());
                              break;

                          case "RetailTransaction":
                              //VALretailTransactionBuilder = RetailTransaction.builder();
                              Attribute transactionStatusAttribute = startElement.getAttributeByName(new QName("TransactionStatus"));
                              if (transactionStatusAttribute != null) {
                                 //VAL transactionBuilder.transactionStatus(transactionStatusAttribute.getValue());
                              }
                              break;

                          case "LineItem":
                              isLineItem = true;
                              //VAL lineItemBuilder = LineItem.builder();
                              Attribute voidFlagAttribute = startElement.getAttributeByName(new QName("VoidFlag"));
                              if (voidFlagAttribute != null) {
                                  //VAL lineItemBuilder.voidFlag(voidFlagAttribute.getValue());
                              }
                              Attribute entryMethodAttribute = startElement.getAttributeByName(new QName("EntryMethod"));
                              if (entryMethodAttribute != null) {
                                  //VAL lineItemBuilder.voidFlag(entryMethodAttribute.getValue());
                              }
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

              }
              
              //output.put("transactionlist", tg_list);
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