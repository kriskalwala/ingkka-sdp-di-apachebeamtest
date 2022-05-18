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

import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;



public class DataflowSBPDI1 {
	
	
	public static void main(String[] args) {
		
		valGCSoptions options =
		          PipelineOptionsFactory.fromArgs(args).withValidation()
		            .as(valGCSoptions.class);
		
		DataflowPipelineOptions dataflowPipeLineOptions = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
		dataflowPipeLineOptions.setJobName("StreamingIngestion17MAYA");
		dataflowPipeLineOptions.setProject("cpskk2021-03-1615568275864");
		//dataflowPipeLineOptions.setRegion("australia-southeast");
		
		dataflowPipeLineOptions.setRegion("us-central1");
		//dataflowPipeLineOptions.setGcpTempLocation("gs://smalltech1/tmp");
		
		//dataflowPipeLineOptions.setGcpTempLocation("gs://bucket-20apr");
		
		
		//dataflowPipeLineOptions.setGcpTempLocation("gs://pubsubbqsubbucket/tmp");
		//dataflowPipeLineOptions.setGcpTempLocation("gs://pubsubbucket2/tmp/staging");		
		dataflowPipeLineOptions.setGcpTempLocation("gs://pubsubbb/tmp");
		options.setTempLocation("gs://pubsubbb/tmp");
		
		
		//dataflowPipeLineOptions.setGcpTempLocation("gs://pubsubbucket3/temp");	
		
		dataflowPipeLineOptions.setRunner(DataflowRunner.class);
		
		Pipeline pipeline = Pipeline.create(dataflowPipeLineOptions);
		
		//ORG
		//
		PCollection<String>  pubsubmessage = pipeline.apply(PubsubIO.readStrings().fromTopic("projects/cpskk2021-03-1615568275864/topics/pubsubbqmay2022")); 
		//NEW 
	//	PCollection<String>  pubsubmessage2 = pipeline.apply("ReadFromText",TextIO.read().from(pubsubmessage.toString().concat(" second step"))); 
	//	PCollection<String>  pubsubmessage2 = pubsubmessage.apply("ReadFromText", TextIO.read().from(pubsubmessage.toString().toLowerCase());    // apply("ReadFromText",TextIO.read().from(pubsubmessage.toString().concat(" second step"))); 
		
		
		
		/* "ComputeWordLengths",                     // the transform name
		  ParDo.of(new DoFn<String, Integer>() {    // a DoFn as an anonymous inner class instance
		      @ProcessElement
		      public void processElement(@Element String word, OutputReceiver<Integer> out) {
		        out.output(word.length());
		      }
		    }) */
		//ORG 
		PCollection<TableRow> bqrow =  pubsubmessage.apply( ParDo.of(new ConvertStringBq()) );
		//NEW
		//PCollection<TableRow> bqrow =  pubsubmessage2.apply( ParDo.of(new ConvertStringBq()) );
		
		
		bqrow.apply(BigQueryIO.writeTableRows().to("cpskk2021-03-1615568275864:smalltech.pubsubStream3").withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
				.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER));
				//.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER).withCustomGcsTempLocation(options.getString()));    TO DZIALLALO !!!
		        //.withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER).withCustomGcsTempLocation(gcsTempLocation));
		
		
		pipeline.run();
		
	}
	
	
	public static class ConvertStringBq extends DoFn<String, TableRow> {
		
		@ProcessElement
		public void processing(ProcessContext processContext) {
			TableRow tableRow = new TableRow().set("message", processContext.element().toString())
					.set("messageid", processContext.element().toString()+":"+processContext.timestamp().toString())
					.set("messageprocessingtime", processContext.timestamp().toString());
			
			
			
			processContext.output(tableRow);
		} 
		
		
	}

}
