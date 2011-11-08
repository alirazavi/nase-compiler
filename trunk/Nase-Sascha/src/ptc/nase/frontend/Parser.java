package ptc.nase.frontend;

import java.io.IOException;

import javax.swing.JFrame;

import ptc.nase.SymbolTable;
import ptc.nase.SyntaxtreeView;
import ptc.nase.exceptions.GeneralSyntaxError;
import ptc.nase.syntaxtree.*;
import ptc.nase.syntaxtree.nodes.*;

@SuppressWarnings("unused")
public class Parser 
{
	
	private final boolean DEBUG_PARSER = true; 
	
	private Scanner scanner;
	private SyntaxtreeNode nullNode;
	private SymbolTable symbolTable;
	private Syntaxtree syntaxtree;
	
	public SyntaxtreeView treeView;
	
	public Parser(Scanner sScanner)
	{
		scanner = sScanner;
		nullNode = new SyntaxtreeNode();
		symbolTable = scanner.getSymbolTable();
		syntaxtree = null;
	}
	
	private SyntaxtreeNode isRead() throws IOException
	{
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();
		SyntaxtreeNode identifierNode;
		
		if (scanner.getCurrentSymbol() == SymbolTable.ST_READ_SYMBOL)
		{
			scanner.getNextSymbol();
			identifierNode = isIdentifier();
			
			if (identifierNode.getType() != NODE_TYPE.NULL)
			{
				return new ReadNode(line, column, identifierNode);
			}
			else
			{
				line = scanner.getCurrentLine();
				column = scanner.getCurrentColumn();
				
				// TODO: skip to delimiter
				
				return new ErrorNode(line, column);
			}
		}
		
		return nullNode;
	}
	
	private SyntaxtreeNode isWrite() throws IOException
	{
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();
		SyntaxtreeNode identifierNode;
		
		if (scanner.getCurrentSymbol() == SymbolTable.ST_WRITE_SYMBOL)
		{
			
			scanner.getNextSymbol();
			identifierNode = isIdentifier();
			
			if (identifierNode.getType() == NODE_TYPE.IDENTIFIER)
			{
				return new WriteNode(line, column, identifierNode);
			}
			else
			{
				line = scanner.getCurrentLine();
				column = scanner.getCurrentColumn();
				
				// TODO: skip to delimiter
				
				return new ErrorNode(line, column);
			}
		}
		
		return nullNode;
	}	
	
	private SyntaxtreeNode isIdentifier() throws IOException
	{
		int currentSymbol;
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();
		
		currentSymbol = scanner.getCurrentSymbol();
		
		if (symbolTable.isAnyIdentifierSymbol(currentSymbol))
		{
			scanner.getNextSymbol();
			
			return new IdentifierNode(line, column, currentSymbol);
		}
		
		return nullNode;
	}
	
	private SyntaxtreeNode isStatement() throws IOException
	{
		SyntaxtreeNode statement = nullNode;
		
		statement = isDeclaration();
		if ( !statement.isValid() )
		{
			statement = isAssignment();
			if ( !statement.isValid() )
			{
				statement = isRead();
				if ( !statement.isValid() )
				{
					statement = isWrite();
					if ( !statement.isValid() )
					{
						if ( isEOFSymbol() )
						{
							return nullNode;
						}
						else	// statement expected
						{
							expectedSymbolError("Statement expected");
							
							return nullNode;
						}
					}
				}
			}
		}
		
		// declaration | assignment | read | write
		// check for expected delimiter
		
		if (scanner.getCurrentSymbol() == SymbolTable.ST_DELIMITER_SYMBOL)
		{
			scanner.getNextSymbol();
			return statement;
		}
		else	// ; expected
		{
			return expectedSymbolError("';' expected");
		}
		
	}		
	
	private SyntaxtreeNode isStatementSequence() throws IOException
	{
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();
		SyntaxtreeNode statement = nullNode;
		SyntaxtreeNode statementSequence = nullNode;
		SyntaxtreeNode firstStatementSequence = nullNode;
		
		statement = isStatement();
		if (statement.isValid())
		{
			statementSequence = new SequenceNode(line, column, statement, statementSequence);
		}
		else
		{
			return nullNode;
		}
		
		firstStatementSequence = statementSequence;
		
		statement = isStatement();

		while ( statement.isValid() )
		{
			line = scanner.getCurrentLine();
			column = scanner.getCurrentColumn();
			statementSequence = new SequenceNode(line, column, statement, statementSequence);
			
			statement = isStatement();
		}
		
		return firstStatementSequence;
	}
	
	private SyntaxtreeNode isProgram() throws IOException, GeneralSyntaxError
	{
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();
		SyntaxtreeNode statementSequence = nullNode;
		
		scanner.getNextSymbol();
		
		statementSequence = isStatementSequence();
		
		if ( statementSequence.isValid() )
		{
			if ( isEOFSymbol() )
			{
				return new ProgramNode(line, column, statementSequence);
			}
			else
			{
				// TODO: throw --> dumme Idee !11elf
				//throw new GeneralSyntaxError(line, column, "Missing EOF at program end");
				
				Listing.writeInternalError("Missing EOF at program end");
			}
		}
		
		return nullNode;
	}
	
	public boolean parseProgram() throws IOException, GeneralSyntaxError
	{
		SyntaxtreeNode rootNode;
		
		Listing.write("");
		
		rootNode = isProgram();
		
		if (DEBUG_PARSER)
		{
			symbolTable.dumpSymbolTable();		
		}
		
		if (rootNode.isValid())
		{		
			syntaxtree = new Syntaxtree(rootNode);
			
			if (DEBUG_PARSER)
			{
				treeView = new SyntaxtreeView(syntaxtree);
				treeView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				treeView.setSize(1024, 768);
				treeView.setVisible(true);
			}
		}
		else
		{
			return false;
		}
		
		return true;
	}
	
	private SyntaxtreeNode isDeclaration()
	{
		
		// TODO: nach new Declaration(...) --> ST_setDeclarationNodeLinkToSymbol( sym, pNode );
		
		return nullNode;
	}
	
	private SyntaxtreeNode isTypeName() throws IOException
	{
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();		
		
		if (scanner.getCurrentSymbol() == SymbolTable.ST_INT_TYPE_SYMBOL)
		{
			scanner.getNextSymbol();
			return new TypeNode(line, column, SymbolTable.ST_INT_TYPE_SYMBOL);
		}
		
		return nullNode;
	}
	
	private SyntaxtreeNode isAssignment() throws IOException
	{
		long line;
		long column;		
		
		SyntaxtreeNode identifier;
		SyntaxtreeNode intExpression;
		
		identifier = isIdentifier();
		if (identifier.getType() != NODE_TYPE.NULL)
		{
			line = scanner.getCurrentLine();
			column = scanner.getCurrentColumn();
			
			if (scanner.getCurrentSymbol() == SymbolTable.ST_ASSIGN_SYMBOL)
			{
				scanner.getNextSymbol();
				intExpression = isIntExpression();
				
				if ( intExpression.isValid() )
				{
					return new AssignmentNode(line, column, identifier, intExpression);
				}
				else	// integer expression expected
				{
					return expectedSymbolError("Expected integer expression on right hand side of ':='");
				}
				
			}
			else	// := expected
			{
				return expectedSymbolError("':=' expected");
			}
			
		}
		
		return nullNode;
	}
	
	private SyntaxtreeNode isIntExpression()
	{
		return nullNode;
	}
	
	
	private SyntaxtreeNode isIntTerm()
	{
		return nullNode;
	}
	
	private SyntaxtreeNode isIntFactor()
	{
		return nullNode;
	}
	
	private SyntaxtreeNode isInteger()
	{
		return nullNode;
	}
	
	private SyntaxtreeNode isInlineIf()
	{
		return nullNode;
	}
	
	private SyntaxtreeNode isBoolExpression()
	{
		return nullNode;
	}	
	
	private boolean isEOFSymbol()
	{
		return scanner.getCurrentSymbol() == SymbolTable.ST_EOF_SYMBOL;
	}
	
	private SyntaxtreeNode expectedSymbolError(String message)
	{
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();
		
		generalSyntaxError(line, column, message);
		
		// TODO: skipToDelimiter
		
		return new ErrorNode(line, column);		
	}
	
	private boolean isRelationOpSymbol(int symbol)
	{
		return
		(
				(symbol == SymbolTable.ST_LT_SYMBOL) ||
				(symbol == SymbolTable.ST_LE_SYMBOL) ||
				(symbol == SymbolTable.ST_EQ_SYMBOL) ||
				(symbol == SymbolTable.ST_GE_SYMBOL) ||
				(symbol == SymbolTable.ST_GT_SYMBOL) ||
				(symbol == SymbolTable.ST_NE_SYMBOL)
		);
	}
	
	private void generalSyntaxError(long line, long column, String message)
	{
		
	}
}
