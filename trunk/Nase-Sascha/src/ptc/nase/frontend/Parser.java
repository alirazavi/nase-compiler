package ptc.nase.frontend;

import java.io.IOException;

import javax.swing.JFrame;

import ptc.nase.SymbolTable;
import ptc.nase.SyntaxtreeView;
import ptc.nase.syntaxtree.*;
import ptc.nase.syntaxtree.nodes.*;

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
	
	/***
	* read = READ_SYMBOL identifier.
	*/
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
				
				scanner.skipToDelimiter();
				
				return new ErrorNode(line, column);
			}
		}
		
		return nullNode;
	}
	
	/***
	* write = WRITE_SYMBOL identifier.
	*/
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
				
				scanner.skipToDelimiter();
				
				return new ErrorNode(line, column);
			}
		}
		
		return nullNode;
	}	
	
	/***
	* identifier = ANY_LETTER { ANY_DIGIT | ANY_LETTER }.
	**/
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
	
	/***
	* statement = [ declaration | assignment | read | write ] DELIMITER_SYMBOL.
	**/
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
	
	/***
	* statementSequence = statement { statement }.
	**/
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
	
	/***
	* program = statementSequence EOF_SYMBOL.
	**/
	private SyntaxtreeNode isProgram() throws IOException
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
	
	/***
	* declaration = typeName identifier { COMMA_SYMBOL identifier }.
	**/
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
					return expectedSymbolError("Identifier expected after type name");
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
						
						return expectedSymbolError("',' or ';' expected after an identifier in a declaration");
						
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
	
	/***
	* typeName = INT_TYPE_SYMBOL.
	**/
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
	
	/***
	* assignment = identifier ASSIGN_SYMBOL intExpr.
	*/
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
	
	/***
	* intExpr = [ MINUS_SYMBOL ] term { addOp term }.
	 * @throws IOException 
	**/
	private SyntaxtreeNode isIntExpression() throws IOException
	{
		boolean minusFlag = false;
		SyntaxtreeNode leftTermNode, minusPrefixNode, rightTermNode, newTermNode;
		long minusSignLine, minusSignColumn;
		int currentSymbol;
		
		long line;
		long column;
		
		minusSignLine = scanner.getCurrentLine();
		minusSignColumn = scanner.getCurrentColumn();
		
		if (scanner.getCurrentSymbol() == SymbolTable.ST_MINUS_SYMBOL)
		{
			scanner.getNextSymbol();
			minusFlag = true;
		}
		
		leftTermNode = isIntTerm();
		
		if (!leftTermNode.isValid())
		{
			if(minusFlag)
			{
				return expectedSymbolError("intTerm expected after '-'");
					
			}
			else
			{
				return nullNode;
			}
		}
		
		if (minusFlag)
		{
			minusPrefixNode = new MonadicOpNode(minusSignLine, minusSignColumn, leftTermNode, SymbolTable.ST_MINUS_SYMBOL);
			leftTermNode = minusPrefixNode;
		}
		
		currentSymbol = scanner.getCurrentSymbol();
		
		while ( (currentSymbol == SymbolTable.ST_MINUS_SYMBOL) || (currentSymbol == SymbolTable.ST_PLUS_SYMBOL) )
		{
			line = scanner.getCurrentLine();
			column = scanner.getCurrentColumn();
			scanner.getNextSymbol();
			
			rightTermNode = isIntTerm();
			
			if (rightTermNode.isValid())
			{
				newTermNode = new DyadicOpNode(line, column, leftTermNode, rightTermNode, currentSymbol);
				leftTermNode = newTermNode;
				
				currentSymbol = scanner.getCurrentSymbol();
			}
			else
			{
				return expectedSymbolError("intTerm expected after 'addOp'");
			}
		}
		
		return leftTermNode;
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
				return expectedSymbolError("intFactor expected after 'multOp'");		
			}
			
		}
		
		
		return nullNode;
	}
	
	/***
	* intFactor = integer | identifier | OPEN_PARAENTHESIS_SYMBOL intExpr CLOSE_PARENTHESIS_SYMBOL | inlineIfStatement.
	 * @throws IOException 
	**/
	private SyntaxtreeNode isIntFactor() throws IOException
	{
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();
		SyntaxtreeNode intFactor, intExpression;
		
		intFactor = isInteger();
		if (intFactor.isValid())
		{
			return intFactor;
		}
		else
		{
			intFactor = isIdentifier();
			if (intFactor.isValid())
			{
				return intFactor;
			}
			else
			{
				if (scanner.getCurrentSymbol() == SymbolTable.ST_OPEN_PARENTHESIS_SYMBOL)
				{
					scanner.getNextSymbol();
					
					intExpression = isIntExpression();
					if (intExpression.isValid())
					{
						if (scanner.getCurrentSymbol() == SymbolTable.ST_CLOSE_PARENTHESIS_SYMBOL)
						{
							scanner.getNextSymbol();
							
							return new ParenthesisNode(line, column, intExpression);
						}
						else	// no closing parenthesis symbol
						{
							return expectedSymbolError("')' expected after '(' intExpr");
						}
					}
					else	// no intExpression after (
					{
						return expectedSymbolError("intExpression expected after (");
					}
					
				}
				else
				{
					intFactor = isInlineIf();
					if (intFactor.isValid())
					{
						return intFactor;
					}
					else // no intFactor rule hit
					{
						return nullNode;	// ?
						//return expectedSymbolError("intFactor expected"); 
					}
				}
			}
		}
	}
	
	/***
	* integer = ANY_DIGIT { ANY_DIGIT }.
	**/
	private SyntaxtreeNode isInteger() throws IOException
	{
		int symbol = scanner.getCurrentSymbol();
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();	
		
		if (symbolTable.isAnyNumericSymbol(symbol))
		{
			scanner.getNextSymbol();
			
			// check if numeric symbol already has a const node linked to
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
	
	/***
	* inlineIfStatement = INLINE_IF_SYMBOL boolExpr INLINE_THEN_SYMBOL intExpr INLINE_ELSE_SYMBOL intExpr INLINE_FI_SYMBOL.
	**/
	private SyntaxtreeNode isInlineIf() throws IOException
	{
		SyntaxtreeNode boolExpression, thenIntExpression, elseIntExpression;
		
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();			
		
		if (scanner.getCurrentSymbol() == SymbolTable.ST_INLINE_IF_SYMBOL)
		{
			scanner.getNextSymbol();
			
			boolExpression = isBoolExpression();
			if (boolExpression.isValid())
			{
				if (scanner.getCurrentSymbol() == SymbolTable.ST_INLINE_THEN_SYMBOL)
				{
					thenIntExpression = isIntExpression();
					if (thenIntExpression.isValid())
					{
						if (scanner.getCurrentSymbol() == SymbolTable.ST_INLINE_ELSE_SYMBOL)
						{
							elseIntExpression = isIntExpression();
							if (elseIntExpression.isValid())
							{
								if (scanner.getCurrentSymbol() == SymbolTable.ST_INLINE_FI_SYMBOL)
								{
									scanner.getNextSymbol();
									return new InlineIfNode(line, column, boolExpression, thenIntExpression, elseIntExpression);
								}
								else // no FI symbol
								{
									return expectedSymbolError("'FI' expected after ':' intExpr");
								}
							}
							else // no else-intExpr
							{
								return expectedSymbolError("intExpr expected after ':'");
							}
						}
						else // no else symbol
						{
							return expectedSymbolError("':' expected after '?' intExpr");
						}
					}
					else // no then-intExpr
					{
						return expectedSymbolError("intExpr expected after '?'");
					}
				}
				else	// no then symbol
				{
					return expectedSymbolError("'?' expected after 'IIF' boolExpr");
				}	
			}
			else // no boolExpression after inline if
			{
				return expectedSymbolError("boolExpr expected after 'IIF'");
			}		
		}
		else	// currentSymbol != ST_INLINE_IF_SYMBOL
		{
			return nullNode;
		}
	}
	
	/***
	* boolExpr = intExpr relationOp intExpr { boolOp intExpr relationOp intExpr }.
	 * @throws IOException 
	**/
	private SyntaxtreeNode isBoolExpression() throws IOException
	{
		
		SyntaxtreeNode leftIntExpression, rightIntExpression;
		SyntaxtreeNode newBoolExpression1, newBoolExpression2, newBoolExpression3;
		long line;
		long column;
		int currentSymbol, currentSymbolForBoolOp;
		
		leftIntExpression = isIntExpression();
		if (leftIntExpression.isValid())
		{
			line = scanner.getCurrentLine();
			column = scanner.getCurrentColumn();
			currentSymbol = scanner.getCurrentSymbol();
			
			if (isRelationOpSymbol(currentSymbol))
			{
				rightIntExpression = isIntExpression();
				if (rightIntExpression.isValid())
				{
					newBoolExpression1 = new DyadicOpNode(line, column, leftIntExpression, rightIntExpression, currentSymbol);
				}
				else	// no intExpr after relationOp
				{
					return expectedSymbolError("intExpr expected after relationOp");
				}
			}
			else	// no relationOp after intExpr
			{
				return expectedSymbolError("'relationOp' expected after intExpr");
			}
			
		}
		else
		{
			return nullNode;
		}
	
		currentSymbolForBoolOp = scanner.getCurrentSymbol();
		
		while ( 
				(currentSymbolForBoolOp == SymbolTable.ST_AND_SYMBOL) ||
				(currentSymbolForBoolOp == SymbolTable.ST_OR_SYMBOL)
			  )
		{
			
			line = scanner.getCurrentLine();
			column = scanner.getCurrentColumn();
			scanner.getNextSymbol();
			
			leftIntExpression = isIntExpression();
			if (leftIntExpression.isValid())
			{
				currentSymbol = scanner.getCurrentSymbol();
				if (isRelationOpSymbol(currentSymbol))
				{
					scanner.getNextSymbol();
					
					rightIntExpression = isIntExpression();
					if (rightIntExpression.isValid())
					{
						newBoolExpression2 = new DyadicOpNode(line, column, leftIntExpression, rightIntExpression, currentSymbol);
						newBoolExpression3 = new DyadicOpNode(line, column, newBoolExpression1, newBoolExpression2, currentSymbolForBoolOp);
						newBoolExpression1 = newBoolExpression3;
						
						currentSymbolForBoolOp = scanner.getCurrentSymbol();
					}
					else	// no intExpr after relationOp	
					{
						return expectedSymbolError("intExpr expected after relationOp");
					}
				}
				else	// no relationOp after intExpr
				{
					return expectedSymbolError("'relationOp' expected after intExpr");
				}
			}
			else	// no intExpr after boolOp
			{
				return expectedSymbolError("intExpr expected after boolOp");
			}
			
		}
		
		return newBoolExpression1;
	}	
	
	/***
	* checks for EOF_SYMBOL
	**/
	private boolean isEOFSymbol()
	{
		return scanner.getCurrentSymbol() == SymbolTable.ST_EOF_SYMBOL;
	}
	
	private SyntaxtreeNode expectedSymbolError(String message)
	{
		long line = scanner.getCurrentLine();
		long column = scanner.getCurrentColumn();
		
		generalSyntaxError(line, column, message);
		
		scanner.skipToDelimiter();
		
		return new ErrorNode(line, column);		
	}
	
	/***
	* checks for one of the relational Op's
	* relationOp = LT_SYMBOL | LE_SYMBOL | EQ_SYMBOL | GE_SYMBOL | GT_SYMBOL | NE_SYMBOL.
	**/
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
	
	public boolean parseProgram() throws IOException
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
			
			syntaxtree.checkIntegrity();
			
			if (DEBUG_PARSER)
			{
				treeView = new SyntaxtreeView(syntaxtree, scanner.getSymbolTable());
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
	
	private void generalSyntaxError(long line, long column, String message)
	{
		
	}
	
	private void syntaxErrorUnexpectedEOF()
	{
		
	}
}
