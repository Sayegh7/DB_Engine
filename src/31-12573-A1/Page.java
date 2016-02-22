import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;

public class Page implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[][] page;
	int max = 200;
	transient BufferedWriter w;
	transient BufferedReader br;
	public int columns;
	public int lastEmptyRow;
	public String pageName;

	public Page(String tableNameOrPageName, int columns) throws IOException {
		this.columns = columns;
		//DO NOT FOR THE LOVE OF GOD CHANGE THIS NUMBER
		page = new String[max][columns + 1];
		pageName = "" + (HardDrive.h.pages + 1) + ".class";
		HardDrive.h.addPage(tableNameOrPageName, pageName);
		w = new BufferedWriter(new FileWriter(pageName, true));
		w.append("");
		w.flush();
		save();
	}
	

	public boolean isFull() {
		boolean full = true;
		for (int i = 0; i < page.length; i++) {
			if (page[i][0] == null) {
				full = false;
				lastEmptyRow = i;
			}
		}
		return full;
	}

	public void save() throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File(pageName)));
		oos.writeObject(this);
		oos.close();
	}

	public void writeRow(Hashtable<String, Object> htblColNameValue)
			throws DBAppException, IOException {
		if (isFull()) {
			throw new DBAppException("Full");
		}
		int i = 0;
		Date date = new Date();
		for (String key : htblColNameValue.keySet()) {
			String value = htblColNameValue.get(key).toString();
			page[lastEmptyRow][i] = value;
			i++;
		}
		page[lastEmptyRow][i] = date.toString();

		lastEmptyRow++;
		save();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("6.class")));
		Page p = (Page) ois.readObject();
		for (int i = 0; i < p.page.length; i++) {
			System.out.println(Arrays.toString(p.page[i]));
		}
	}

}
