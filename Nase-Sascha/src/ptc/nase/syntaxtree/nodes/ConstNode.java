package ptc.nase.syntaxtree.nodes;

import java.io.IOException;
import ptc.nase.backend.Storage;

public class ConstNode extends SyntaxtreeNode 
{
	private boolean addressAsigned;
	
	public ConstNode(long sLine, long sColumn, int i)
	{
		super(NODE_TYPE.CONST, sLine, sColumn);
		addUserEntry(i);
		addUserEntry(0);	// Used later for memory adress
		
		addressAsigned = false;
	}
	
	public boolean assignMemoryAdress()
	{
		if (addressAsigned == false)
		{
			setUserEntry(1, Storage.getAndPushConstantAdress());
			addressAsigned = true;
			return true;
		}
		
		return false;
	}
	
	public boolean checkIntegrity() throws IOException
	{
		return true;
	}
}
