package com.ingka.sbp.di.nested;

/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//ORG package com.example.bigquery;

// [START bigquery_nested_repeated_schema]
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.Field.Mode;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardSQLTypeName;
import com.google.cloud.bigquery.StandardTableDefinition;
import com.google.cloud.bigquery.TableDefinition;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.TableInfo;

public class NestedRepeatedSchema2 {

  public static void runNestedRepeatedSchema() {
    // TODO(developer): Replace these variables before running the sample.
   // String datasetName = "MY_DATASET_NAME";
   // String tableName = "MY_TABLE_NAME";
    
	//HOME
    String datasetName = "smalltech";
    String tableName = "json10";
    createTableWithNestedRepeatedSchema(datasetName, tableName);
  }

  public static void createTableWithNestedRepeatedSchema(String datasetName, String tableName) {
    try {
      // Initialize client that will be used to send requests. This client only needs to be created
      // once, and can be reused for multiple requests.
      BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

      TableId tableId = TableId.of(datasetName, tableName);

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

      TableDefinition tableDefinition = StandardTableDefinition.of(schema2);
      TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();

      bigquery.create(tableInfo);
      System.out.println("Table with nested and repeated schema created successfully");
    } catch (BigQueryException e) {
      System.out.println("Table was not created. \n" + e.toString());
    }
  }
}
// [END bigquery_nested_repeated_schema]