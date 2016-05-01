package jxar;

import java.io.File;

import jxar.xar.Xar;

public class JxarDriver {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: java -jar jxar-<release>.jar <pathToXarFile> <outputDir>");
			System.exit(-1);
		}
		if (Xar.isXar(new File(args[0]))) {
			Xar xar = new Xar(new File(args[0]));
			xar.extract(new File(args[1]));
		} else {
			System.out.println("That's not a xar.");
		}
	}
}