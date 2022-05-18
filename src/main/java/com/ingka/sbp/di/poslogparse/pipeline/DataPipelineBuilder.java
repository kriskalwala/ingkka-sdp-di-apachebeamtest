package com.ingka.sbp.di.poslogparse.pipeline;


import java.io.IOException;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.DoFn.Element;
import org.apache.beam.sdk.transforms.DoFn.OutputReceiver;
import org.apache.beam.sdk.transforms.DoFn.ProcessElement;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Duration;

//import com.google.pubsub.v1.PubsubMessage;  // to moze byc nie OK!!! daltego ponizej
//import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import com.ingka.sbp.di.poslogparse.transform.DeserializedPayload;

import jdk.internal.org.jline.utils.Log;


public class DataPipelineBuilder {
	
	//GOOD DataflowPipelineOptions dataflowPipeLineOptions = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
	
	
	public Pipeline createDataPipeline(DataPipelineOptions options) {
	//public Pipeline createDataPipeline(DataflowPipelineOptions options) {
		
		final Pipeline pipeline = Pipeline.create(options);    //create(options);
		
		String runner = pipeline.getOptions().getRunner().toString();
		
		Log.info("Pipeline runner {}", runner);
		PCollection<PubsubMessage> pubsubMessagePCollection = pipeline.apply(PubsubIO.readMessages()
			//ver   	.fromSubscription(options.getInputSubscription()));  //fromSubscription(options.getInputSubscription()));
		        .fromSubscription("cxcxcxcxc"));
				
		
		
		//PCollection<String> deserializedPayloadCollection = pubsubMessagePCollection.apply("Deser...", ParDo.of(new DeserializedPayload()));
		
		
		PCollection<String> deserializedPayloadCollection = pubsubMessagePCollection.apply("Deser...", ParDo.of(new DoFn<PubsubMessage, String>() {
            @ProcessElement
            public void processElement(@Element PubsubMessage pubsubMessage, OutputReceiver<String> receiver) {
                String element = new String(pubsubMessage.getPayload());
                receiver.output(element);
            }}));    
		
		
		
	/*	new DoFn<PubsubMessage, String>() {
            @ProcessElement
            public void processElement(@Element PubsubMessage pubsubMessage, OutputReceiver<String> receiver) {
                String element = new String(pubsubMessage.getPayload());
                receiver.output(element);
            } */
		    	  
		Log.info("End running the Pipeline!");    	  
		
		return pipeline;
		
		
	}


}
