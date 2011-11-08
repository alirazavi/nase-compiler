package ptc.nase;

import ptc.nase.syntaxtree.nodes.SyntaxtreeNode;

enum SymbolType { reservedWordSymbol, identifierSymbol, numberSymbol };

public class SymbolTableEntry
{
	public String sRepresentation;
	public boolean isBreackingCharSeq;
	public int yielding;
	public boolean isFixEntry;
	public SymbolType type;
	public SyntaxtreeNode nodeLink;
}
