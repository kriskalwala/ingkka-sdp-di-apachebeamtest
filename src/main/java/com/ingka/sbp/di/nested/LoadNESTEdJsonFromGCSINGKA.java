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
public class LoadNESTEdJsonFromGCSINGKA {

  private static final Field F_addresses2 = Field.newBuilder( "addresses2",
                                StandardSQLTypeName.STRUCT,
                                Field.of("status2", StandardSQLTypeName.STRING),
                                Field.of("address2", StandardSQLTypeName.STRING))
                        .setMode(Mode.REPEATED).build();
  
  private static final Field F_organizationHierarchy = Field.newBuilder( "organizationHierarchy",
						         StandardSQLTypeName.STRUCT,
						         Field.of("value", StandardSQLTypeName.STRING),
	                             Field.of("id", StandardSQLTypeName.STRING),
	                             Field.of("level", StandardSQLTypeName.STRING))
						.setMode(Mode.REPEATED).build();
  
  
  private static final Field F_operatorID = Field.newBuilder( "operatorID",
						         StandardSQLTypeName.STRUCT,
						         Field.of("value", StandardSQLTypeName.NUMERIC),
					             Field.of("operatorName", StandardSQLTypeName.STRING),
					             Field.of("operatorType", StandardSQLTypeName.STRING))
						 .setMode(Mode.REPEATED).build();
  
  
  
  
 
  
  private static final Field F_retailTransaction = Field.newBuilder( "retailTransaction",
	         StandardSQLTypeName.STRUCT,
					         Field.of("loyaltyAccount", StandardSQLTypeName.STRING),
				             Field.of("transactionLink", StandardSQLTypeName.STRING),
				             Field.of("operatorBypassApproval", StandardSQLTypeName.STRING),
				             Field.of("coupon", StandardSQLTypeName.STRING),
				             Field.of("transactionSpecifics", StandardSQLTypeName.STRING),
				             Field.of("transactionStatus", StandardSQLTypeName.STRING),
				             Field.of("version", StandardSQLTypeName.NUMERIC))
					 .setMode(Mode.REPEATED).build();		
  

public static void runLoadJsonFromGCS() {
    // TODO(developer): Replace these variables before running the sample.
   // String datasetName = "MY_DATASET_NAME";
   // String tableName = "MY_TABLE_NAME";
    String datasetName = "smalltech";
    String tableName = "json4nestnestF1partly7";
   // String sourceUri = "gs://cloud-samples-data/bigquery/us-states/us-states.json";
   // String sourceUri = "gs://bucket-20apr/adr.json";
   // String sourceUri = "gs://bucket-20apr/adr_nest_nest.json";
   // String sourceUri = "gs://bucket-20apr/VALjson1line.json";
    String sourceUri = "gs://bucket-20apr/partlyJSONline.json";
    
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
    
    
    Schema schemaIngka =
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
                        F_addresses2,
                        Field.of("numberOfYears", StandardSQLTypeName.STRING))
                    .setMode(Mode.REPEATED)
                    .build());
    
    
    Schema schemaIngkaJSONold =
            Schema.of(
                Field.newBuilder(
                        "transaction",
                        StandardSQLTypeName.STRUCT,
                        Field.of("retailStoreID", StandardSQLTypeName.STRING),
                      //  F_bussinessUnit,
                      //  
                        Field.of("workstationID", StandardSQLTypeName.STRING), //n
                        Field.of("tillID", StandardSQLTypeName.STRING),
                        Field.of("sequenceNumber", StandardSQLTypeName.STRING), //n
                        Field.of("businessDayDate", StandardSQLTypeName.STRING), //n 
                        Field.of("beginDateTime", StandardSQLTypeName.STRING), //n
                      //  F_operatorID,
                        Field.of("currencyCode", StandardSQLTypeName.STRING),
                        Field.of("controlTransaction", StandardSQLTypeName.STRING),
                        Field.of("tenderControlTransaction", StandardSQLTypeName.STRING))
                      //  F_retailTransaction,
                      //  Field.of("numberOfYears", StandardSQLTypeName.STRING))
                    .setMode(Mode.REPEATED)
                    .build());
                // Field.of("poslogSpecifics", StandardSQLTypeName.STRING)
            ;
            
            //5 is working
            Schema schemaIngkaJSON =
                    Schema.of(
                        Field.newBuilder(
                                "transaction",
                                StandardSQLTypeName.STRUCT,
                                Field.of("retailStoreID", StandardSQLTypeName.STRING), 
                                F_organizationHierarchy,
                                Field.of("workstationID", StandardSQLTypeName.NUMERIC), //n
                                Field.of("tillID", StandardSQLTypeName.STRING),
                                Field.of("sequenceNumber", StandardSQLTypeName.NUMERIC), //n
                                Field.of("businessDayDate", StandardSQLTypeName.NUMERIC), //n 
                                Field.of("beginDateTime", StandardSQLTypeName.NUMERIC), //n
                                Field.of("endDateTime", StandardSQLTypeName.NUMERIC), //n
                                F_operatorID,
                                Field.of("currencyCode", StandardSQLTypeName.STRING),
                                Field.of("controlTransaction", StandardSQLTypeName.STRING),
                                Field.of("tenderControlTransaction", StandardSQLTypeName.STRING))
                                //F_retailTransaction,
                            .setMode(Mode.REPEATED)
                            .build(),
                         Field.of("poslogSpecifics", StandardSQLTypeName.STRING));
            //7        
            Schema schemaIngkaJSON10 =
                    Schema.of(
                        Field.newBuilder(
                                "transaction",
                                StandardSQLTypeName.STRUCT,
                                Field.of("retailStoreID", StandardSQLTypeName.STRING), 
                                F_organizationHierarchy,
                                Field.of("workstationID", StandardSQLTypeName.NUMERIC), //n
                                Field.of("tillID", StandardSQLTypeName.STRING),
                                Field.of("sequenceNumber", StandardSQLTypeName.NUMERIC), //n
                                Field.of("businessDayDate", StandardSQLTypeName.NUMERIC), //n 
                                Field.of("beginDateTime", StandardSQLTypeName.NUMERIC), //n
                                Field.of("endDateTime", StandardSQLTypeName.NUMERIC), //n
                                F_operatorID,
                                Field.of("currencyCode", StandardSQLTypeName.STRING),
                                Field.of("controlTransaction", StandardSQLTypeName.STRING),
                                Field.of("tenderControlTransaction", StandardSQLTypeName.STRING),
                                F_retailTransaction)
                            .setMode(Mode.REPEATED)
                            .build(),
                         Field.of("poslogSpecifics", StandardSQLTypeName.STRING));
            
            //working set
            //   {"transaction":[{"retailStoreID":"985","organizationHierarchy":[{"value":"IKEASetCountryCode2PT","id":"PT","level":"Corporation"}],"workstationID":10,"tillID":"RETURN","sequenceNumber":10,"businessDayDate":1654034400000,"beginDateTime":1654073055000,"endDateTime":1654073073000,"operatorID":{"value":16162,"operatorName":"Agneta Cashier","operatorType":"IKEA-SAP:HFB"},"currencyCode":"EUR","controlTransaction":"null","tenderControlTransaction":"null","retailTransaction":[{"loyaltyAccount":"null","transactionLink":"null","operatorBypassApproval":"x","coupon":"x","transactionSpecifics":"null","transactionStatus":"null","version":2.2}]}],"poslogSpecifics":"null"}

            
            //8        
            Schema schemaIngkaJSON11 =
                    Schema.of(
                        Field.newBuilder(
                                "transaction",
                                StandardSQLTypeName.STRUCT,
                                Field.of("retailStoreID", StandardSQLTypeName.STRING), 
                                F_organizationHierarchy,
                                Field.of("workstationID", StandardSQLTypeName.NUMERIC), //n
                                Field.of("tillID", StandardSQLTypeName.STRING),
                                Field.of("sequenceNumber", StandardSQLTypeName.NUMERIC), //n
                                Field.of("businessDayDate", StandardSQLTypeName.NUMERIC), //n 
                                Field.of("beginDateTime", StandardSQLTypeName.NUMERIC), //n
                                Field.of("endDateTime", StandardSQLTypeName.NUMERIC), //n
                                F_operatorID,
                                Field.of("currencyCode", StandardSQLTypeName.STRING),
                                Field.of("controlTransaction", StandardSQLTypeName.STRING),
                                Field.of("tenderControlTransaction", StandardSQLTypeName.STRING),
                                F_retailTransaction)
                            .setMode(Mode.REPEATED)
                            .build(),
                         Field.of("poslogSpecifics", StandardSQLTypeName.STRING));
    
    
    loadJsonFromGCS(datasetName, tableName, sourceUri, schemaIngkaJSON10);  //switch schemas
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