package syntaxtree.nodes;

public class AssignmentNode extends Node {

	public AssignmentNode(int line, int column, Node ident, Node expr){
		id = idCounter++;
		this.column = column;
		this.line = line;
		this.children.add(ident);
		this.children.add(expr);
	}
	
}
