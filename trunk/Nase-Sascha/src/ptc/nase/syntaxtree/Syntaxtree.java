package ptc.nase.syntaxtree;

import java.io.IOException;

import ptc.nase.SymbolTable;
import ptc.nase.syntaxtree.nodes.*;
import ptc.nase.backend.Storage;
import ptc.nase.frontend.Listing;

public class Syntaxtree 
{
	private SyntaxtreeNode root;
	
	public Syntaxtree(SyntaxtreeNode sRoot)
	{
		root = sRoot;
	}

	public SyntaxtreeNode getRoot()
	{
		return root;
	}

	public void setRoot(SyntaxtreeNode root)
	{
		this.root = root;
	}
	
	public boolean checkIntegrity() throws IOException
	{
		return root.checkIntegrity();
	}
	
	public boolean allocateStorageForConstants() throws IOException
	{
		return allocateStorageForConstants(root);
	}
	
	public boolean allocateStorageForConstants(SyntaxtreeNode node) throws IOException
	{
		boolean retVal = true;
		if (node.isValid())
		{
			if (node.getType() == NODE_TYPE.CONST)
			{
				ConstNode constNode = (ConstNode) node;
				constNode.assignMemoryAdress();
			}
			
			int numberOfChilds = node.getNumberOfChilds();
			for (int i = 0; i < numberOfChilds; i++)
			{
				SyntaxtreeNode child = node.getChild(i);
				if ( allocateStorageForConstants(child) == false)
				{
					retVal = false;
				}
			}
		}
		else
		{
			Listing.writeInternalError("traversTreeAndAllocateStorageForConstants called with NULL_NODE");
			return false;
		}
		
		return retVal;
	}
	
	public boolean allocateStorageForDeclarations() throws IOException
	{
		return allocateStorageForDeclarations(root);
	}
	
	public boolean allocateStorageForDeclarations(SyntaxtreeNode node) throws IOException
	{
		boolean retVal = true;
		SyntaxtreeNode typeNode;
		
		if (node.isValid())
		{
			if (node.getType() == NODE_TYPE.DECLARATION)
			{
				typeNode = node.getChild(0);
				if (typeNode.getType() == NODE_TYPE.TYPE)
				{
					switch ( (int) typeNode.getUserEntry(0))
					{
						case SymbolTable.ST_INT_TYPE_SYMBOL:
							
							DeclarationNode declaration = (DeclarationNode) node;
							declaration.assignMemoryAdress();
							
							break;
					}
				}
				else
				{
					retVal = false;
				}
			}
			
			int numberOfChilds = node.getNumberOfChilds();
			for (int i = 0; i < numberOfChilds; i++)
			{
				SyntaxtreeNode child = node.getChild(i);
				if ( allocateStorageForDeclarations(child) == false)
				{
					retVal = false;
				}
			}
		}
		else
		{
			Listing.writeInternalError("traversTreeAndAllocateStorageForConstants called with NULL_NODE");
			return false;
		}
		
		return retVal;
	}
}
