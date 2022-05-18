package com.ingka.sbp.di.poslogparse.xml;


import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public interface XMLParser {
    PayLoadd parsePubsubMessage(byte[] xmlContent) throws IOException, XMLStreamException;
}
