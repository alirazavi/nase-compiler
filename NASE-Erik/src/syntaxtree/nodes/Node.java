package syntaxtree.nodes;

import java.util.ArrayList;

public abstract class Node {
	
	protected Node parent;
	protected ArrayList<Node> children;
	protected ArrayList<String> userEntries;
	protected String representation;
	protected int symbol;
	
	protected int line;
	protected int column;
	
	
	public void addChild(Node child){
		child.setParent(this);
		children.add(child);
	}
	
	public void setParent(Node parent){
		this.parent = parent;
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("\nNode start\n\t%s\n\tRow:      %d\n\tColumn:   %d\n\tEntries:   %d\n\tLinks:     %d", representation, line, column, userEntries.size()+children.size(), children.size()));
		
		return buffer.toString();
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

	
	
}
