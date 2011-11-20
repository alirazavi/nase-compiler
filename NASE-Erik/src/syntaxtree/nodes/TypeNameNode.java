/**
 * 
 */
package syntaxtree.nodes;

import symboltable.Symbols;

/**
 * @author student
 *
 */
public class TypeNameNode extends Node {
	
	public TypeNameNode(int col, int line, int sym){
		this.column = col;
		this.line = line;
		this.symbol = sym;
		id = idCounter++;
		userEntries.add(Symbols.getSymbolName(sym));
	}

}
