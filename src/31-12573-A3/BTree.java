import java.util.Arrays;
import java.util.Stack;


public class BTree {
	Node root;
	static BTree a;
	int n;
	
	public BTree(int n) {
		// TODO Auto-generated constructor stub
		this.n = n;
		BTree.a = this;
	}
	
	
	public Node findParent(Node n){
		int val = n.keys[0];
		Stack<Node> path = new Stack<Node>();
		Node node = root;
		path.push(node);
		try{
		while(node != n){
			node = (Node) node.findInsertingNode(val);
			path.push(node);
		}
		}catch(ClassCastException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			System.out.println(node);
		}
		path.pop();
		return path.pop();
	}
	public void insert(int val){
		if(root == null){
			root = new LeafNode(n);
			root.insert(val);
		}else{
			if(root instanceof LeafNode){
				if(root.isFree()){
					System.err.println("here");
					root.insert(val);
				}else{
					System.err.println("not here");
					root.split(val);
		//			root = root.parentNode;
				}
			}else{
				
				Node node = findNodeToInsert(val);
				node.insert(val);
				
			}
		}
		/*if(root.parentNode !=null){
			root = root.parentNode;
		}*/
	}
	
	
	
	public static void main(String[] args) {
		BTree b = new BTree(4);
		b.insert(4);
		b.insert(5);
		b.insert(6);
		b.insert(7);
		b.insert(11);

				b.insert(12);
		b.insert(13);
		b.insert(14);

		b.insert(8);
		b.insert(9);
		b.insert(15);
		b.insert(16);	
		b.insert(20);
		

	b.insert(30);
		b.insert(40);
		b.insert(50);
		b.insert(60);	

/*		InnerNode roo = (InnerNode) b.root;*/
		System.out.println(Arrays.toString(b.root.keys));
		System.out.println(Arrays.toString(b.root.children));
		System.out.println(Arrays.toString(b.root.children[1].children));
		System.out.println((b.root.children[1].children[3].parentNode));
		
		System.out.println(b.search(40));
	}
	private Node findNodeToInsert(int val) {
		// TODO Auto-generated method stub
		Node node = root;
		System.out.println(node);
		try{
		while(node instanceof InnerNode){
			node = (Node) node.findInsertingNode(val);
			System.out.println(node);
		}
		}catch(ClassCastException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			System.out.println(node);
		}
		return node;
	}

	public String search(int val){
		Node node = root;
		try{
		while(node instanceof InnerNode){
			node = (Node) node.findInsertingNode(val);
			System.err.println(node);
		}
		
		}catch(ClassCastException e){
			e.printStackTrace();
			}
	//	System.out.println(node.toString());
		System.out.println(node.search(val).toString());
		return Arrays.toString(node.keys);
	
	}
	public boolean delete(Object val){
		return false;
	}
	

}
