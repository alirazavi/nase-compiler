package compiler;

import syntaxtree.nodes.Node;

public class InlineIfNode extends Node {
	
	public InlineIfNode(int line, int column, Node boolNode, Node thenNode, Node elseNode){
		this.line = line;
		this.column = column;
		children.add(boolNode);
		children.add(thenNode);
		children.add(elseNode);
	}

}
