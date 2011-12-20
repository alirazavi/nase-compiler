package syntaxtree.nodes;


public class DeclarationNode extends Node {

	public DeclarationNode(int col, int line, int symbol, Node type){
		this.column = col;
		this.line = line;
		this.symbol = symbol;
		this.children.add(type);
		id = idCounter++;
	}
	
}
