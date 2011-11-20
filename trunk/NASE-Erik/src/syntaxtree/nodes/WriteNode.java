package syntaxtree.nodes;

public class WriteNode extends Node {
	
	public WriteNode(int line, int column){
		this.line = line;
		this.column  = column;
		id = idCounter++;
	}
}
