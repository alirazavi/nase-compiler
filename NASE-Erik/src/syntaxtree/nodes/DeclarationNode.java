package syntaxtree.nodes;


public class DeclarationNode extends Node {

	public DeclarationNode(int col, int line, int symbol){
		this.column = col;
		this.line = line;
		this.symbol = symbol;
		id = idCounter++;
	}
	
}
