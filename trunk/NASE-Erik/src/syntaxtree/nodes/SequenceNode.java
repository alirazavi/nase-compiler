package syntaxtree.nodes;


public class SequenceNode extends Node {
	
	public SequenceNode(Node parent, int line, int column){
		this.line = line;
		this.column = column;
		id = idCounter++;
	}

}
