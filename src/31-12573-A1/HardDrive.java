import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class HardDrive {
	static HardDrive h;
	File indexes;
	int pages = 0;
	BufferedReader br;
	BufferedWriter w;
	 ArrayList<String> tableNames = new ArrayList<String>();
	 ArrayList<String> pageNames = new ArrayList<String>();
	public HardDrive() throws IOException{
		h = this;
		w = new BufferedWriter(new FileWriter(
				"Index.csv", true));
		indexes = new File("Index.csv");
		
			 br = new BufferedReader(new FileReader(indexes));
		String str = "";
		while((str= br.readLine())!=null){
			if(str.isEmpty())
				continue;
			pages++;
			String[] temp = str.split(",");
			String tableName = temp[0];
			String pageName = temp[1];
			tableNames.add(tableName);
			pageNames.add(pageName);
		}
	}
	///YAY FOR SUCKERFISH
	public void addPage(String tableName, String pageName) throws IOException{
		w.append(tableName + "," + pageName);
		w.newLine();
		w.flush();
		tableNames.add(tableName);
		pageNames.add(pageName);
		pages++;
//		w.close();
	}
	
	public static void main(String[] args) throws IOException {
		HardDrive h = new HardDrive();
		System.out.println(h.pages);
	}
}
