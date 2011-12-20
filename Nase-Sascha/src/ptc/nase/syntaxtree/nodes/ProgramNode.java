package ptc.nase.syntaxtree.nodes;

import java.io.IOException;

import ptc.nase.backend.CodeFile;
import ptc.nase.backend.Macro;
import ptc.nase.frontend.Listing;

public class ProgramNode extends SyntaxtreeNode
{
	public ProgramNode(long sLine, long sColumn, SyntaxtreeNode statementSequence)
	{
		super(NODE_TYPE.PROGRAM, sLine, sColumn);
		addChild(statementSequence);
	}
	
	public boolean checkIntegrity() throws IOException
	{
		SyntaxtreeNode statementSequence = childs.get(0);
		
		if (statementSequence.getType() != NODE_TYPE.NULL)
		{
			return statementSequence.checkIntegrity();
		}
		else
		{
			Listing.writeInternalError("Program node " + id + " must not have an empty statementSequence");
			return false;
		}
	}
	
	public void generateCode() throws IOException
	{
		super.generateCode();
		
		CodeFile.writeCode(Macro.environment());
		CodeFile.writeCode(Macro.openConstDefinitionTable());
		
		CodeFile.writeCode(Macro.programStart());
		
		//TODO
		/*
		 * 	SY_CD_traverseTreeAndCodeConstantNodes( pNode, MA_defineConstValue );
			MA_allocateStaticMemory( SO_getNextFreeMemoryAdress() );
		 */
		
		SyntaxtreeNode statementSequence = childs.get(0);
		
		statementSequence.generateCode();
		
		CodeFile.writeCode(Macro.programEnd());
	}
}
