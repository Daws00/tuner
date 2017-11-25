package tuner;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class DataHandler {
	private static final char COMMA_DELIMITER = ',';
	private static final char NEW_LINE_DELIMITER = '\n';
	
	private final String FILE_NAME;
	
	public DataHandler(String fileName) {
		FILE_NAME = fileName;
	}
	
	public void write(Pitch pitch) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(FILE_NAME, true));
			bw.write(String.valueOf(pitch.getTime()));
			bw.write(COMMA_DELIMITER);
			bw.write(String.valueOf(pitch.getPitch()));
			bw.write(NEW_LINE_DELIMITER);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) try {
			    bw.close();
			 } catch (IOException ioe2) {
			    // just ignore it
			 }
		}
	}
}
