package file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapWriterReaderTest {
	String writeMap = 
			  "....\n"
			+ "....\n"
			+ "6X.C\n"
			+ "8888\n";
	
	String readMap = 
			  "4\n"
			+ "4\n"
			+ "....\n"
			+ "....\n"
			+ "6X.C\n"
			+ "8888\n";
	
	String clearFloorMap = 
			  "....\n"
			+ "....\n"
			+ "6X.C\n"
			+ "8.88\n";
	
	String missingPlayerMap = 
			  "....\n"
			+ "....\n"
			+ "6..C\n"
			+ "8888\n";
	
	String missingCoinMap = 
			  ".X..\n"
			+ "....\n"
			+ "6...\n"
			+ "8888\n";
	
	String wrongFormatMap = 
			  "X..\n"
			+ "....\n"
			+ "6...\n"
			+ "888\n";
	
	@Test
	public void testWrite() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(byteStream);
		
		MapWriterReader mapWriter = new MapWriterReader();
		mapWriter.write(writer, writeMap);
		
		String actual = new String(byteStream.toByteArray());
		String expected = mapWriter.getMapWidth() + "\n" + mapWriter.getMapHeight() + "\n" + writeMap;
		
		Assertions.assertEquals(expected, actual, "Written string representation is not correct.");
	}
	
	@Test
	public void testRead() throws IOException {
		InputStream byteStream = new ByteArrayInputStream(readMap.getBytes("UTF-8"));

		MapWriterReader mapReader = new MapWriterReader();
		
		String actual = mapReader.read(new InputStreamReader(byteStream));
		String expected = writeMap;
		
		var actualIterator = actual.chars().iterator();
		var expectedIterator = expected.chars().iterator();
		
		int i = 0;
		while (expectedIterator.hasNext()) {
			try {
				Assertions.assertEquals(expectedIterator.next(), actualIterator.next(), "Element mismatch at position " + i);
			} catch (IndexOutOfBoundsException e) {
				Assertions.fail("The read list does not contain the correct number of elements.");
			}
			i++;
		}
		
		Assertions.assertEquals(expected, actual, "Names does not match.");
	}
	
	@Test
	public void testWriteValidation() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(byteStream);
		
		MapWriterReader mapWriter = new MapWriterReader();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			mapWriter.write(writer, clearFloorMap);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			mapWriter.write(writer, missingPlayerMap);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			mapWriter.write(writer, missingCoinMap);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			mapWriter.write(writer, wrongFormatMap);
		});
	}
}
