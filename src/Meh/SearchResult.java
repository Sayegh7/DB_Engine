import java.util.Arrays;
import java.util.Iterator;


public class SearchResult implements Iterator{

	String[][] table;
	int pointer = 0;
	int maxrows;
	int currentrows = 0;
	public SearchResult(int rows, int cols) {
		table = new String[rows][cols];
		this.maxrows = rows;
	}
	public void addRow(String[] row){
		table[pointer] = row;
		//System.out.println(Arrays.toString(table[pointer]));
		currentrows++;

	}
	
	@Override
	public boolean hasNext() {
		if(pointer==currentrows )
		return false;
		
		return true;
	}

	@Override
	public Object next() {
		String x=Arrays.toString(table[pointer]);
		pointer++;
		return x;
	}

}
