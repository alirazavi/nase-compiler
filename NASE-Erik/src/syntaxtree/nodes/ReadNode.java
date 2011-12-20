package syntaxtree.nodes;

public class ReadNode extends Node {
	
	public ReadNode(int line, int column, Node ident){
		this.line = line;
		this.column  = column;
		id = idCounter++;
		this.children.add(ident);
	}
}
