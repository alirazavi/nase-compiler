package syntaxtree.nodes;

import java.util.ArrayList;

public class NullNode extends Node {
	
	public NullNode(){
		representation = "NULL_NODE";
		children = new ArrayList<Node>(0);
		parent = null;
		column = -1;
		line = -1;
		userEntries = new ArrayList<String>(0);
	}

}
