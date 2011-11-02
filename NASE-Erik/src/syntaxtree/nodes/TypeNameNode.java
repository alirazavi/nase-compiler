/**
 * 
 */
package syntaxtree.nodes;

import java.util.ArrayList;

import symboltable.Symbols;

/**
 * @author student
 *
 */
public class TypeNameNode extends Node {
	
	public TypeNameNode(int col, int line, Symbols sym){
		this.representation = "TypeNode";
		this.column = col;
		this.line = line;
		this.symbol = sym.ordinal();
		this.children = new ArrayList<Node>();
		this.userEntries = new ArrayList<String>();
		userEntries.add(sym.name());
	}

}
