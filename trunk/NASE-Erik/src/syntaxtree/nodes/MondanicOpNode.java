package syntaxtree.nodes;

public class MondanicOpNode extends Node {
	
	public MondanicOpNode(int line, int column){
		this.line = line;
		this.column = column;
		id = idCounter++;
	}

}
