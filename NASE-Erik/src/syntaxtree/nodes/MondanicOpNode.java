package syntaxtree.nodes;

public class MondanicOpNode extends Node {
	
	public MondanicOpNode(int line, int column, int smybol, Node term){
		this.line = line;
		this.column = column;
		id = idCounter++;
		addChild(term);
		addUserEntry(symbol);
	}

}
