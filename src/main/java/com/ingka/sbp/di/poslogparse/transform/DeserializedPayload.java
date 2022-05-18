package com.ingka.sbp.di.poslogparse.transform;

import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.DoFn.ProcessElement;

public class DeserializedPayload extends DoFn<PubsubMessage, String>{
	
	
	@ProcessElement
	public void processElement(PipelineOptions pipelineOptions, 
			 @Element PubsubMessage pubsubMessage, OutputReceiver receiver)		
     {
	
	      byte[] payload = pubsubMessage.getPayload();
	      receiver.output("Success!");
	      
	      
	 }

}
