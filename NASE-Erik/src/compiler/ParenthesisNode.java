package compiler;

import syntaxtree.nodes.Node;

public class ParenthesisNode extends Node {
	
	public ParenthesisNode(int line, int column, Node expr){
		this.line = line;
		this.column = column;
		children.add(expr);		
	}
}
