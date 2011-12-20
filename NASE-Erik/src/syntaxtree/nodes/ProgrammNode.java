package syntaxtree.nodes;

public class ProgrammNode extends Node {

	public ProgrammNode(int row, int column, Node seq) {
		this.line = row;
		this.column = column;
		id = idCounter++;
		if(seq != null)
			this.children.add(seq);
	}

}
