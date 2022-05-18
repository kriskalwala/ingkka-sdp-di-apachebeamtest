package com.ingka.sbp.di.poslogparse;

import com.ingka.sbp.di.poslogparse.pipeline.DataPipelineBuilder;
import com.ingka.sbp.di.poslogparse.pipeline.DataPipelineOptions;
import com.ingka.sbp.di.poslogparse.xml.ResourceUtils;
import com.ingka.sbp.di.poslogparse.xml.StaxParser;
import com.ingka.sbp.di.poslogparse.xml.XMLParser;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

@Slf4j
public class DataPipeline {
	
	static Logger log = Logger.getLogger(DataPipeline.class.getName());
	
    public static void main(String[] args) throws IOException, XMLStreamException {
        final DataPipelineOptions options = DataPipelineOptions.configureFromSystemProperties();
        log.info("Pipeline Options: {} --- " +  options.toString()); 

//        DataPipelineBuilder dataPipelineBuilder = new DataPipelineBuilder();
//        dataPipelineBuilder.createDataPipeline(options).run();

        ResourceUtils resourceUtils = new ResourceUtils();
        byte[] bytes = {};
        try (InputStream inputStream = resourceUtils.resourceToInputStream("poslog_PT_367_20150613_1.xml")) {
            bytes = resourceUtils.getByteArrayFromInputStream(inputStream);
        }
        XMLParser staxParser = new StaxParser();
        staxParser.parsePubsubMessage(bytes);
    }
}