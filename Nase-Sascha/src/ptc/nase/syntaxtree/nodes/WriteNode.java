package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.frontend.Listing;

public class WriteNode extends SyntaxtreeNode 
{
	public WriteNode(long sLine, long sColumn, SyntaxtreeNode identifierNode)
	{
		super(NODE_TYPE.WRITE, sLine, sColumn);
		addChild(identifierNode);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode identifier = childs.get(0);
		
		if (identifier.getType() != NODE_TYPE.NULL)
		{
			return identifier.checkIntegrity();
		}
		else
		{
			Listing.writeInternalError("Identifier of write node " + id + " must not be empty!");
			return false;
		}		
	}
}
