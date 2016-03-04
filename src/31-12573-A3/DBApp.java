import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;

class DBAppException extends Exception {

	public DBAppException(String string) {
		// TODO Auto-generated constructor stub
		super(string);
	}

}

class DBEngineException extends Exception {

}

public class DBApp {

	File f = new File("metadata.csv");

	public void init() {
		try {
			HardDrive h = new HardDrive();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Reflector r = new Reflector();
	}

	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws DBAppException, IOException {
		checkTableDuplicates(strTableName);
		BufferedWriter w = new BufferedWriter(new FileWriter(f, true));
		int columns = 0;
		for (String key : htblColNameType.keySet()) {
			String colName = key;
			boolean primary = (strKeyColName.equals(colName));
			columns++;
			String colType = htblColNameType.get(key);
			String ref = htblColNameRefs.get(key);
			String meta = "";
			if (ref == null) {
				meta = strTableName + "," + colName + "," + colType + ","
						+ primary + "," + "False, null";
			} else {
				meta = strTableName + "," + colName + "," + colType + ","
						+ primary + "," + "False" + "," + ref;
			}
			w.append(meta);
			w.newLine();
			w.flush();
		}
		Page p = new Page(strTableName, columns);

	}

	private void checkTableDuplicates(String strTableName) throws IOException,
			DBAppException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] temp = line.split(",");
			if (strTableName.equalsIgnoreCase(temp[0])) {
				throw new DBAppException("Table already exists");
			}
		}
	}

	public void createIndex(String strTableName, String strColName)
			throws DBAppException {

	}

	public void insertIntoTable(String strTableName,
			Hashtable<String, Object> htblColNameValue) {
		Page p = findPage(strTableName);
		try {
			p.writeRow(htblColNameValue);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DBAppException e) {
			if (e.getMessage().equalsIgnoreCase("Full")) {

			}
		}

	}

	private Page findPage(String strTableName) {
		Page p = null;
		try {
			ArrayList<String> tables = HardDrive.h.tableNames;
			ArrayList<String> pages = HardDrive.h.pageNames;
			System.out.println(tables.toString());
			System.out.println(pages.toString());
			for (int i = tables.size() - 1; i >= 0; i--) {
				if (tables.get(i).equalsIgnoreCase(strTableName)) {
					String url = pages.get(i);
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(new File(url)));
					p = (Page) ois.readObject();
					ois.close();
					if (p.isFull()) {
						Page pp = new Page(strTableName, p.columns);
						// hehehehe pepe
						return pp;
					}
					break;

				}
			}
			// Writing a new table
			/*
			 * if (p == null) { p = new Page(strTableName, true);
			 * 
			 * }
			 */
		} catch (EOFException e) {

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;

	}

	public void updateTable(String strTableName, Object strKey,
			Hashtable<String, Object> htblColNameValue) throws DBAppException,
			IOException, ClassNotFoundException {

		ArrayList<String> pages = getFiles(strTableName);
		// getting column position
		int position = findColumn(strTableName);
		// use the above loop in a mthod called find column
		// getting row number in page
		Page p;
		int row = 0;
		for (String pageName : pages) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					new File(pageName)));
			p = (Page) ois.readObject();
			ois.close();
			row = p.findRow(strKey.toString(), position);
			if (row == -1) {
				System.out.println("koko");
				continue;

			} else {
				for (String columnName : htblColNameValue.keySet()) {
					int columnIndex = findColumn(strTableName, columnName);
					p.updateRow(htblColNameValue.get(columnName).toString(),
							row, columnIndex);
					p.save();
				}

			}

		}

	}

	public int findColumn(String strTableName, String columnName)
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		int position = 0;
		while ((line = br.readLine()) != null) {
			String[] x = line.split(",");
			if (x[0].equalsIgnoreCase(strTableName)) {
				if (!x[1].equalsIgnoreCase(columnName)) {
					position++;
				} else {
					break;
				}
			}

		}
		return position;
	}

	public int findColumn(String strTableName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		int position = 0;
		while ((line = br.readLine()) != null) {
			String[] x = line.split(",");
			if (x[0].equalsIgnoreCase(strTableName)) {
				if (x[3].equalsIgnoreCase("false")) {
					position++;
				} else {
					break;
				}
			}

		}
		return position;
	}

	public void deleteFromTable(String strTableName,
			Hashtable<String, Object> htblColNameValue, String strOperator)
			throws DBEngineException {
		try {

			ArrayList<String> x = new ArrayList<String>();
			for (String key : htblColNameValue.keySet()) {
				x.add(key);
			}
			Hashtable<String, Integer> colNameIndex = findColNumber(strTableName, x);
			ArrayList<String> pages = getFiles(strTableName);
			BufferedReader b;
			// Hashtable<Integer, Object> Comparingtable = new
			// Hashtable<Integer, Object>();
			ArrayList<Integer> ColIndex = new ArrayList<Integer>();
			ArrayList<Object> ColVal = new ArrayList<Object>();
			ArrayList<String> ColType = findColTypes(strTableName);
			// ColIndex has an index for every value in ColVal
			for (String NameIndex : colNameIndex.keySet()) {
				for (String NameVal : htblColNameValue.keySet()) {
					if (NameVal.equalsIgnoreCase(NameIndex)) {
						ColIndex.add(colNameIndex.get(NameIndex));
						ColVal.add(htblColNameValue.get(NameVal));
					}
				}
			}
			ObjectInputStream oss = new ObjectInputStream(new FileInputStream(
					new File(pages.get(0))));
			Page p = (Page) oss.readObject();
			oss.close();

			if (strOperator.equalsIgnoreCase("and")) {
				// and

				for (String pageName : pages) {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(new File(pageName)));
					p = (Page) ois.readObject();
					ois.close();
					String line = "";
					boolean flag = true;
					for (int j = 0; j < p.page.length; j++) {
						flag = true;
						line = Arrays.toString(p.page[j]);
						line = line.replace("[", "");
						line = line.replace("]", "");
						String[] temp = line.split(",");
						for (int i = 0; i < ColIndex.size(); i++) {
							String compare = temp[ColIndex.get(i)].toString()
									.replace(" ", "");
							String compare2 = ColVal.get(i).toString()
									.replace(" ", "");
							if (compare2.equalsIgnoreCase(compare)) {
							} else {
								flag = false;
							}
						}
						if (flag) {
							// System.err.println(Arrays.toString(temp));
							p.deleteRow(j);
						}

					}
				}
			} else if (strOperator.equalsIgnoreCase("or")) {
				// or

				for (String pageName : pages) {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(new File(pageName)));
					p = (Page) ois.readObject();
					ois.close();
					String line = "";
					for (int j = 0; j < p.page.length; j++) {
						line = Arrays.toString(p.page[j]);
						line = line.replace("[", "");
						line = line.replace("]", "");
						String[] temp = line.split(",");
						// System.out.println(line);
						// System.out.println(Arrays.toString(temp));
						for (int i = 0; i < ColIndex.size(); i++) {
							String compare = temp[ColIndex.get(i)].toString()
									.replace(" ", "");
							String compare2 = ColVal.get(i).toString()
									.replace(" ", "");
							if (compare2.equalsIgnoreCase(compare)) {
								// System.err.println(Arrays.toString(temp));
								p.deleteRow(j);
								break;
							}
						}
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Iterator selectFromTable(String strTable,
			Hashtable<String, Object> htblColNameValue, String strOperator)
			throws DBEngineException {
		SearchResult sr = null;
		try {

			ArrayList<String> x = new ArrayList<String>();
			for (String key : htblColNameValue.keySet()) {
				x.add(key);
			}
			Hashtable<String, Integer> colNameIndex = findColNumber(strTable, x);
			ArrayList<String> pages = getFiles(strTable);
			BufferedReader b;
			// Hashtable<Integer, Object> Comparingtable = new
			// Hashtable<Integer, Object>();
			ArrayList<Integer> ColIndex = new ArrayList<Integer>();
			ArrayList<Object> ColVal = new ArrayList<Object>();
			ArrayList<String> ColType = findColTypes(strTable);
			// ColIndex has an index for every value in ColVal
			for (String NameIndex : colNameIndex.keySet()) {
				for (String NameVal : htblColNameValue.keySet()) {
					if (NameVal.equalsIgnoreCase(NameIndex)) {
						ColIndex.add(colNameIndex.get(NameIndex));
						ColVal.add(htblColNameValue.get(NameVal));
					}
				}
			}
			ObjectInputStream oss = new ObjectInputStream(new FileInputStream(
					new File(pages.get(0))));
			Page p = (Page) oss.readObject();
			sr = new SearchResult(p.page.length, p.page[0].length);
			oss.close();

			if (strOperator.equalsIgnoreCase("and")) {
				// and

				for (String pageName : pages) {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(new File(pageName)));
					p = (Page) ois.readObject();
					ois.close();
					String line = "";
					boolean flag = true;
					for (int j = 0; j < p.page.length; j++) {
						flag = true;
						line = Arrays.toString(p.page[j]);
						line = line.replace("[", "");
						line = line.replace("]", "");
						String[] temp = line.split(",");
						for (int i = 0; i < ColIndex.size(); i++) {
							String compare = temp[ColIndex.get(i)].toString()
									.replace(" ", "");
							String compare2 = ColVal.get(i).toString()
									.replace(" ", "");
							if (compare2.equalsIgnoreCase(compare)) {
							} else {
								flag = false;
							}
						}
						if (flag) {
							// System.err.println(Arrays.toString(temp));
							sr.addRow(temp);
						}

					}
				}
			} else if (strOperator.equalsIgnoreCase("or")) {
				// or

				for (String pageName : pages) {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(new File(pageName)));
					p = (Page) ois.readObject();
					ois.close();
					String line = "";
					for (int j = 0; j < p.page.length; j++) {
						line = Arrays.toString(p.page[j]);
						line = line.replace("[", "");
						line = line.replace("]", "");
						String[] temp = line.split(",");
						// System.out.println(line);
						// System.out.println(Arrays.toString(temp));
						for (int i = 0; i < ColIndex.size(); i++) {
							String compare = temp[ColIndex.get(i)].toString()
									.replace(" ", "");
							String compare2 = ColVal.get(i).toString()
									.replace(" ", "");
							if (compare2.equalsIgnoreCase(compare)) {
								// System.err.println(Arrays.toString(temp));
								sr.addRow(temp);
								break;
							}
						}
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sr;
	}

	private ArrayList<String> getFiles(String strTable) throws IOException {
		BufferedReader bb = new BufferedReader(new FileReader("Index.csv"));
		ArrayList<String> x = new ArrayList<String>();
		String line = "";
		while ((line = bb.readLine()) != null) {
			String[] temp = line.split(",");
			if (temp[0].equalsIgnoreCase(strTable)) {
				x.add(temp[1]);
			}
		}
		return x;
	}

	private Hashtable<String, Integer> findColNumber(String strTable,
			ArrayList<String> x) {
		Hashtable<String, Integer> result = new Hashtable<String, Integer>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			int index = 0;
			while ((line = br.readLine()) != null) {
				for (int i = 0; i < x.size(); i++) {
					String[] temp = line.split(",");
					if (temp[0].equals(strTable)) {
						if (temp[1].equals(x.get(i))) {
							result.put(x.get(i), index);
						}
					}
				}
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<String> findColTypes(String strTable) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"metadata.csv"));
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] temp = line.split(",");
				for (int i = 0; i < temp.length; i++) {
					result.add(temp[2]);
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) throws DBAppException, IOException,
			DBEngineException, ClassNotFoundException {
		// main2();

		// update will update the age of the student with id=550 to 23

		DBApp myDB = new DBApp();

		// initialize it

		myDB.init();
		/*
		 * Hashtable<String, String> fTblColNameType = new Hashtable<String,
		 * String>(); fTblColNameType.put("ID", "Integer");
		 * fTblColNameType.put("Name", "String");
		 * 
		 * Hashtable<String, String> fTblColNameRefs = new Hashtable<String,
		 * String>();
		 * 
		 * myDB.createTable("Faculty", fTblColNameType, fTblColNameRefs, "ID");
		 * 
		 * Hashtable<String, Object> ftblColNameValue2 = new Hashtable<String,
		 * Object>(); ftblColNameValue2.put("ID", Integer.valueOf("1"));
		 * ftblColNameValue2.put("Name", "Management Technology");
		 * myDB.insertIntoTable("Faculty", ftblColNameValue2);
		 * 
		 * Hashtable<String,Object> stblColNameValue = new
		 * Hashtable<String,Object>(); stblColNameValue.put("Name", "koko" );
		 * myDB.updateTable("Faculty","1",stblColNameValue);
		 */
		Hashtable<String, Object> stblColNameValue1 = new Hashtable<String, Object>();
		stblColNameValue1.put("Name", "koko");

		long startTime = System.currentTimeMillis();
		Iterator myIt = myDB.selectFromTable("Faculty", stblColNameValue1,
				"AND");
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
		while (myIt.hasNext()) {
			System.out.println(myIt.next());
		}

	/*	Hashtable<String, Object> stblColNameValue1 = new Hashtable<String, Object>();
		stblColNameValue1.put("Name", "koko");

		 myDB.deleteFromTable("Faculty", stblColNameValue1,
				"AND");*/

		
	}

	public static void main2() throws DBAppException, DBEngineException,
			IOException {
		// creat a new DBApp
		DBApp myDB = new DBApp();

		// initialize it
		myDB.init();

		// creating table "Faculty"

		Hashtable<String, String> fTblColNameType = new Hashtable<String, String>();
		fTblColNameType.put("ID", "Integer");
		fTblColNameType.put("Name", "String");

		Hashtable<String, String> fTblColNameRefs = new Hashtable<String, String>();

		myDB.createTable("Faculty", fTblColNameType, fTblColNameRefs, "ID");

		// creating table "Major"

		Hashtable<String, String> mTblColNameType = new Hashtable<String, String>();
		fTblColNameType.put("ID", "Integer");
		fTblColNameType.put("Name", "String");
		fTblColNameType.put("Faculty_ID", "Integer");

		Hashtable<String, String> mTblColNameRefs = new Hashtable<String, String>();
		mTblColNameRefs.put("Faculty_ID", "Faculty.ID");

		myDB.createTable("Major", mTblColNameType, mTblColNameRefs, "ID");

		// creating table "Course"

		Hashtable<String, String> coTblColNameType = new Hashtable<String, String>();
		coTblColNameType.put("ID", "Integer");
		coTblColNameType.put("Name", "String");
		coTblColNameType.put("Code", "String");
		coTblColNameType.put("Hours", "Integer");
		coTblColNameType.put("Semester", "Integer");
		coTblColNameType.put("Major_ID", "Integer");

		Hashtable<String, String> coTblColNameRefs = new Hashtable<String, String>();
		coTblColNameRefs.put("Major_ID", "Major.ID");

		myDB.createTable("Course", coTblColNameType, coTblColNameRefs, "ID");

		// creating table "Student"

		Hashtable<String, String> stTblColNameType = new Hashtable<String, String>();
		stTblColNameType.put("ID", "Integer");
		stTblColNameType.put("First_Name", "String");
		stTblColNameType.put("Last_Name", "String");
		stTblColNameType.put("GPA", "Double");
		stTblColNameType.put("Age", "Integer");

		Hashtable<String, String> stTblColNameRefs = new Hashtable<String, String>();

		myDB.createTable("Student", stTblColNameType, stTblColNameRefs, "ID");

		// creating table "Student in Course"

		Hashtable<String, String> scTblColNameType = new Hashtable<String, String>();
		scTblColNameType.put("ID", "Integer");
		scTblColNameType.put("Student_ID", "Integer");
		scTblColNameType.put("Course_ID", "Integer");

		Hashtable<String, String> scTblColNameRefs = new Hashtable<String, String>();
		scTblColNameRefs.put("Student_ID", "Student.ID");
		scTblColNameRefs.put("Course_ID", "Course.ID");

		myDB.createTable("Student_in_Course", scTblColNameType,
				scTblColNameRefs, "ID");

		// insert in table "Faculty"

		Hashtable<String, Object> ftblColNameValue1 = new Hashtable<String, Object>();
		ftblColNameValue1.put("ID", Integer.valueOf("1"));
		ftblColNameValue1.put("Name", "Media Engineering and Technology");
		myDB.insertIntoTable("Faculty", ftblColNameValue1);

		Hashtable<String, Object> ftblColNameValue2 = new Hashtable<String, Object>();
		ftblColNameValue2.put("ID", Integer.valueOf("2"));
		ftblColNameValue2.put("Name", "Management Technology");
		myDB.insertIntoTable("Faculty", ftblColNameValue2);

		for (int i = 0; i < 1000; i++) {
			Hashtable<String, Object> ftblColNameValueI = new Hashtable<String, Object>();
			ftblColNameValueI.put("ID", Integer.valueOf(("" + (i + 2))));
			ftblColNameValueI.put("Name", "f" + (i + 2));
			myDB.insertIntoTable("Faculty", ftblColNameValueI);
		}

		// insert in table "Major"

		Hashtable<String, Object> mtblColNameValue1 = new Hashtable<String, Object>();
		mtblColNameValue1.put("ID", Integer.valueOf("1"));
		mtblColNameValue1.put("Name", "Computer Science & Engineering");
		mtblColNameValue1.put("Faculty_ID", Integer.valueOf("1"));
		myDB.insertIntoTable("Major", mtblColNameValue1);

		Hashtable<String, Object> mtblColNameValue2 = new Hashtable<String, Object>();
		mtblColNameValue2.put("ID", Integer.valueOf("2"));
		mtblColNameValue2.put("Name", "Business Informatics");
		mtblColNameValue2.put("Faculty_ID", Integer.valueOf("2"));
		myDB.insertIntoTable("Major", mtblColNameValue2);

		for (int i = 0; i < 1000; i++) {
			Hashtable<String, Object> mtblColNameValueI = new Hashtable<String, Object>();
			mtblColNameValueI.put("ID", Integer.valueOf(("" + (i + 2))));
			mtblColNameValueI.put("Name", "m" + (i + 2));
			mtblColNameValueI
					.put("Faculty_ID", Integer.valueOf(("" + (i + 2))));
			myDB.insertIntoTable("Major", mtblColNameValueI);
		}

		// insert in table "Course"

		Hashtable<String, Object> ctblColNameValue1 = new Hashtable<String, Object>();
		ctblColNameValue1.put("ID", Integer.valueOf("1"));
		ctblColNameValue1.put("Name", "Data Bases II");
		ctblColNameValue1.put("Code", "CSEN 604");
		ctblColNameValue1.put("Hours", Integer.valueOf("4"));
		ctblColNameValue1.put("Semester", Integer.valueOf("6"));
		ctblColNameValue1.put("Major_ID", Integer.valueOf("1"));
		myDB.insertIntoTable("Course", mtblColNameValue1);

		Hashtable<String, Object> ctblColNameValue2 = new Hashtable<String, Object>();
		ctblColNameValue2.put("ID", Integer.valueOf("1"));
		ctblColNameValue2.put("Name", "Data Bases II");
		ctblColNameValue2.put("Code", "CSEN 604");
		ctblColNameValue2.put("Hours", Integer.valueOf("4"));
		ctblColNameValue2.put("Semester", Integer.valueOf("6"));
		ctblColNameValue2.put("Major_ID", Integer.valueOf("2"));
		myDB.insertIntoTable("Course", mtblColNameValue2);

		for (int i = 0; i < 1000; i++) {
			Hashtable<String, Object> ctblColNameValueI = new Hashtable<String, Object>();
			ctblColNameValueI.put("ID", Integer.valueOf(("" + (i + 2))));
			ctblColNameValueI.put("Name", "c" + (i + 2));
			ctblColNameValueI.put("Code", "co " + (i + 2));
			ctblColNameValueI.put("Hours", Integer.valueOf("4"));
			ctblColNameValueI.put("Semester", Integer.valueOf("6"));
			ctblColNameValueI.put("Major_ID", Integer.valueOf(("" + (i + 2))));
			myDB.insertIntoTable("Course", ctblColNameValueI);
		}

		// insert in table "Student"

		for (int i = 0; i < 1000; i++) {
			Hashtable<String, Object> sttblColNameValueI = new Hashtable<String, Object>();
			sttblColNameValueI.put("ID", Integer.valueOf(("" + i)));
			sttblColNameValueI.put("First_Name", "FN" + i);
			sttblColNameValueI.put("Last_Name", "LN" + i);
			sttblColNameValueI.put("GPA", Double.valueOf("0.7"));
			sttblColNameValueI.put("Age", Integer.valueOf("20"));
			myDB.insertIntoTable("Student", sttblColNameValueI);
			// changed it to student instead of course
		}

		// selecting

		Hashtable<String, Object> stblColNameValue = new Hashtable<String, Object>();
		stblColNameValue.put("ID", Integer.valueOf("550"));
		stblColNameValue.put("Age", Integer.valueOf("20"));

		long startTime = System.currentTimeMillis();
		Iterator myIt = myDB
				.selectFromTable("Student", stblColNameValue, "AND");
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
		while (myIt.hasNext()) {
			System.out.println(myIt.next());
		}

		// feel free to add more tests Hashtable<String, Object>
		Hashtable<String, Object> stblColNameValue3 = new Hashtable<String, Object>();
		stblColNameValue3 = new Hashtable<String, Object>();
		stblColNameValue3.put("Name", "m7");
		stblColNameValue3.put("Faculty_ID", Integer.valueOf("7"));

		long startTime2 = System.currentTimeMillis();
		Iterator myIt2 = myDB
				.selectFromTable("Major", stblColNameValue3, "AND");
		long endTime2 = System.currentTimeMillis();
		long totalTime2 = endTime - startTime;
		System.out.println(totalTime2);
		while (myIt2.hasNext()) {
			System.out.println(myIt.next());
		}
	}

}
