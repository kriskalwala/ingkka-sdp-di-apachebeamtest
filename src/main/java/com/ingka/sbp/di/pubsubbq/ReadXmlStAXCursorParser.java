package com.ingka.sbp.di.pubsubbq;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.*;

import com.google.api.services.pubsub.model.PubsubMessage;

public class ReadXmlStAXCursorParser {

    private static final String FILENAME = "src/test/java/staff.xml";

    public static void main(String[] args) {
    	
    	String sss ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
    			+ "<company>\n"
    			+ "    <staff id=\"1001\">\n"
    			+ "        <name>mkyong</name>\n"
    			+ "        <role>support</role>\n"
    			+ "        <salary currency=\"USD\">5000</salary>\n"
    			+ "        <!-- for special characters like < &, need CDATA -->\n"
    			+ "        <bio><![CDATA[HTML tag <code>testing</code>]]></bio>\n"
    			+ "    </staff>\n"
    			+ "    <staff id=\"1002\">\n"
    			+ "        <name>yflow</name>\n"
    			+ "        <role>admin</role>\n"
    			+ "        <salary currency=\"EUR\">8000</salary>\n"
    			+ "        <bio><![CDATA[a & b]]></bio>\n"
    			+ "    </staff>\n"
    			+ "    <transaction>\n"
    			+ "       <sale_item>soul</sale_item>\n"
    			+ "    </transaction>\n"
    			+ "</company>";

        try {

        	System.out.println("from MAIN : " + printXmlByXmlCursorReaderString(sss)); 

        } 
        //catch (FileNotFoundException | XMLStreamException e) {
        catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }
    
    
    public void givenUsingPlainJava_whenConvertingStringToInputStream_thenCorrect() 
    		  throws IOException {
    		    String initialString = "text";
    		    InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
    		}
    
   /* public static Stream GenerateStreamFromString(string s)
    {
        var stream = new MemoryStream();
        var writer = new StreamWriter(stream);
        writer.Write(s);
        writer.Flush();
        stream.Position = 0;
        return stream;
    }*/
    
    
    private static String printXmlByXmlCursorReaderString(String element) throws XMLStreamException {
		 XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
	     //   XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(
	     //           new FileInputStream(path.toFile()));
	        
	        String salary_text = "S ";
	        
	        //String pubsubHERE= pubsubMessage.getData();
	        
	        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(element.getBytes()));

	        int eventType = reader.getEventType();
	        System.out.println(eventType);   // 7, START_DOCUMENT
	        System.out.println(reader);      // xerces

	        while (reader.hasNext()) {

	            eventType = reader.next();

	            if (eventType == XMLEvent.START_ELEMENT) {

	                switch (reader.getName().getLocalPart()) {

	                    case "staff":
	                        String id = reader.getAttributeValue(null, "id");
	                        System.out.printf("Staff id : %s%n", id);
	                        break;

	                    case "name":
	                        eventType = reader.next();
	                        if (eventType == XMLEvent.CHARACTERS) {
	                            System.out.printf("Name : %s%n", reader.getText());
	                        }
	                        break;

	                    case "role":
	                        eventType = reader.next();
	                        if (eventType == XMLEvent.CHARACTERS) {
	                            System.out.printf("Role : %s%n", reader.getText());
	                        }
	                        break;

	                    case "salary":
	                        String currency = reader.getAttributeValue(null, "currency");
	                        eventType = reader.next();
	                        salary_text = reader.getText();;
	                        if (eventType == XMLEvent.CHARACTERS) {
	                            String salary = reader.getText();
	                            System.out.printf("Salary [Currency] : %,.2f [%s]%n",
	                              Float.parseFloat(salary), currency);
	                        }
	                        break;

	                    case "bio":
	                        eventType = reader.next();
	                        if (eventType == XMLEvent.CHARACTERS) {
	                            System.out.printf("Bio : %s%n", reader.getText());
	                        }
	                        break;
	                }

	            }

	            if (eventType == XMLEvent.END_ELEMENT) {
	                // if </staff>
	                if (reader.getName().getLocalPart().equals("staff")) {
	                    System.out.printf("%n%s%n%n", "---");
	                }
	            }

	        }
			return salary_text;
	} 
	
	

    

    private static String printXmlByXmlCursorReader(PubsubMessage pubsubMessage)
            throws FileNotFoundException, XMLStreamException {

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
     //   XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(
     //           new FileInputStream(path.toFile()));
        
        String salary_text = "S ";
        
        String pubsubHERE= pubsubMessage.getData();
        
        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(pubsubHERE.getBytes()));

        int eventType = reader.getEventType();
        System.out.println(eventType);   // 7, START_DOCUMENT
        System.out.println(reader);      // xerces

        while (reader.hasNext()) {

            eventType = reader.next();

            if (eventType == XMLEvent.START_ELEMENT) {

                switch (reader.getName().getLocalPart()) {

                    case "staff":
                        String id = reader.getAttributeValue(null, "id");
                        System.out.printf("Staff id : %s%n", id);
                        break;

                    case "name":
                        eventType = reader.next();
                        if (eventType == XMLEvent.CHARACTERS) {
                            System.out.printf("Name : %s%n", reader.getText());
                        }
                        break;

                    case "role":
                        eventType = reader.next();
                        if (eventType == XMLEvent.CHARACTERS) {
                            System.out.printf("Role : %s%n", reader.getText());
                        }
                        break;

                    case "salary":
                        String currency = reader.getAttributeValue(null, "currency");
                        eventType = reader.next();
                        salary_text = reader.getText();;
                        if (eventType == XMLEvent.CHARACTERS) {
                            String salary = reader.getText();
                            System.out.printf("Salary [Currency] : %,.2f [%s]%n",
                              Float.parseFloat(salary), currency);
                        }
                        break;

                    case "bio":
                        eventType = reader.next();
                        if (eventType == XMLEvent.CHARACTERS) {
                            System.out.printf("Bio : %s%n", reader.getText());
                        }
                        break;
                }

            }

            if (eventType == XMLEvent.END_ELEMENT) {
                // if </staff>
                if (reader.getName().getLocalPart().equals("staff")) {
                    System.out.printf("%n%s%n%n", "---");
                }
            }

        }
		return salary_text;

    }

}