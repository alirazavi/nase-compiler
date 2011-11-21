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
	
	private IScanner scanner;
	private SyntaxtreeNode nullNode;
	private SymbolTable symbolTable;
	private Syntaxtree syntaxtree;
	
	public SyntaxtreeView treeView;
	
	public Parser(IScanner sScanner)
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
	
	private SyntaxtreeNode isDeclaration() throws IOException
	{
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();	
		int currentSymbol;
		
		boolean isFirstIdentifier = true;
		
		SyntaxtreeNode typeNode, declaration, firstSequenceNode, sequenceNode;
		
		sequenceNode = nullNode;
		firstSequenceNode = nullNode;
		typeNode = isTypeName();
		
		if (typeNode.isValid())
		{
			// read all listed identifiers
			do
			{
				currentSymbol = scanner.getCurrentSymbol();
				
				if (symbolTable.isAnyIdentifierSymbol(currentSymbol))
				{
					declaration = new DeclarationNode(line, column, typeNode, currentSymbol);
					symbolTable.setSymbolNodeLink(currentSymbol, declaration);
				}
				else	// no identifier symbol followed type name
				{
					line = scanner.getCurrentLine();
					column = scanner.getCurrentColumn();
					
					generalSyntaxError(line, column, "Identifier expected after type name");
					
					// TODO: SkipToDelimiter
					
					return new ErrorNode(line, column);
				}
				
				// try to read next symbol
				if (!scanner.getNextSymbol())
				{
					syntaxErrorUnexpectedEOF();
					line = scanner.getCurrentLine();
					column = scanner.getCurrentColumn();
					
					return new ErrorNode(line, column);
				}
				
				line = scanner.getCurrentLine();
				column = scanner.getCurrentColumn();
				
				switch (scanner.getCurrentSymbol())
				{
					case SymbolTable.ST_COMMA_SYMBOL:
						
						sequenceNode = new SequenceNode(line, column, declaration, sequenceNode);
						
						if (isFirstIdentifier)
						{
							firstSequenceNode = sequenceNode;
							isFirstIdentifier = false;
						}
						
						// try to get read symbol
						if (!scanner.getNextSymbol())
						{
							syntaxErrorUnexpectedEOF();
							line = scanner.getCurrentLine();
							column = scanner.getCurrentColumn();
							
							return new ErrorNode(line, column);
						}
						
						break;
						
					case SymbolTable.ST_DELIMITER_SYMBOL:
						
						sequenceNode = new SequenceNode(line, column, declaration, sequenceNode);
						
						if (isFirstIdentifier)
						{
							firstSequenceNode = sequenceNode;
							isFirstIdentifier = false;
						}
						
						break;
						
					default:
						
						generalSyntaxError(line, column, "',' or ';' expected after an identifier in a declaration");
						// TODO: SkipToDelimiter
						return new ErrorNode(line, column);
				}
			}
			while (scanner.getCurrentSymbol() != SymbolTable.ST_DELIMITER_SYMBOL);
		}
		else
		{
			return nullNode;
		}
		
		return firstSequenceNode;
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
	
	/***
	* intTerm = isIntTerm { multOp intFactor }.
	 * @throws IOException 
	**/
	private SyntaxtreeNode isIntTerm() throws IOException
	{
		SyntaxtreeNode leftFactorNode;
		SyntaxtreeNode rightFactorNode;
		int currentSymbol;
		long line;
		long column;
		
		
		leftFactorNode = isIntFactor();
		if ( ! leftFactorNode.isValid() )
		{
			return nullNode;
		}
		
		currentSymbol = scanner.getCurrentSymbol();
		
		while (
				(currentSymbol == SymbolTable.ST_TIMES_SYMBOL)  ||
				(currentSymbol == SymbolTable.ST_DIVIDE_SYMBOL) ||
				(currentSymbol == SymbolTable.ST_MODULO_SYMBOL)
			  )
		{
			line = scanner.getCurrentLine();
			column = scanner.getCurrentColumn();
			
			scanner.getNextSymbol();
			
			rightFactorNode = isIntFactor();
			if (rightFactorNode.isValid())
			{
				leftFactorNode = new DyadicOpNode(line, column, leftFactorNode, rightFactorNode, currentSymbol);
				currentSymbol = scanner.getCurrentSymbol();
			}
			else
			{
				line = scanner.getCurrentLine();
				column = scanner.getCurrentColumn();
				generalSyntaxError(line, column, "intFactor expected after 'multOp'");
				
				// TODO: skipToDelimiter
				return new ErrorNode(line, column);				
			}
			
		}
		
		
		return nullNode;
	}
	
	private SyntaxtreeNode isIntFactor()
	{
		return nullNode;
	}
	
	private SyntaxtreeNode isInteger() throws IOException
	{
		int symbol = scanner.getCurrentSymbol();
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();	
		
		if (symbolTable.isAnyNumericSymbol(symbol))
		{
			scanner.getNextSymbol();
			
			// check if numeric symbol already has an const node linked to
			SyntaxtreeNode constNode = symbolTable.getSymbolNodeLink(symbol);
			
			if ( ! constNode.isValid() )
			{
				// if no -> create a new one and link the symbol to it
				constNode = new ConstNode(line, column, symbol);
				symbolTable.setSymbolNodeLink(symbol, constNode);
			}
			
			return constNode;
		}
		
		
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
		switch (symbol)
		{
			case SymbolTable.ST_LT_SYMBOL:
			case SymbolTable.ST_LE_SYMBOL:
			case SymbolTable.ST_EQ_SYMBOL:
			case SymbolTable.ST_GE_SYMBOL:
			case SymbolTable.ST_GT_SYMBOL:
			case SymbolTable.ST_NE_SYMBOL:
				
				return true;
				
			default:
				
				return false;
				
		}
	}
	
	private void generalSyntaxError(long line, long column, String message)
	{
		
	}
	
	private void syntaxErrorUnexpectedEOF()
	{
		
	}
}
