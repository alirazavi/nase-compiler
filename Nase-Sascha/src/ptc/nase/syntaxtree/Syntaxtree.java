package ptc.nase.syntaxtree;

import java.io.IOException;

import ptc.nase.syntaxtree.nodes.SyntaxtreeNode;

public class Syntaxtree 
{
	private SyntaxtreeNode root;
	
	public Syntaxtree(SyntaxtreeNode sRoot)
	{
		root = sRoot;
	}

	public SyntaxtreeNode getRoot()
	{
		return root;
	}

	public void setRoot(SyntaxtreeNode root)
	{
		this.root = root;
	}
	
	public boolean checkIntegrity() throws IOException
	{
		return root.checkIntegrity();
	}
}
