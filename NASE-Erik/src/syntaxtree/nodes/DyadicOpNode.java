package syntaxtree.nodes;

public class DyadicOpNode extends Node {
	
	public DyadicOpNode(int line, int column, int sym, Node left, Node right){
		this.line = line;
		this.column = column;
		id = idCounter++;
		children.add(left);
		children.add(right);
		userEntries.add(sym);
	}

}
