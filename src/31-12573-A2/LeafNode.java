
public class LeafNode extends Node{
	public static int LEAFORDER;
	public Object[] values;
	
	public LeafNode(int n) {
		super(n);
		values = new Object[n];
		keys = new int[n];
		
		// TODO Auto-generated constructor stub
	}
	
	
	public Object getValue(){
		return null;
	}
	
	public void setValue(){
		
	}
	
	public Object search(int x){
		for (int i = 0; i < keys.length; i++) {
			if(keys[i] == x)
				return Integer.valueOf(x);
		}
		return -1;
	}
	
	public void insertKey(Object x){
		
	}
	
	private void insertAt(int a, Object x){
		
	}
	
	public boolean delete(Object x){
		return false;
	}
	
	private boolean deleteAt(int x, Object y){
		return false;
	}
}
