package com.ingka.sbp.di.poslogparse;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.beam.runners.core.construction.SplittableParDo;
import org.apache.beam.runners.core.construction.graph.ProjectionPushdownOptimizer;
import org.apache.beam.runners.core.metrics.MetricsPusher;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.PipelineRunner;
import org.apache.beam.sdk.metrics.MetricsEnvironment;
import org.apache.beam.sdk.metrics.MetricsOptions;
import org.apache.beam.sdk.options.ExperimentalOptions;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsValidator;
import org.apache.beam.sdk.runners.TransformHierarchy;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.View;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.annotations.VisibleForTesting;
import org.apache.commons.logging.Log;
//import org.apache.flink.api.common.JobExecutionResult;
//import org.apache.flink.runtime.jobgraph.JobGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ingka.sbp.di.poslogparse.pipeline.DataPipelineOptions;  // to jest interface
//import com.inkga.sbp.di.poslogparse.xml.ResourceUtils;
import com.ingka.sbp.di.poslogparse.xml.ResourceUtils;


public class DataPipeline {
	
	
	public static void main(String[] args) throws IOException {
		
		final DataPipelineOptions options = DataPipelineOptions.configureFromSystemProperties();
		//Log.info("Pipeline options: {}", options.toString() );
		
		//ResourceUtils resourceUtils = new ResourceUtils();
		
		byte[] bytes = {};
		
	/*	try { InputStream inputstream = resourceUtils.resourceToInputStream(resource);
				bytes = resourceUtils.getByteArrayFromInputStream(inputstream);
			
		} 
		catch (Exception e) {
			
		} */
		
		//!!!!!XMLParser staxParser = new staxParser();
		//staxParser = new staxParser();
		//staxParser.parsePubsubMessage(bytes);
		
	}

}
