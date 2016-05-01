package jxar.xar;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.zip.Inflater;

import jxar.util.ByteArrayUtils;
import jxar.xar.toc.Item;
import jxar.xar.toc.TocParser;

public class Xar {
	
	File xarFile;
	FileInputStream fis; 
	
	// Header (note: all of these should be unsigned)
	public byte[] magic = new byte[4];
	public short size;  
	public short version;
	public long tocLengthCompressed;
	public long tocLengthUncompressed;
	public int chksumAlg;
	
	// Table of Contents (XML document)
	public byte[] toc;
	
	// The tree of items (directories and files)
	public List<Item> items;
	
	// Checksum (the first thing on the heap)
	public byte[] checksum; 
	
	public Xar(File f) throws Exception {
		xarFile = f;
		fis = new FileInputStream(xarFile);
		
		fis.read(magic,0,4); // magic number is "xar!"
		
		// TODO: All of these numbers should be unsigned.
		size = ByteArrayUtils.readShort(fis);
		version = ByteArrayUtils.readShort(fis);
		tocLengthCompressed = ByteArrayUtils.readLong(fis);
		tocLengthUncompressed = ByteArrayUtils.readLong(fis);
		chksumAlg = ByteArrayUtils.readInt(fis);
		
		byte[] tocBytesCompressed = new byte[(int) tocLengthCompressed];
		fis.read(tocBytesCompressed,0,(int) tocLengthCompressed);
		
		byte[] tocBytesUncompressed = new byte[(int) tocLengthUncompressed];
		Inflater inflater = new Inflater();
		inflater.setInput(tocBytesCompressed,0,(int) tocLengthCompressed);
		int resultLength = inflater.inflate(tocBytesUncompressed);
		
		String tocXml = new String(tocBytesUncompressed,0, resultLength, "UTF-8");
		items = TocParser.staxParseToc(tocXml);
		
		// Read the checksum.
		// TODO: Is checksum always 20 bytes?  (even though it is specified in the XML payload)
		// What is this checksum for, the entire file, or just the header?
		checksum = new byte[20]; 
		fis.read(checksum,0,20);
	}
	
	public  List<Item> extract(File outputDir) throws Exception {
		if (!outputDir.isDirectory()) {
			throw new Exception("Output file must be a directory");
		}
		//System.out.println(tocXml);
		for (Item item : items) {
			item.create(fis,outputDir);
		}
		return items;
	}

	public static boolean isXar(File f) throws Exception {
		boolean isXar = false;
		FileInputStream fis = new FileInputStream(f);
		byte[] magic = new byte[4];
		fis.read(magic,0,4);
		if ("xar!".equals(new String(magic))) {
			isXar = true;
		}
		fis.close();
		return isXar;
	}

	public void dump() {
		for (Item item : items) {
			item.dump();
		}
	}
	
	public String toString() {
		return String.format("Xar: magic %s, size %d, version %d",magic, size, version );
	}
}