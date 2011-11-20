package syntaxtree.nodes;


public class ErrorNode extends Node {

	public ErrorNode(int line, int column){
		this.line = line;
		this.column = column;
		id = idCounter++;	
	}
	
}
