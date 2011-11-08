package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.frontend.Listing;

public class ErrorNode extends SyntaxtreeNode
{
	public ErrorNode(long sLine, long sColumn)
	{
		super(NODE_TYPE.ERROR, sLine, sColumn);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		Listing.writeGeneralContextError(line, column, "Program contains at leat one syntactic error!");
		return true;
	}
}
