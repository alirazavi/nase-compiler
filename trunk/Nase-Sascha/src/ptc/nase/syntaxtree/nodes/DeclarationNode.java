package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.backend.Storage;
import ptc.nase.frontend.Listing;

public class DeclarationNode extends SyntaxtreeNode
{
	private boolean addressAsigned;
	@SuppressWarnings("unused")
	
	public DeclarationNode(long sLine, long sColumn, SyntaxtreeNode typeNode, int symbol)
	{
		super(NODE_TYPE.DECLARATION, sLine, sColumn);
		addChild(typeNode);
		addUserEntry(symbol);
		addUserEntry(0);
		
		addressAsigned = false;
	}
	
	public boolean assignMemoryAdress()
	{
		if (addressAsigned == false)
		{
			setUserEntry(1, Storage.getAndPushMemoryAdress());
			addressAsigned = true;
			return true;
		}
		
		return false;
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
