package jxar.xar.toc;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public class Item {
	public Item parent;
	public List<Item> children = new ArrayList<Item>();
	
	public String id;
	public String type;
	public String name;
	
	int length;
	int offset;
	int size;
	
	public Item(XMLStreamReader reader) throws Exception {
		
		this.id = reader.getAttributeValue(null, "id");
		
		int event = reader.next();	
		while (reader.hasNext()) {
			switch(event) {
			case XMLStreamConstants.START_ELEMENT:
				
				if ("type".equals(reader.getLocalName())) {
					type = reader.getElementText();
				} else if ("name".equals(reader.getLocalName())) {
					name = reader.getElementText();
				} else if ("length".equals(reader.getLocalName())) {
					length = Integer.parseInt(reader.getElementText());
				} else if ("offset".equals(reader.getLocalName())) {
					offset = Integer.parseInt(reader.getElementText());
				} else if ("size".equals(reader.getLocalName())) {
					size = Integer.parseInt(reader.getElementText());			
				} else if ("file".equals(reader.getLocalName())) {
					Item child = new Item(reader);
					this.children.add(child);
					child.parent = this;
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				if ("file".equals(reader.getLocalName())) {
					return;
				}
			}
			event = reader.next();
		}
	}
	
	public void dump() {
		System.out.println(String.format("id: %s name: %s type: %s", this.id, this.name, this.type));
		for (Item child : children) {
			child.dump();
		}
	}
	public void create(FileInputStream fis, File outputDir) throws Exception {
		System.out.println(String.format("id: %s name: %s type: %s", this.id, this.name, this.type));
		File file = new File(outputDir,name);
		if ("directory".equals(this.type)) {		
			if (file.mkdir()) {
				System.out.println("Created dir " + this);
			} else {
				System.err.println("Failed to create dir " + this);
			}
			for (Item child : children) {
				child.create(fis,file);
			}
		} else {
			System.out.println("creating file " + this);
			// TODO: Deal with "extended attributes" here
			InflaterInputStream iis = new InflaterInputStream(fis);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			int oneByte;
			while ((oneByte = iis.read()) != -1) {
				bos.write(oneByte);
			}
			bos.close();
		}
	}	
	public String toString() {
		return String.format("id: %s name: %s type: %s", this.id, this.name, this.type);
	}
}