package syntaxtree.nodes;

public class AssignmentNode extends Node {

	public AssignmentNode(int line, int column){
		id = idCounter++;
		this.column = column;
		this.line= line;
	}
	
}
