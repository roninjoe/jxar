package jxar.xar.toc;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class TocParser {

	
	public static List<Item> staxParseToc(String tocXml) throws Exception {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		List<Item> items = new ArrayList<Item>();
		int numFiles = 0;
		
		XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(tocXml));
		
		int event = reader.next();
		while (reader.hasNext()) {
			switch(event) {
			case XMLStreamConstants.START_ELEMENT:
				if ("file".equals(reader.getLocalName())) {
					numFiles++;
					items.add(new Item(reader));
				}	
				break;
			}
			event = reader.next();
		}
		return items;
	}
}