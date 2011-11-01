package syntaxtree.nodes;

import java.util.ArrayList;

public class ErrorNode extends Node {

	public ErrorNode(int line, int column){
		this.line = line;
		this.column = column;
		this.children = new ArrayList<Node>(0);
		this.representation = "errorNode";		
	}
	
}
