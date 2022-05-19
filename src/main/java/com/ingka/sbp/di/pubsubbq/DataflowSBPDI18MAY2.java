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

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

import com.ingka.sbp.di.pubsubbq.ReadXmlStAXCursorParser;

public class DataflowSBPDI18MAY2 {
	
	
	public static void main(String[] args) {
		
		valGCSoptions options =
		          PipelineOptionsFactory.fromArgs(args).withValidation()
		            .as(valGCSoptions.class);
		
		DataflowPipelineOptions dataflowPipeLineOptions = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
		dataflowPipeLineOptions.setJobName("StreamingIngestion18MAYAB2");
		dataflowPipeLineOptions.setProject("cpskk2021-03-1615568275864");
		
		dataflowPipeLineOptions.setRegion("us-central1");
	
		
		dataflowPipeLineOptions.setGcpTempLocation("gs://pubsubbb/tmp");
		options.setTempLocation("gs://pubsubbb/tmp");
		
		
		dataflowPipeLineOptions.setRunner(DataflowRunner.class);
		
		Pipeline pipeline = Pipeline.create(dataflowPipeLineOptions);
		
		//ORG
		//
		PCollection<String>  pubsubmessage = pipeline.apply(PubsubIO.readStrings().fromTopic("projects/cpskk2021-03-1615568275864/topics/pubsubbqmay2022")); 
		
		
		PCollection<TableRow> bqrow =  pubsubmessage.apply( ParDo.of(new ConvertStringBqA()) );
		bqrow.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.pubsubStream7A").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
				.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER));
				//.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER).withCustomGcsTempLocation(options.getString()));    IT WORKED !!!
	
		
		
		PCollection<TableRow> bqrow2 =  pubsubmessage.apply( ParDo.of(new ConvertStringBqB()) );
		bqrow.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.pubsubStream7B").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
				.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER));
		
		
		pipeline.run();
		
	}
	
	
	public static class ConvertStringBqA extends DoFn<String, TableRow> {
		
		@ProcessElement
		public void processing(ProcessContext processContext) throws XMLStreamException {
			
			
			//parse XML
			
			String parsed = printXmlByXmlCursorReader(processContext.element());
			
			
			TableRow tableRow = new TableRow().set("message", processContext.element().toString())
					.set("messageid", processContext.element().toString()+":"+processContext.timestamp().toString()+ "parsed-" + parsed)
					.set("messageprocessingtime", processContext.timestamp().toString());
			
			
			
			processContext.output(tableRow);
		}

		private String printXmlByXmlCursorReader(String element) throws XMLStreamException {
			 XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		     //   XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(
		     //           new FileInputStream(path.toFile()));
		        
		        String salary_text = "S ";
		        
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
				return salary_text + "_KKA";
		} 
		
		
	}
	
	
	
public static class ConvertStringBqB extends DoFn<String, TableRow> {
		
		@ProcessElement
		public void processing(ProcessContext processContext) throws XMLStreamException {
			
			
			//parse XML
			
			String parsed = printXmlByXmlCursorReaderB(processContext.element());
			
			
			TableRow tableRow = new TableRow().set("message", processContext.element().toString())
					.set("messageid", processContext.element().toString()+":"+processContext.timestamp().toString()+ "parsed-" + parsed)
					.set("messageprocessingtime", processContext.timestamp().toString());
			
			
			
			processContext.output(tableRow);
		}

		private String printXmlByXmlCursorReaderB(String element) throws XMLStreamException {
			 XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		     //   XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(
		     //           new FileInputStream(path.toFile()));
		        
		        String salary_text = "S ";
		        
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
				return salary_text + "_KKB";
		} 
		
		
	}

}

