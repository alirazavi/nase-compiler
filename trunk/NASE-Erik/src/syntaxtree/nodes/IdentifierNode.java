package syntaxtree.nodes;

public class IdentifierNode extends Node {

	public IdentifierNode(int line, int column, int symbol){
		this.line = line;
		this.column = column;
		this.userEntries.add(symbol);
		id = idCounter++;
	}
}
