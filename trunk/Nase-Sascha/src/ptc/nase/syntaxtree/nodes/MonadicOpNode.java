package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.frontend.Listing;

public class MonadicOpNode extends SyntaxtreeNode
{
	public MonadicOpNode(long sLine, long sColumn, SyntaxtreeNode operand, int opCodeSymbol)
	{
		super(NODE_TYPE.MONADICOP, sLine, sColumn);
		addChild(operand);
		addUserEntry(opCodeSymbol);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode operand = getChild(0);
		
		if (operand.isValid())
		{
			return operand.checkIntegrity();
		}
		else
		{
			Listing.writeInternalError("Operand of monadic operator node " + id + " must not be empty");
			return false;
		}
	}
}