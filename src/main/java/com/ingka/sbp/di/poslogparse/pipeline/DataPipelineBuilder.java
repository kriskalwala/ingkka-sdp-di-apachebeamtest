package com.ingka.sbp.di.poslogparse.pipeline;

import com.ingka.sbp.di.poslogparse.transform.DeserializedPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.DoFn.Element;
import org.apache.beam.sdk.transforms.DoFn.OutputReceiver;
import org.apache.beam.sdk.transforms.DoFn.ProcessContext;
import org.apache.beam.sdk.transforms.DoFn.ProcessElement;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;


import java.io.Serializable;
import java.util.logging.Logger;

@Slf4j
public  class DataPipelineBuilder implements Serializable {

    private static final long serialVersionUID = -4653742684995510178L;
    
    static Logger log = Logger.getLogger(DataPipelineBuilder.class.getName());

    public Pipeline createDataPipeline(DataPipelineOptions options) {
        // create the Pipeline with the specified options
        final Pipeline pipeline = Pipeline.create(options);

        String runner = pipeline.getOptions().getRunner().toString();
        log.info("Pipeline runner: {}" + runner);
        PCollection<PubsubMessage> pubsubMessagePCollection = pipeline.apply(PubsubIO.readMessages()
                .fromSubscription(options.getInputSubscription()));
       // PCollection<String> deserializedPayloadCollection = pubsubMessagePCollection.apply("Deserialize Payload",
       //         ParDo.of(new DeserializedPayload()));
        
      PCollection<Integer> deserializedPayloadCollection = pubsubMessagePCollection.apply("Deserialize Payload",
                 ParDo.of(new ComputeWordLengthFn()));
        log.info("End running the pipeline!");

        return pipeline;
    }
    
    
    
    static class ComputeWordLengthFn extends DoFn<PubsubMessage, Integer> {
    	  @ProcessElement
    	  public void processElement(@Element String word, OutputReceiver<Integer> out) {
    	    // Use OutputReceiver.output to emit the output element.
    	    out.output(word.length());
    	  }
    	}
    
    
    @ProcessElement
    public void processElement(ProcessContext c) {

        //System.out.println("Payload::::"+c.element().getPayload().toString());
        //System.out.println("Bucket Id::::"+ c.element().getAttribute("bucketId"));
        //System.out.println("Bucket Id::::"+ c.element().getAttribute("objectId"));
        //String bucketName = c.element().getAttribute("bucketId");
        //String fileName = c.element().getAttribute("objectId");
        //c.output("Bucket Name::" + bucketName +"  File Name::::" + fileName);
    }
}