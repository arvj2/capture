package com.claro.cfc.oss.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/30/2014.
 */
public final class TaskResponseParser {

    public static final GenericTaskExecutionResult toResult(String message) {
        GenericTaskExecutionResult result = new GenericTaskExecutionResult();

        SAXParserFactory fact = SAXParserFactory.newInstance();
        try {
            fact.setNamespaceAware(true);
            SAXParser parser = fact.newSAXParser();
            parser.parse(new ReaderInputStream(new StringReader(message.trim())),new TaskResultHandler(result));
        }catch ( Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }

    private static class TaskResultHandler extends DefaultHandler {
        private GenericTaskExecutionResult result;
        private String current;
        private String name = "";

        public TaskResultHandler(GenericTaskExecutionResult result) {
            this.result = result;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            name = localName;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (localName.toLowerCase().contains("success")) {
                result.success(new Boolean(current));
            } else if (localName.toLowerCase().contains("errortype")) {
                result.setErrorType(current);
            } else if (localName.toLowerCase().contains("errormessage")) {
                result.setMessage(current);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (name.toLowerCase().contains("success")) {
                current = new String(ch,start,length);
            } else if (name.toLowerCase().contains("errortype")) {
                current = new String(ch,start,length);
            } else if (name.toLowerCase().contains("errormessage")) {
                current = new String(ch,start,length);
            }
        }
    }
}
