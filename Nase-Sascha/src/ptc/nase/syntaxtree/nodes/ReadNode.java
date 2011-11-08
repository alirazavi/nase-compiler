package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.frontend.Listing;

public class ReadNode extends SyntaxtreeNode 
{
	public ReadNode(long sLine, long sColumn, SyntaxtreeNode identifierNode)
	{
		super(NODE_TYPE.READ, sLine, sColumn);
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
			Listing.writeInternalError("Identifier of read node (TODO) must not be empty!");
			return false;
		}		
	}
}
