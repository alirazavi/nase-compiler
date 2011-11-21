package ptc.nase.syntaxtree.nodes;

import java.io.IOException;
import ptc.nase.frontend.Listing;

public class ParenthesisNode extends SyntaxtreeNode 
{
	public ParenthesisNode(long sLine, long sColumn, SyntaxtreeNode intExpr)
	{
		super(NODE_TYPE.PARENTHESIS, sLine, sColumn);
		addChild(intExpr);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode intExpr = getChild(0);
		
		if (intExpr.isValid())
		{
			return intExpr.checkIntegrity();
		}
		else	// parenthesis has no subexpression
		{
			Listing.writeInternalError("Parenthesis node " + id + " must not have an empty subexpression");
			return false;
		}

	}
}
