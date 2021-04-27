package file;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public interface WriterReader {
	String read(InputStreamReader reader);
	String read(String fileName) throws IOException;
	void write(String fileName, String text) throws IOException;
	void write(PrintWriter writer, String text);
}
