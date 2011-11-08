package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

public class IdentifierNode extends SyntaxtreeNode 
{
	public IdentifierNode(long sLine, long sColumn, int symbol)
	{
		super(NODE_TYPE.IDENTIFIER, sLine, sColumn);
		addUserEntry(symbol);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		return true;
	}
}
