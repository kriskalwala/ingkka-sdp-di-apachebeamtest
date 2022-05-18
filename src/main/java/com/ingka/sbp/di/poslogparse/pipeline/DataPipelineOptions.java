package com.ingka.sbp.di.poslogparse.pipeline;

import org.apache.beam.sdk.options.*;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public interface DataPipelineOptions extends PipelineOptions {

    static DataPipelineOptions configureFromSystemProperties() {
        Properties props = System.getProperties();
        List<String> list = props.entrySet().stream()
                .map(entry -> entry.getKey().toString() + "=" + entry.getValue().toString())
                .filter(s -> s.startsWith("--")).collect(Collectors.toList());
        String[] args = list.toArray(new String[]{});
        System.out.println("Input arguments: " + Arrays.toString(args));

        return PipelineOptionsFactory.fromArgs(args).as(DataPipelineOptions.class);
    }

    /*
     * GCP Project Id
     */
    @Description("The GCP project id")
    @Default.String("ikea-analytics-data-poc")
    String getProjectId();
    void setProjectId(String value);

//    //@Default.String("europe-west1")
//    String getRegion();
//    void setRegion(String value);

    @Description(
            "The Cloud Pub/Sub subscription to consume from. "
                    + "The name should be in the format of "
                    + "projects/<project-id>/subscriptions/<subscription-name>.")
    @Validation.Required
    @Default.String("projects/ikea-analytics-data-poc/subscriptions/ctm-poslog-sub-prod")
    String getInputSubscription();

    void setInputSubscription(String inputSubscription);
}