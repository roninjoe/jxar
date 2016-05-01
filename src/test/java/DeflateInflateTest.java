import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.junit.Test;

public class DeflateInflateTest {

	@Test
	public void canDeflateInflateMultiple() throws Exception {
		File file = new File("bin/outFile");
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		DeflaterOutputStream dos = new DeflaterOutputStream(bos);
		dos.write("abcd".getBytes());  // get bytes as platform default
		dos.close();
		
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		InflaterInputStream iis = new InflaterInputStream(bis);
		int oneByte;
		StringBuffer  sb = new StringBuffer();
		while ((oneByte = iis.read()) != -1) {
			System.out.println(oneByte);
			sb.append(Character.toChars(oneByte));
		}
		iis.close();
		System.out.println(sb.toString());
		
		assertEquals("Should be equal",sb.toString(), "abcd");
	}
}
