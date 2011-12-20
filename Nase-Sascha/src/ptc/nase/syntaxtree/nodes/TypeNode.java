package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.backend.Storage;

public class TypeNode extends SyntaxtreeNode
{
	public TypeNode(long sLine, long sColumn, int symbol)
	{
		super(NODE_TYPE.TYPE, sLine, sColumn);
		addUserEntry(symbol);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		return true;
	}
}
