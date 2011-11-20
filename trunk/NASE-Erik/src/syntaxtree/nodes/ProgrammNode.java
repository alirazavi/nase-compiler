package syntaxtree.nodes;

public class ProgrammNode extends Node {

	public ProgrammNode(Node parent, int row, int column) {
		this.line = row;
		this.column = column;
		id = idCounter++;
	}

}
