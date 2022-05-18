package com.ingka.sbp.di.poslogparse.pipeline;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.beam.sdk.options.Description;
//import org.apache.beam.sdk.options.Default;
//import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
//import org.apache.beam.vendor.bytebuddy.v1_11_0.net.bytebuddy.implementation.bind.annotation.Default;

//import lombok.libs.com.zwitserloot.cmdreader.Description;

public interface DataPipelineOptions extends PipelineOptions {
	
	static DataPipelineOptions configureFromSystemProperties() {
		
		
		Properties props = System.getProperties();
		
		List<String> list = (List<String>) props.entrySet().stream()
				.map(entry -> entry.getKey().toString() + "=" + entry.getValue())
				.filter(s -> s.startsWith("--")).collect(Collectors.toList());
		String[] args = list.toArray(new String[]{});
		System.out.println("Input arguments" + Arrays.toString(args));
		
		return PipelineOptionsFactory.fromArgs(args).as(DataPipelineOptions.class);
 		
	}
	
	
	/*
	 * GCP Project ID
	 * 
	 * */
	@Description("The GCP Project ID")
//!!!	@Default.String("ikea-analytics-data-poc")
	String getProjectId();
	void setProjectId(String value);
	
	
	@Description(" The Cloud ")
	//!!!	@Validation.Required
	//!!!@Default.String("pubsubbbbb")
	String getInputSubscription();
	void setInputSubscription(String iputSubscription);
	

	String Log = null; // to change

}
