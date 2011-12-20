package syntaxtree.nodes;

public class Block extends Node {
	
	public Block(int line, int column, Node statementSeq){
		this.line = line;
		this.column = column;
		this.children.add(statementSeq);
		id = idCounter++;
	}

}
