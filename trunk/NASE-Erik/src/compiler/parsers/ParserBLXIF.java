package compiler.parsers;

import symboltable.SymbolTable;
import symboltable.Symbols;
import syntaxtree.nodes.AssignmentNode;
import syntaxtree.nodes.Block;
import syntaxtree.nodes.ConstantNode;
import syntaxtree.nodes.DyadicOpNode;
import syntaxtree.nodes.ErrorNode;
import syntaxtree.nodes.IfNode;
import syntaxtree.nodes.MondanicOpNode;
import syntaxtree.nodes.Node;
import syntaxtree.nodes.ParenthesisNode;
import syntaxtree.nodes.ProgrammNode;
import syntaxtree.nodes.SequenceNode;
import syntaxtree.nodes.TypeNameNode;

import compiler.scanner.ScannerWrapper;

public class ParserBLXIF extends Parser{

	public ParserBLXIF(ScannerWrapper scanner) {
		super(scanner);		
	}

	Node getErrorNodeBlock(String message){
		int column = scanner.getColumn();
		int row = scanner.getLine();
		reportGeneralSyntaxError(row, column, message);
		scanner.skipBlockEndSymbol();
		return new ErrorNode(row, column);
	}
	
	@Override
	Node isTypeName(){
		Node type = NULLNODE;
		int column = scanner.getColumn();
		int row = scanner.getLine();		
		
		switch(scanner.getCurrentSymbol()){
			case Symbols.INT_TYPE_SYMBOL:
			case Symbols.BOOL_TYPE_SYMBOL:
				type = new TypeNameNode(column, row, scanner.getCurrentSymbol());
				scanner.getNextSymbol();
				break;					
		}
		
		return type;
	}
		
	/*
	 * boolConstDenotation = TRUE | FALSE
	 */
	protected Node isBoolConstDenotation(){
		
		Node constNode;
		int currentSymbol = scanner.getCurrentSymbol();
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		if(currentSymbol == Symbols.TRUE_SYMBOL || currentSymbol == Symbols.FALSE_SYMBOL){
			scanner.getNextSymbol();
			constNode = SymbolTable.getInstance().getNodeLink(currentSymbol);
			if(NULLNODE.equals(constNode)){
				constNode = new ConstantNode(line, column, currentSymbol);
				SymbolTable.getInstance().setNodeLink(currentSymbol, constNode);
			}
			return constNode;
		}
		return NULLNODE;
	}
	
	/*
	 * boolFactor = TRUE_SYMBOL | FALSE_SYMBOL | [NOT_SYMBOL] identifier | [NOT_SYMBOL] intExpr relationOp intExpr |
	 *              [NOT_SYMBOL] OPEN_PARENTHESIS_SYMBOL boolExpr CLOSE_PARENTHESIS_SYMBOL.
	 */
	protected Node isBoolFactor(){
		Node leftIntExpr;
		Node rightIntExpr;
		
		Node boolFactor;
		Node boolExpr;
		Node notPrefix;
		int line = scanner.getLine(); 
		int column = scanner.getColumn();
		int symbol;
		int nextSymbol;
		boolean notFlag = false;
		boolean intExprMayFollow = false;
		boolean intExprMustFollow = false;
		
		if(!NULLNODE.equals(boolFactor = isBoolConstDenotation()))
			return boolFactor;
		else {
			symbol = scanner.getCurrentSymbol();
			
			if(symbol == Symbols.NOT_SYMBOL){
				scanner.getNextSymbol();
				notFlag = true;
			}
			
			if(scanner.getCurrentSymbol() == Symbols.OPEN_PARAENTHESIS_SYMBOL){
				scanner.getNextSymbol();
				symbol = scanner.getCurrentSymbol();
				if(!NULLNODE.equals(boolExpr = isBoolExpr())){
					symbol = scanner.getCurrentSymbol();
					if(symbol == Symbols.CLOSE_PARENTHESIS_SYMBOL){
						scanner.getNextSymbol();
						boolFactor = new ParenthesisNode(line, column, boolExpr);
						if(notFlag){
							notPrefix = new MondanicOpNode(line, column, Symbols.NOT_SYMBOL, boolFactor);
							boolFactor = notPrefix;
						} else
							return getErrorNode("')' expected");
					} else
						return getErrorNode("')' expected");
				} else 
					return getErrorNode("boolExpr expected after '('");
			} else {
				if(SymbolTable.getInstance().isAnyIdentifierSymbol(symbol)){
					nextSymbol = scanner.lookAheadOneSymbol();
					if(nextSymbol == Symbols.OR_SYMBOL || nextSymbol == Symbols.AND_SYMBOL
							|| nextSymbol == Symbols.INLINE_IF_SYMBOL || nextSymbol == Symbols.THEN_SYMBOL
							|| nextSymbol == Symbols.CLOSE_PARENTHESIS_SYMBOL || nextSymbol == Symbols.DELIMITER_SYMBOL){
						if(!NULLNODE.equals(boolFactor = isIdentifier())){
							if(notFlag){
								notPrefix = new MondanicOpNode(line, column, Symbols.NOT_SYMBOL, boolFactor);
								boolFactor = notPrefix;
							}
							return boolFactor;
						} else
							return getErrorNode("an identifier was promised but could not be parsed");						
					} else
						intExprMayFollow = true;
				} else if(SymbolTable.getInstance().isAnyNumericSymbol(symbol))
					intExprMustFollow = true;
			}
			if(intExprMayFollow || intExprMustFollow){
				if(!NULLNODE.equals(leftIntExpr = isIntExpr())){
					symbol = scanner.getCurrentSymbol();
					if(isRelationalOp(symbol)){
						scanner.getNextSymbol();
						if(!NULLNODE.equals(rightIntExpr = isIntExpr())){
							boolFactor = new DyadicOpNode(line, column, symbol, leftIntExpr, rightIntExpr);
							if(notFlag){
								notPrefix = new MondanicOpNode(line, column, Symbols.NOT_SYMBOL, boolFactor);
								boolFactor = notPrefix;
							}
							return boolFactor;
						} else
							return getErrorNode("'intExpr' expected after 'relationOp'");
					} else
						return getErrorNode("'relationOp' expected after 'intExpr'");
				} else {
					if(intExprMustFollow)
						return getErrorNode("'intExpr' after integer expected");
					else
						return getErrorNode("'boolExpr' expected");
				}
			} else
				return getErrorNode("'boolExpr' expected");
		}
	}
	
	/*
	 * boolTerm = boolFactor {AND_SYMBOL boolFactor}.
	 */
	protected Node isBoolTerm(){
		Node leftFactor;
		Node rightFactor;
		Node newFactor;
		int currentSymbol;
		
		if(NULLNODE.equals(leftFactor = isBoolFactor()))
			return NULLNODE;
		
		currentSymbol = scanner.getCurrentSymbol();
		while(currentSymbol == Symbols.AND_SYMBOL){
			int line = scanner.getLine();
			int column = scanner.getColumn();
			scanner.getNextSymbol();
			if(NULLNODE.equals(rightFactor = isBoolFactor()))
				return getErrorNode("boolFactor expected after 'AND'");
			else {
				newFactor = new DyadicOpNode(line, column, currentSymbol, leftFactor, rightFactor);
				leftFactor = newFactor;
				currentSymbol = scanner.getCurrentSymbol();
			}
		}
		return leftFactor;
	}
	
	/*
	 * boolExpr = [NOT_SYMBOL] boolTerm { OR_SYMBOL [NOT_SYMBOL] boolTerm }.
	 */
	@Override
	Node isBoolExpr(){
		Node leftTerm;
		Node rightTerm;
		Node notPrefix;
		Node newTerm;
		
		int lineNot = scanner.getLine();
		int columnNot = scanner.getColumn();
		int currentSymbol = scanner.getCurrentSymbol();
		boolean notFlag = false;
		
		if(currentSymbol == Symbols.NOT_SYMBOL){
			scanner.getNextSymbol();
			notFlag  = true;
		}
		
		if(NULLNODE.equals(leftTerm = isBoolTerm())){
			if(!notFlag)
				return NULLNODE;
			else
				return getErrorNode("boolTerm expected after 'NOT'");
		}
		
		if(notFlag){
			notPrefix = new MondanicOpNode(lineNot, columnNot, Symbols.NOT_SYMBOL, leftTerm);
			leftTerm = notPrefix;
		}
		
		currentSymbol = scanner.getCurrentSymbol();
		while(currentSymbol == Symbols.OR_SYMBOL){
			int line = scanner.getLine();
			int column = scanner.getColumn();
			scanner.getNextSymbol();
			
			lineNot = scanner.getLine();
			columnNot = scanner.getColumn();
			
			if(scanner.getCurrentSymbol() == Symbols.NOT_SYMBOL){
				scanner.getNextSymbol();
				notFlag = true;
			} else
				notFlag = false;
			
			if(NULLNODE.equals(rightTerm = isBoolTerm()))
				return getErrorNode("boolTerm expected after 'OR'");
			else {
				if(notFlag){
					notPrefix = new MondanicOpNode(lineNot, columnNot, Symbols.NOT_SYMBOL, rightTerm);
					rightTerm = notPrefix;
				}
				newTerm = new DyadicOpNode(line, column, currentSymbol, leftTerm, rightTerm);
				leftTerm = newTerm;
				currentSymbol= scanner.getCurrentSymbol();
			}
		}
		return leftTerm;
	}
		
	protected Node isIfStmt(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		Node boolExpr;
		Node thenStmt;
		Node elseStmt;
		
		if(Symbols.IF_SYMBOL == scanner.getCurrentSymbol()){
			scanner.getNextSymbol();
			if(!NULLNODE.equals(boolExpr = isBoolExpr())){
				if(Symbols.THEN_SYMBOL == scanner.getCurrentSymbol()){
					scanner.getNextSymbol();
					if(!NULLNODE.equals(thenStmt = isStatement())){
						scanner.getNextSymbol();
						if(Symbols.ELSE_SYMBOL == scanner.getCurrentSymbol()){
							scanner.getNextSymbol();
							if(!NULLNODE.equals(elseStmt = isStatement()))
								return new IfNode(line, column, boolExpr, thenStmt, elseStmt);
							else
								return getErrorNode("Statment expected after ELSE");
						} else
							return new IfNode(line, column, boolExpr, thenStmt);
					} else 
						return getErrorNode("Statement expected after THEN");
				} else
					return getErrorNode("THEN expected");			
			} else
				return getErrorNode("boolExpr expected");
		}
		
		return NULLNODE;
	}
	
	
	@Override
	Node isAssignment(){
		
		Node identNode;
		Node exprNode;
		
		if(! NULLNODE.equals(identNode = isIdentifier())){	
			int line = scanner.getLine();
			int column = scanner.getColumn();
			if(Symbols.ASSIGN_SYMBOL  == scanner.getCurrentSymbol()){
				scanner.getNextSymbol();
				if(! NULLNODE.equals(exprNode = isIntExpr())
						|| !NULLNODE.equals(exprNode = isBoolExpr()))
					return new AssignmentNode(line,column, identNode, exprNode);
				else
					return getErrorNode("IntExp or BoolExp expected on right hand side of ':='");
			} else 
				return getErrorNode("':=' expected");	
		}
		return NULLNODE;
	}
	
	@Override
	Node isStatement(){
		Node statement;
		
		if(!NULLNODE.equals(statement = isDeclarationSequence())
				|| !NULLNODE.equals(statement = isIfStmt()))
			return statement;
		
		if(!NULLNODE.equals(statement = isBlock())
				|| !NULLNODE.equals(statement = isAssignment())
				|| !NULLNODE.equals(statement = isRead())
				|| !NULLNODE.equals(statement = isWrite())){
			
			if(Symbols.DELIMITER_SYMBOL == scanner.getCurrentSymbol()){
				scanner.getNextSymbol();
				return statement;
			} else
				return getErrorNode("';' expected");
		}
		
		if(isEOFSymbol())
			return NULLNODE;
		
		return NULLNODE;
	}
	
	Node isBlock(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		Node statementSeq;
		
		if(Symbols.BEGIN_SYMBOL == scanner.getCurrentSymbol()){
			scanner.getNextSymbol();

			if(!NULLNODE.equals(statementSeq = isStatementSequence())){
				if(Symbols.END_SYMBOL == scanner.getCurrentSymbol()){
					scanner.getNextSymbol();
					return new Block(line, column, statementSeq);
				} else
					return getErrorNodeBlock("END symbol expected");
			} else 
				return getErrorNodeBlock("StatementSequence expected");
		}
		return NULLNODE;
	}
	
	Node isDeclarationSequence(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		boolean declFound = false;
		Node decl;
		Node declSeq = new SequenceNode(null, line, column);
		
		while(!NULLNODE.equals(decl = isDeclaration())){
			declFound = true;
			
			if(Symbols.DELIMITER_SYMBOL != scanner.getCurrentSymbol())
				return getErrorNode("';' expected");
			
			scanner.getNextSymbol();
			declSeq.addChild(decl);
		}
		return (declFound ? declSeq : NULLNODE);
	}
	
	@Override
	Node isProgram() {
		Node declarationSequence;
		Node blockSequence;
		Node block;
		Node programmNode = NULLNODE;

		int column = scanner.getColumn();
		int row = scanner.getLine();
		
		scanner.getNextSymbol();
		declarationSequence = isDeclarationSequence();
		
		blockSequence = new SequenceNode(null, row, column);
		boolean blockFound = false;
		column = scanner.getColumn();
		row = scanner.getLine();
		
		while(!NULLNODE.equals(block = isBlock())){
			blockFound = true;
			blockSequence.addChild(block);
			if(Symbols.DELIMITER_SYMBOL != scanner.getCurrentSymbol()){
				Node error = getErrorNode("';' expected");
				blockSequence.addChild(error);
				return blockSequence;
			}
			
			scanner.getNextSymbol();

			row = scanner.getLine();
			column = scanner.getColumn();			
		}
		
		if(!blockFound){
			Node error = getErrorNode("BEGIN symbol expected");
			blockSequence.addChild(error);
			return blockSequence;
		}
		
		if(!isEOFSymbol()){
			getErrorNode("EOF symbol at program end missing");
			return NULLNODE;
		}
		
		programmNode = new ProgrammNode(row, column, null);
		if(!NULLNODE.equals(declarationSequence))
			programmNode.addChild(declarationSequence);
		programmNode.addChild(blockSequence);
		return programmNode;		
	}
	
}
