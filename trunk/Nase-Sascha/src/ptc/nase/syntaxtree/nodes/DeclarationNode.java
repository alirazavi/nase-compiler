package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.frontend.Listing;

public class DeclarationNode extends SyntaxtreeNode
{
	public DeclarationNode(long sLine, long sColumn, SyntaxtreeNode typeNode, int symbol)
	{
		super(NODE_TYPE.DECLARATION, sLine, sColumn);
		addChild(typeNode);
		addUserEntry(symbol);
		addUserEntry(0);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode typeNode = childs.get(0);
		
		if (typeNode.getType() != NODE_TYPE.NULL)
		{
			return typeNode.checkIntegrity();
		}
		else
		{
			Listing.writeInternalError("Declaration node " + id + " must not have an empty type child");
			return false;
		}		
	}
}
