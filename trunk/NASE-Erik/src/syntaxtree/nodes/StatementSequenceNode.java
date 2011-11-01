package syntaxtree.nodes;

import java.util.ArrayList;

public class StatementSequenceNode extends Node {
	
	public StatementSequenceNode(Node parent, int line, int column){
		this.line = line;
		this.column = column;
		this.representation = "statementSequenceNode";
		this.children = new ArrayList<Node>();
	}

}
