package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.frontend.Listing;

public class DyadicOpNode extends SyntaxtreeNode 
{
	public DyadicOpNode(long sLine, long sColumn, SyntaxtreeNode leftHandOperand, SyntaxtreeNode rightHandOperand, int opCodeSymbol)
	{
		super(NODE_TYPE.DYADICOP, sLine, sColumn);
		addChild(leftHandOperand);
		addChild(rightHandOperand);
		addUserEntry(opCodeSymbol);	// TODO: als 3. userEntry Eintrag??? -> NO_set3rdUserEntry( pNode, (ULONG)opCodeSym );
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode leftHandOperand = getChild(0);
		
		
		if (leftHandOperand.isValid())
		{
			if (leftHandOperand.checkIntegrity())
			{
				SyntaxtreeNode rightHandOperand = getChild(1);
				if (rightHandOperand.isValid())
				{
					return rightHandOperand.checkIntegrity();
				}
				else	// dyadic node has no right hand operand
				{
					Listing.writeInternalError("Right hand side of dyadic operator node " + id + " must not be empty");
					return false;
				}
			}
			else	// integrity check of left hand operand failed
			{
				return false;
			}
			
		}
		else	// dyadic node has no left hand operand
		{
			Listing.writeInternalError("Left hand side of dyadic operator node " + id + " must not be empty");
			return false;
		}
	}
}
