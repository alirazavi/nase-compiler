package syntaxtree.nodes;

import java.util.ArrayList;

public class ProgrammNode extends Node {

	public ProgrammNode(Node parent, int row, int column) {
		this.line = row;
		this.column = column;
		this.representation = "programmNode";
		this.children = new ArrayList<Node>();
	}

}
