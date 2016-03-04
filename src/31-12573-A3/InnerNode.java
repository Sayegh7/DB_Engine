
public class InnerNode extends Node{
	
	public InnerNode(int n) {
		super(n);
		keys = new int[n];
		
		// TODO Auto-generated constructor stub
	}

	public Object search(int val) {
		// TODO Auto-generated method stub
		for (int i = 0; i < keys.length; i++) {
			if(keys[i]>val)
				return children[i];
		}
		return children[keys.length-1];
	}

	public void insertAndPoint(Node newNode, int i) {
		// TODO Auto-generated method stub
		insert(i);
	
		
	}



	
	
	
}
