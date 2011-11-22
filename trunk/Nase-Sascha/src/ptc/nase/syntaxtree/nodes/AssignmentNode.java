package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.frontend.Listing;

public class AssignmentNode extends SyntaxtreeNode
{
	public AssignmentNode(long sLine, long sColumn, SyntaxtreeNode identifier, SyntaxtreeNode intExpression)
	{
		super(NODE_TYPE.ASSIGNMENT, sLine, sColumn);
		addChild(identifier);
		addChild(intExpression);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode identifier = childs.get(0);
		
		if (identifier.getType() != NODE_TYPE.NULL)
		{
				if ( identifier.checkIntegrity() )
				{
					SyntaxtreeNode intExpression = childs.get(1);
					
					if (intExpression.getType() != NODE_TYPE.NULL)
					{
						return intExpression.checkIntegrity();
					}
					else // assignment has no intExpression (right hand side)
					{
						Listing.writeInternalError("Assignment node " + id + " has no intExpression");
						return false;
					}
					
				}
				else // integrity check of assignment identifier node failed
				{
					return false;
				}
		}
		else // assignment has no identifier
		{
			Listing.writeInternalError("Assignment node " + id + " has no identifier");
			return false;
		}
	}
}
