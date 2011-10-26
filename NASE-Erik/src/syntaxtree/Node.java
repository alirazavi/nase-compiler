package syntaxtree;

import java.util.ArrayList;

public class Node {
	
	private NodeType nodeType;
	private Node parent;
	private ArrayList<Node> children;
	

	public Node(Node parent){
		this.parent = parent;		
	}
	
	public void addChild(Node child){
		child.setParent(this);
		children.add(child);
	}
	
	public void setParent(Node parent){
		this.parent = parent;
	}

}
