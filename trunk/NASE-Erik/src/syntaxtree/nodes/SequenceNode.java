package syntaxtree.nodes;

import java.util.ArrayList;

public class SequenceNode extends Node {
	
	public SequenceNode(Node parent, int line, int column){
		this.line = line;
		this.column = column;
		this.representation = "sequenceNode";
		this.children = new ArrayList<Node>();
		this.userEntries = new ArrayList<String>();
	}

}
