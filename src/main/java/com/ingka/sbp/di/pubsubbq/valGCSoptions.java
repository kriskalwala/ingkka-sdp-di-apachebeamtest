package com.ingka.sbp.di.pubsubbq;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.ValueProvider;

public interface valGCSoptions extends PipelineOptions {
    public static final String output = "";
	
    ValueProvider<Integer> getInt();
    void setInt(ValueProvider<Integer> value);
    
    @Default.String("gs://pubsubbb/tmp")
    ValueProvider<String> getString();
    void setString(ValueProvider<String> value);
    
    
}