package syntaxtree.nodes;

import java.util.ArrayList;

public class DeclarationNode extends Node {

	public DeclarationNode(int col, int line, int symbol){
		this.column = col;
		this.line = line;
		this.symbol = symbol;
		this.children = new ArrayList<Node>();
		this.userEntries = new ArrayList<String>();
		this.representation = "declarationNode";
	}
	
}
