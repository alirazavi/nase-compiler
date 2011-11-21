package ptc.nase.syntaxtree.nodes;

import java.io.IOException;
import ptc.nase.frontend.Listing;

public class InlineIfNode extends SyntaxtreeNode 
{
	public InlineIfNode(long sLine, long sColumn, SyntaxtreeNode boolExpression, SyntaxtreeNode thenIntExpression, SyntaxtreeNode elseIntExpression)
	{
		super(NODE_TYPE.INLINEIF, sLine, sColumn);
		addChild(boolExpression);
		addChild(thenIntExpression);
		addChild(elseIntExpression);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode boolExpression = getChild(0);

		// ----------- CHECK BOOL EXPRESSION ----------- 
		if (boolExpression.isValid())
		{
			if (boolExpression.checkIntegrity())
			{
				// ----------- CHECK THEN EXPRESSION ----------- 
				SyntaxtreeNode thenIntExpression = getChild(1);
				if (thenIntExpression.isValid())
				{
					if (thenIntExpression.checkIntegrity())
					{
						// ----------- CHECK ELSE EXPRESSION ----------- 
						SyntaxtreeNode elseIntExpression = getChild(2);
						if (elseIntExpression.isValid())
						{
							return elseIntExpression.checkIntegrity();
						}
						else	// inline-if has no elseIntExpression
						{
							Listing.writeInternalError("elseIntExpression node of inline-if node " + id + " must not be empty");
							return false;
						}
						
					}
					else	// integrity check of thenIntExpression failed
					{
						return false;
					}
					
				}
				else	// inline-if has no thenIntExpression
				{
					Listing.writeInternalError("thenIntExpression node of inline-if node " + id + " must not be empty");
					return false;
				}
			}
			else	// integrity check of boolExpression failed
			{
				return false;
			}
			
		}
		else	// inline-if has no bool expression
		{
			Listing.writeInternalError("Boolexpression node of inline-if node " + id + " must not be empty");
			return false;
		}
	}
}
