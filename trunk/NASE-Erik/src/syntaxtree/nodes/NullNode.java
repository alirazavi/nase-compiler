package syntaxtree.nodes;


public class NullNode extends Node {
	
	public NullNode(){
		representation = "NULL_NODE";
		parent = null;
		column = -1;
		line = -1;
		id = idCounter++;
	}

}
