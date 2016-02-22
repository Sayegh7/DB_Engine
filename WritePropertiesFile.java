import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class WritePropertiesFile {
	public static void main(String[] args) {
		try {
			Properties properties = new Properties();
			properties.setProperty("MaximumRowsCountinPage", "200");
			properties.setProperty("BPlusTreeN", "20");

			File file = new File("DBApp.properties");
			FileOutputStream fileOut = new FileOutputStream(file);
			properties.store(fileOut, "Properties file");
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
