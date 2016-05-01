package jxar.util;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class ByteArrayUtils {
	
	public static long readLong(FileInputStream fis) throws Exception {
		byte[] buff = new byte[8];
		fis.read(buff,0,8);
		return byteArrayToLong(buff);
	}
	
	public static short byteArrayToShort(byte[] b) {
	    final ByteBuffer bb = ByteBuffer.wrap(b);
	    //bb.order(ByteOrder.LITTLE_ENDIAN);
	    return bb.getShort();
	}
	public static int byteArrayToInt(byte[] b) {
	    final ByteBuffer bb = ByteBuffer.wrap(b);
	    //bb.order(ByteOrder.LITTLE_ENDIAN);
	    return bb.getInt();
	}
	public static long byteArrayToLong(byte[] b) {
	    final ByteBuffer bb = ByteBuffer.wrap(b);
	    //bb.order(ByteOrder.LITTLE_ENDIAN);
	    return bb.getLong();
	}
	public static short readShort(FileInputStream fis) throws Exception {
		byte[] buff = new byte[2];
		fis.read(buff,0,2);
		return byteArrayToShort(buff);
	}

	public static int readInt(FileInputStream fis) throws Exception {
		byte[] buff = new byte[4];
		fis.read(buff,0,4);
		return byteArrayToInt(buff);
	}

}