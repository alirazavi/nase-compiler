package syntaxtree.nodes;

public class IfNode extends Node {

	public IfNode(int line, int column, Node boolNode, Node thenNode, Node elseNode){
		this.line = line;
		this.column = column;
		id = idCounter++;
		children.add(boolNode);
		children.add(thenNode);
		if(elseNode != null)
			children.add(elseNode);
	}
	
	public IfNode(int line, int column, Node boolNode, Node thenNode){
		this(line, column, boolNode, thenNode, null);
	}
}
