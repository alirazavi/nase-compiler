package syntaxtree.nodes;

import java.util.ArrayList;

import compiler.Parser;

public abstract class Node{
	
	protected static int idCounter = 0;

	protected Node parent;
	protected ArrayList<Node> children = new ArrayList<Node>();

	protected ArrayList<Object> userEntries = new ArrayList<Object>();
	
	protected String representation;
	protected int symbol;
	
	protected int line;
	protected int column;
	protected int id  = 0;
		
	public Node(){
		Parser.addToHistory(this);
	}
	
	public void addUserEntry(Object o){
		userEntries.add(o);
	}
	
	public void addChild(Node child){
		child.setParent(this);
		children.add(child);
	}
	
	public void setParent(Node parent){
		this.parent = parent;
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("Node start\n\t%s\n\tID:       %d\n\tRow:      %d\n\tColumn:   %d\n\tEntries:   %d\n\tLinks:     %d", this.getClass().getSimpleName(), id, line, column, userEntries.size()+children.size(), children.size()));
		buffer.append("\n");
		for(Object o : userEntries)
			buffer.append("\t Userentry: "+o);
	
		for(Node n : children)
			buffer.append("\n" + n);
		
		if(children.size() == 0)
			buffer.append("\n");
		
		return buffer.toString();
	}
	
	public int getId(){
		return id;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((representation == null) ? 0 : representation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node other = (Node) obj;
		if (representation == null) {
			if (other.representation != null)
				return false;
		} else if (!representation.equals(other.representation))
			return false;
		return true;
	}

	public String dumpNode(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("Node start\n\t%s\n\tID:       %d\n\tRow:      %d\n\tColumn:   %d\n\tEntries:   %d\n\tLinks:     %d", this.getClass().getSimpleName(), id, line, column, userEntries.size()+children.size(), children.size()));
		buffer.append("\n");
		
		for(Object o : userEntries)
			buffer.append("\tUserentry: "+o);
		
		for(Node n : children)
			buffer.append("\tChild-ID: "+n.getId()+"\n");
		
		if(children.size() == 0)
			buffer.append("\n");
		
		return buffer.toString();
	}
	
}
