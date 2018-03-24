package test.my.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class CSVReader {


	private Iterator<CSVRecord> iterator = null;
	private CSVParser parser = null;

	public void initiateReader(String filePath) throws IOException {
		
		File csvData = new File(filePath);
		parser = CSVParser.parse(csvData, Charset.forName("UTF-8"),
				CSVFormat.DEFAULT);
		iterator = parser.iterator();


	}

	public boolean hasNext() {

		if (iterator != null) {
			return iterator.hasNext();
		}
		return false;

	}

	public String[] readLine() throws IOException {

		CSVRecord csvRecord = iterator.next();
		int size = csvRecord.size();
		String[] splittedValues = new String[size];
		for (int i = 0; i < size; i++) {
			splittedValues[i] = csvRecord.get(i);
		}
		
		return splittedValues;

	}

	public void dismantleReader() {

		if (parser != null) {
			try {
				parser.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
