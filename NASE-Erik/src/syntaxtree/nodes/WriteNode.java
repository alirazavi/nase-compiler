package syntaxtree.nodes;

public class WriteNode extends Node {
	
	public WriteNode(int line, int column, Node expr){
		this.line = line;
		this.column  = column;
		id = idCounter++;
		this.children.add(expr);
	}
}
