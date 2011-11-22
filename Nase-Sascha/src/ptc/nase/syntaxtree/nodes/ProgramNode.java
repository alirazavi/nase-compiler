package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.frontend.Listing;

public class ProgramNode extends SyntaxtreeNode
{
	public ProgramNode(long sLine, long sColumn, SyntaxtreeNode statementSequence)
	{
		super(NODE_TYPE.PROGRAM, sLine, sColumn);
		addChild(statementSequence);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode statementSequence = childs.get(0);
		
		if (statementSequence.getType() != NODE_TYPE.NULL)
		{
			return statementSequence.checkIntegrity();
		}
		else
		{
			Listing.writeInternalError("Program node " + id + " must not have an empty statementSequence");
			return false;
		}
	}
}
