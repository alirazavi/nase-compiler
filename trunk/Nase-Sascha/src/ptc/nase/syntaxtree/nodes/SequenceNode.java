package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.frontend.Listing;

public class SequenceNode extends SyntaxtreeNode
{
	public SequenceNode(long sLine, long sColumn, SyntaxtreeNode statement, SyntaxtreeNode sequenceNode)
	{
		super(NODE_TYPE.SEQUENCE, sLine, sColumn);
		
		addChild(statement);
		addChild(new SyntaxtreeNode());	// NULL_NODE
		
		// if this node is part of a given sequence
		if (sequenceNode.getType() != NODE_TYPE.NULL)
		{
			// iterate to the last sequence node of the chain
			// maybe unneeded (at least in some applications) !?
			while ( sequenceNode.childs.get(1).getType() != NODE_TYPE.NULL )
			{
				sequenceNode = sequenceNode.childs.get(1);
			}
			
			// add this new node to the end of the sequence chain
			sequenceNode.childs.set(1, this);
		}
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode statement = childs.get(0);
		
		if (statement.getType() != NODE_TYPE.NULL)
		{
			if ( statement.checkIntegrity() )
			{
				SyntaxtreeNode sequence = childs.get(1);
				
				if (sequence.getType() != NODE_TYPE.NULL)
				{
					return sequence.checkIntegrity();
				}
				else // sequence node NULL means end of chain
				{
					return true;
				}
			}
			else // integrity check of sequence statement node failed
			{
				return false;
			}
			
		}
		else // sequence node has no statement
		{
			Listing.writeInternalError("Sequence node " + id + " has no statement");
			return false;
		}
	}
}
