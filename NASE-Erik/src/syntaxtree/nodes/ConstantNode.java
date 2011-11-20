package syntaxtree.nodes;

public class ConstantNode extends Node {
	
	public ConstantNode(int line, int column, int sym){
		this.line = line;
		this.column = column;
		this.userEntries.add(sym);
		id = idCounter++;
	}

}
