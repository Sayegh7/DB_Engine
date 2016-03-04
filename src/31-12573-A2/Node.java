import java.util.Arrays;

public class Node {

	protected int[] keys;
	protected int keyCount;
	protected int n;
	public Node[] children;
	protected Node parentNode;
	protected Node leftSibling;
	protected Node rightSibling;
	public int Children;
	protected Node(int n) {
		this.keyCount = 0;
		this.Children = 0;
		this.n = n;
		this.parentNode = null;
		this.leftSibling = null;
		this.rightSibling = null;
		children = new Node[n+1];

	}

	public int getKeyCount() {
		return keyCount;
	}

	public boolean isFree() {
		if(keyCount < keys.length){
			return true;
		}
		return false;
	}

	public boolean isOverflow() {
		return false;
	}

	public boolean isUnderflow() {
		return false;
	}
	
	public Node findInsertingNode(int x){
		for (int i = 0; i < keyCount; i++) {
			if(keys[i]>x)
			{
	//			System.out.println("Inserting " +  x+ " at "+children[i]);				
				return children[i];
			}
		}
//		System.out.println("Inserting " +  x+ " at "+children[keyCount]);				
		return children[keyCount];

	}
	
	public Object search(int x){
		for (int i = 0; i < keys.length; i++) {
			if(keys[i]== x)
				return Integer.valueOf(x);
		}
		return Integer.valueOf(-1);
	
	}

	public void insert(int x) {
		if (!isFree()) {
			split(x);
			return;
		}
		int index = 0;
		//getting the index
		for (int i = 0; i < keys.length; i++) {
			if(keys[i]==0)
				break;
			if(keys[i]<x)
			index++;
			
		}
		//shifting keys to the right
		for (int i = keys.length-1; i > index; i--) {
			keys[i] = keys[i-1];
		}
		keys[index] = x;
		System.out.println("Inserting " + x + "at " + this);
		//adding key
		
//		keys[keyCount] = (x);
		
		if(this instanceof LeafNode){
			Integer i = Integer.valueOf(x);
			LeafNode t = (LeafNode) this;
			t.values[index] = i;
		}
		keyCount++;
		
	}

	public void split(int key) {
		// TODO Auto-generated method stub
		Node newNode;
		//boolean
		System.err.println("split");
		if (this instanceof InnerNode) {
			newNode = new InnerNode(n);
			splitChildren(this, newNode);
		} else {
			newNode = new LeafNode(n);
		}
		int j = 0;
		for (int i = keys.length / 2; i < keys.length; i++) {
			newNode.insert(keys[i]);
			keys[i] = 0;
			keyCount--;
			j++;
		}
		
		
		newNode.parentNode = parentNode;

		if (parentNode == null) {
			//new root
			parentNode = new InnerNode(n);
			parentNode.children[0] = this;
			parentNode.children[1] = newNode;
			parentNode.Children+=2;
			BTree.a.root = parentNode;
//			parentNode.keys[0] = (newNode.keys[0]);
//			parentNode.children[1] = newNode;
		}
		newNode.parentNode = parentNode;
		//		parentNode.insertAndPoint(newNode, newNode.keys[0]);
	/*	if(!parentNode.isFree()){
			System.err.println("Parent needs splitting");
			parentNode.insert(newNode.keys[0]);
			if(parentNode.contains(newNode.keys[0])){
				int i = parentNode.shiftAround(newNode);
				parentNode.children[i] = this;
				parentNode.children[i+1] = newNode;
			}else{
				try{
				int i = parentNode.rightSibling.shiftAround(newNode);
				parentNode.rightSibling.children[i] = this;
				parentNode.rightSibling.children[i+1] = newNode;
				}catch(Exception e){
					System.err.println(this);
				}
			}
				
		}else{
		
	*/	
		
		parentNode.insert(newNode.keys[0]);
			if(!parentNode.pointerShift(this, newNode)){
				System.err.println(this);
				parentNode.rightSibling.pointerShift(this, newNode);
			}
	//	}
			
		
		Node oldright = this.rightSibling;
		if (oldright != null)
			oldright.leftSibling = newNode;
		newNode.rightSibling = oldright;
		rightSibling = newNode;
		//parentNode.pointAndShift(newNode);
		parentNode = BTree.a.findParent(this);
		newNode.parentNode = BTree.a.findParent(newNode);
		System.err.println(this + " parent " + BTree.a.findParent(this));
		System.err.println(newNode + " parent " + BTree.a.findParent(newNode));

		try{
		parentNode.findInsertingNode(key).insert(key);
		}catch(NullPointerException e){
			System.err.println(this.parentNode);
		}
/*		for (int i = 0; i < children.length; i++) {
			if(children[i]!=null)
			children[i].parentNode = this;
		}
		for (int i = 0; i < newNode.children.length; i++) {
			if(newNode.children[i]!=null)
			newNode.children[i].parentNode = newNode;
		}
*/	}
	
	

	public boolean contains(int x){
		for (int i = 0; i < keys.length; i++) {
			if(keys[i] == x)
				return true;
		}
		return false;
	}

	private void splitChildren(Node node, Node newNode) {
		// TODO Auto-generated method stub
		int j = 0;
		for (int i = ((n+1) / 2); i < node.children.length; i++) {
			newNode.children[j] = node.children[i];
			if(j>0){
				newNode.children[j].parentNode = newNode;
				node.Children--;				
				node.children[i] = null;
			}
			newNode.Children++;
			j++;
		}
	}
	
	
	public String toString(){
		return Arrays.toString(keys);
	}

	public boolean pointerShift(Node node, Node newNode) {
		// TODO Auto-generated method stub
		int index = 0;
		//getting the index
		for (int i = 0; i < children.length; i++) {
			if(children[i]==node){
				break;
			}else{
				index++;
			}
		}
		if(index==5){
			System.err.println(this);
			return false;
		}
		
		//shifting pointers to the right
		for (int i = children.length-1; i > index+2 ; i--) {
			children[i] = children[i-1];
		}
		//adding pointers
		children[index] = node;
		children[index+1] = newNode;
		return true;
	}


/*	public void pointerShift(Node node, Node newNode) {
		// TODO Auto-generated method stub
		int index = 0;
		//getting the index
		for (int i = 0; i < keys.length; i++) {
			if(keys[i]==newNode.keys[0]){
				break;
			}else{
				index++;
			}
		}
		//shifting pointers to the right
		for (int i = children.length-1; i > index+1 ; i--) {
			children[i] = children[i-1];
		}
		//adding pointers
		children[index] = node;
		children[index+1] = newNode;
		
	}
*/
	
}
