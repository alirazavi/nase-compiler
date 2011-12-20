package compiler.parsers;

import java.util.ArrayList;

import syntaxtree.nodes.Node;
import syntaxtree.nodes.NullNode;

import compiler.scanner.ScannerWrapper;

import files.FileManager;
import files.Listing;

public abstract class ParserWrapper {
	
	ScannerWrapper scanner;	
	Node rootNode;
	final NullNode NULLNODE = new NullNode();
	
	Listing listing = FileManager.getInstance().getListing();
	
	static ArrayList<Node> history = new ArrayList<Node>();
	
	public abstract void parseProgramm();
	

	public static void addToHistory(Node node){
		history.add(node);
	}	
	
	String dumpHistory(){
		StringBuffer buffer = new StringBuffer();
		for(Node n : history)
			buffer.append(n.dumpNode()+"\n");
		
		return buffer.toString();
	}
}
