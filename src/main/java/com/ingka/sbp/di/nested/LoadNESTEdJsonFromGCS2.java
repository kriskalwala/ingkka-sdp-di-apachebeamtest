package com.ingka.sbp.di.nested;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.FormatOptions;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.LoadJobConfiguration;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardSQLTypeName;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.Field.Mode;

// Sample to load JSON data from Cloud Storage into a new BigQuery table
public class LoadNESTEdJsonFromGCS2 {

  public static void runLoadJsonFromGCS() {
    // TODO(developer): Replace these variables before running the sample.
   // String datasetName = "MY_DATASET_NAME";
   // String tableName = "MY_TABLE_NAME";
    String datasetName = "smalltech";
    String tableName = "json4nestnest";
   // String sourceUri = "gs://cloud-samples-data/bigquery/us-states/us-states.json";
   // String sourceUri = "gs://bucket-20apr/adr.json";
   // String sourceUri = "gs://bucket-20apr/adr_nest_nest.json";
    String sourceUri = "gs://bucket-20apr/adr_nest_nest.json";
    
    /*Schema schema =
        Schema.of(
            Field.of("name", StandardSQLTypeName.STRING),
            Field.of("post_abbr", StandardSQLTypeName.STRING));*/
    Schema schema =
            Schema.of(
                Field.of("id", StandardSQLTypeName.STRING),
                Field.of("first_name", StandardSQLTypeName.STRING),
                Field.of("last_name", StandardSQLTypeName.STRING),
                Field.of("dob", StandardSQLTypeName.DATE),
                // create the nested and repeated field
                Field.newBuilder(
                        "addresses",
                        StandardSQLTypeName.STRUCT,
                        Field.of("status", StandardSQLTypeName.STRING),
                        Field.of("address", StandardSQLTypeName.STRING),
                        Field.of("city", StandardSQLTypeName.STRING),
                        Field.of("state", StandardSQLTypeName.STRING),
                        Field.of("zip", StandardSQLTypeName.STRING),
                        Field.of("numberOfYears", StandardSQLTypeName.STRING))
                    .setMode(Mode.REPEATED)
                    .build());
    
    Schema schema2 =
            Schema.of(
                Field.of("id", StandardSQLTypeName.STRING),
                Field.of("first_name", StandardSQLTypeName.STRING),
                Field.of("last_name", StandardSQLTypeName.STRING),
                Field.of("dob", StandardSQLTypeName.DATE),
                // create the nested and repeated field
                Field.newBuilder(
                        "addresses",
                        StandardSQLTypeName.STRUCT,
                        Field.of("status", StandardSQLTypeName.STRING),
                        Field.of("address", StandardSQLTypeName.STRING),
                        Field.of("city", StandardSQLTypeName.STRING),
                        Field.of("state", StandardSQLTypeName.STRING),
                        Field.of("zip", StandardSQLTypeName.STRING),
                        Field.newBuilder( "addresses2",
                                StandardSQLTypeName.STRUCT,
                                Field.of("status2", StandardSQLTypeName.STRING),
                                Field.of("address2", StandardSQLTypeName.STRING))
                        .setMode(Mode.REPEATED).build(),
                        Field.of("numberOfYears", StandardSQLTypeName.STRING))
                    .setMode(Mode.REPEATED)
                    .build());
    
    loadJsonFromGCS(datasetName, tableName, sourceUri, schema2);  //switch schemas
  }

  public static void loadJsonFromGCS(
      String datasetName, String tableName, String sourceUri, Schema schema) {
    try {
      // Initialize client that will be used to send requests. This client only needs to be created
      // once, and can be reused for multiple requests.
      BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

      TableId tableId = TableId.of(datasetName, tableName);
      LoadJobConfiguration loadConfig =
          LoadJobConfiguration.newBuilder(tableId, sourceUri)
              .setFormatOptions(FormatOptions.json())
              .setSchema(schema)
              .build();

      // Load data from a GCS JSON file into the table
      Job job = bigquery.create(JobInfo.of(loadConfig));
      // Blocks until this load table job completes its execution, either failing or succeeding.
      job = job.waitFor();
      if (job.isDone()) {
        System.out.println("Json from GCS successfully loaded in a table");
      } else {
        System.out.println(
            "BigQuery was unable to load into the table due to an error:"
                + job.getStatus().getError());
      }
    } catch (BigQueryException | InterruptedException e) {
      System.out.println("Column not added during load append \n" + e.toString());
    }
  }
}