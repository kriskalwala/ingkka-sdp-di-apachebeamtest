package com.ingka.sbp.di.pubsubbq;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;

public class MyPiplineJSON {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(MyPiplineJSON.class);
    private static String name;

    public static void main(String[] args) {

        List<TableFieldSchema> fields = new ArrayList<>();
        fields.add(new TableFieldSchema().setName("a").setType("string"));
        fields.add(new TableFieldSchema().setName("b").setType("string"));
        fields.add(new TableFieldSchema().setName("c").setType("string"));
        TableSchema tableSchema = new TableSchema().setFields(fields);

        DataflowPipelineOptions options = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
      //  options.setRunner(BlockingDataflowPipelineRunner.class);
        options.setProject("my-data-analysis");
        options.setStagingLocation("gs://my-bucket/dataflow-jars");
        options.setStreaming(true);

        Pipeline pipeline = Pipeline.create(options);

        PCollection<String> input = pipeline
                .apply(PubsubIO.Read.subscription(
                        "projects/my-data-analysis/subscriptions/myDataflowSub"));

        input.apply(ParDo.of(new DoFn<String, Void>() {

            //@Override
            public void processElement(DoFn<String, Void>.ProcessContext c) throws Exception {
                LOG.info("json" + c.element());
            }

        }));
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");


        input.apply(ParDo.of(new DoFn<String, String>() {
            //@Override
            public void processElement(DoFn<String, String>.ProcessContext c) throws Exception {
                JSONObject firstJSONObject = new JSONObject(c.element());
                firstJSONObject.put("a", firstJSONObject.get("a").toString()+ "1000");
                c.output(firstJSONObject.toString());

            }

        }).named("update json")).apply(ParDo.of(new DoFn<String, TableRow>() {

            //@Override
            public void processElement(DoFn<String, TableRow>.ProcessContext c) throws Exception {
                JSONObject json = new JSONObject(c.element());
                TableRow row = new TableRow().set("a", json.get("a")).set("b", json.get("b")).set("c", json.get("c"));
                c.output(row);
            }

        }).named("convert json to table row"))
                .apply(BigQueryIO.Write.to("my-data-analysis:mydataset.mytable").withSchema(tableSchema)
        );

        pipeline.run();
    }
}