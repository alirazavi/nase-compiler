package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

public class ConstNode extends SyntaxtreeNode 
{
	public ConstNode(long sLine, long sColumn, int i)
	{
		super(NODE_TYPE.CONST, sLine, sColumn);
		addUserEntry(i);
		addUserEntry(0);	// Used later for memory adress
		addUserEntry(0);	// Used later as flag to indicate that this node was traversed one time
	}
	
	public boolean checkIntegrity() throws IOException
	{
		return true;
	}
}
