package ptc.nase.syntaxtree.nodes;

import java.io.IOException;
import java.util.ArrayList;
import ptc.nase.backend.*;


@SuppressWarnings("unused")
public class SyntaxtreeNode
{
	
	private NODE_TYPE type;
	
	private SyntaxtreeNode parent;
	private int symbol;
	
	protected int id;
	private static int idCount = 1;
	
	protected long line;
	protected long column;
	
	protected ArrayList<SyntaxtreeNode> childs;
	protected ArrayList<Long> userEntries;
	
	/*
	 * Default constructor constructs the NULL node
	 */
	public SyntaxtreeNode()
	{
		type = NODE_TYPE.NULL;
		line = -1;
		column = -1;
		
		childs = null;
		userEntries = null;
		id = 0;	// each NULL node has id 0
	}
	
	protected SyntaxtreeNode(NODE_TYPE sType, long sLine, long sColumn)
	{
		type = sType;
		line = sLine;
		column = sColumn;

		childs = null;
		userEntries = null;
		
		id = idCount++;
	}
	
	public NODE_TYPE getType()
	{
		return type;
	}
	
	public SyntaxtreeNode getChild(int index)
	{
		if (childs != null && index < childs.size())
		{
			return childs.get(index);
		}
		else
		{
			return new SyntaxtreeNode();
		}
	}
	
	public int getNumberOfChilds()
	{
		if (childs != null)
			return childs.size();
		else
			return 0;
	}
	
	public long getUserEntry(int index)
	{
		if (userEntries != null && index < userEntries.size() )
		{
			return userEntries.get(index);
		}
		else
		{
			return -1;
		}		
	}
	
	protected void setUserEntry(int index, long entry)
	{
		if (userEntries != null && index < userEntries.size() )
		{
			userEntries.set(index, entry);
		}		
	}
	
	protected void addChild(SyntaxtreeNode newChild)
	{
		if (childs == null)
		{
			childs = new ArrayList<SyntaxtreeNode>();
		}
		childs.add(newChild);
	}
	
	protected void addUserEntry(long entry)
	{
		if (userEntries == null)
		{
			userEntries = new ArrayList<Long>();
		}
		userEntries.add(entry);
	}	
	
	public boolean checkIntegrity() throws IOException
	{
		return true;
	}
	
	public boolean isValid()
	{
		return type != NODE_TYPE.NULL;
	}
	
	public int getID()
	{
		return id;
	}
	
	public String toString()
	{
		String ret = id + ": " + type.toString() +  " (" + line + "," + column + ")";
		
		if (userEntries != null)
		{
			for (int i = 0; i < userEntries.size(); i++)
			{
				ret += "\nEntry " + i + " = " + userEntries.get(i); 
			}
		}
		
		return ret;
	}
	
	public void generateCode() throws IOException
	{
		String code = Macro.comment("POSITION: " + line + "/" + column + ": begin coding of " + type.toString() + " node (" + id + ")");
		CodeFile.writeCode(code);
	}
	
}
