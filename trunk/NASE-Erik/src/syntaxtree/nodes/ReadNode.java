package syntaxtree.nodes;

public class ReadNode extends Node {
	
	public ReadNode(int line, int column){
		this.line = line;
		this.column  = column;
		id = idCounter++;
	}
}
