package compiler.parsers;

import symboltable.SymbolTable;
import symboltable.Symbols;
import syntaxtree.nodes.AssignmentNode;
import syntaxtree.nodes.ConstantNode;
import syntaxtree.nodes.DeclarationNode;
import syntaxtree.nodes.DyadicOpNode;
import syntaxtree.nodes.ErrorNode;
import syntaxtree.nodes.IdentifierNode;
import syntaxtree.nodes.InlineIfNode;
import syntaxtree.nodes.MondanicOpNode;
import syntaxtree.nodes.Node;
import syntaxtree.nodes.ParenthesisNode;
import syntaxtree.nodes.ProgrammNode;
import syntaxtree.nodes.ReadNode;
import syntaxtree.nodes.SequenceNode;
import syntaxtree.nodes.TypeNameNode;
import syntaxtree.nodes.WriteNode;

import compiler.scanner.ScannerWrapper;

public class Parser extends ParserWrapper{
		
	public Parser(ScannerWrapper scanner) {
		this.scanner = scanner;
	}
	
	boolean isEOFSymbol(){
		return Symbols.EOF == scanner.getCurrentSymbol();
	}
	
	void reportGeneralSyntaxError(int line, int column, String error){
		listing.write(String.format("\nSYNTAX ERROR near line %d, column %d: %s", line, column, error));	
	}
	
	boolean isRelationalOp(int sym){
		return (sym == Symbols.LT_SYMBOL
				 || sym == Symbols.LE_SYMBOL
				 || sym == Symbols.EQ_SYMBOL
				 || sym == Symbols.GT_SYMBOL
				 || sym == Symbols.GE_SYMBOL
				 || sym == Symbols.NE_SYMBOL);
	}
	
	Node getErrorNode(String message){
		int column = scanner.getColumn();
		int row = scanner.getLine();
		reportGeneralSyntaxError(row, column, message);
		scanner.skipToDelimiter();
		return new ErrorNode(row, column);
	}
	
	/*
	 * typeName = INT_TYPE_SYMBOL.
	 */
	Node isTypeName(){
		Node type = NULLNODE;
		int column = scanner.getColumn();
		int row = scanner.getLine();		
		
		switch(scanner.getCurrentSymbol()){
			case Symbols.INT_TYPE_SYMBOL:
				type = new TypeNameNode(column, row, scanner.getCurrentSymbol());
				scanner.getNextSymbol();
				break;					
		}
		
		return type;
	}
	
	/*
	 * typeName = INT_TYPE_SYMBOL.
	 */
	Node isInteger(){
		int symbol = scanner.getCurrentSymbol();
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		if(SymbolTable.getInstance().isAnyNumericSymbol(symbol)){
			scanner.getNextSymbol();
			Node constantNode = SymbolTable.getInstance().getNodeLink(symbol);
			if(constantNode == null){
				constantNode = new ConstantNode(line, column, symbol);
				SymbolTable.getInstance().setNodeLink(symbol, constantNode);
			}
			return constantNode;
		}
		return NULLNODE;
	}
		
	/*
	 * declaration = typeName identifier { COMMA_SYMBOL identifier }.
	 */
	Node isDeclaration(){
		Node type;
		Node decl;
		Node declseq = NULLNODE;
		Node firstdeclseq = NULLNODE;
		
		boolean commaOccured = false;
		int column = scanner.getColumn();
		int row = scanner.getLine();		
		
		if(!NULLNODE.equals(type = isTypeName())){
			do{
				int currentSymbol = scanner.getCurrentSymbol();
				
				if(SymbolTable.getInstance().isAnyIdentifierSymbol(currentSymbol)){
					decl = new DeclarationNode(column, row, currentSymbol, type);
					if(!scanner.getNextSymbol())
						return getErrorNode("Unexpected EOF");
				} else 
					return getErrorNode("Identifier expected after type name");
				
				column = scanner.getColumn();
				row = scanner.getLine();
				
				if(scanner.getCurrentSymbol() == Symbols.COMMA_SYMBOL ){
					declseq = new SequenceNode(NULLNODE, row, column);
					declseq.addChild(decl);
					if(!commaOccured){
						commaOccured = true;
						firstdeclseq = declseq;
					}
					if(!scanner.getNextSymbol())						
						return getErrorNode("Unexpected EOF");
					
				} else if(scanner.getCurrentSymbol() == Symbols.DELIMITER_SYMBOL ){
					declseq = new SequenceNode(NULLNODE, row, column);
					declseq.addChild(decl);
					if(!commaOccured)
						firstdeclseq = declseq;
				} else 
					return getErrorNode("',' or ';' expected after an identifier declaration");	
				
			}while(scanner.getCurrentSymbol() != Symbols.DELIMITER_SYMBOL );
			return firstdeclseq;
		}
		return NULLNODE;
	}
	
	/*
	 * identifier = ANY_LETTER { ANY_DIGIT | ANY_LETTER }.
	 */
	Node isIdentifier(){
		int symbol = scanner.getCurrentSymbol();
		if(SymbolTable.getInstance().isAnyIdentifierSymbol(symbol)){
			scanner.getNextSymbol();
			return new IdentifierNode(scanner.getLine(),scanner.getColumn(), symbol);
		}
		return NULLNODE;
	}

	/*
	 * intFactor = integer | identifier | OPEN_PARAENTHESIS_SYMBOL intExpr CLOSE_PARENTHESIS_SYMBOL | inlineIfStatement.
	 */
	Node isIntFactor(){
		int symbol = scanner.getCurrentSymbol();
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		Node factor;
		Node expr;
		
		if(!NULLNODE.equals(factor = isInteger())
			 || !NULLNODE.equals(factor = isIdentifier())
			 || !NULLNODE.equals(factor = isInlineIf())){
			return factor;
		} else if(scanner.getCurrentSymbol() == Symbols.OPEN_PARAENTHESIS_SYMBOL) {
			scanner.getNextSymbol();
			symbol = scanner.getCurrentSymbol();
			if(!NULLNODE.equals(expr = isIntExpr())){
				symbol = scanner.getCurrentSymbol();
				if(symbol == Symbols.CLOSE_PARENTHESIS_SYMBOL){
					scanner.getNextSymbol();
					return new ParenthesisNode(line, column, expr);
				} else
					return getErrorNode("')' expected");
			} else
				return getErrorNode("intExpr expected after '('");
		}
		return getErrorNode("intFactor expected");
	}

	/*
	 * intTerm = isIntTerm { multOp intFactor }.
	 */
	Node isIntTerm(){
		Node leftFactor;
		Node rightFactor;
		
		if(NULLNODE.equals(leftFactor = isIntFactor()))
			return NULLNODE;
		
		int symbol = scanner.getCurrentSymbol();
	
		while(Symbols.TIMES_SYMBOL  == symbol
				|| Symbols.DIVIDE_SYMBOL  == symbol
				|| Symbols.MODULO_SYMBOL  == symbol){
			
			int column = scanner.getColumn();
			int row = scanner.getLine();			
			scanner.getNextSymbol();
			
			if(NULLNODE.equals(rightFactor = isIntFactor()))
				return getErrorNode("intFactor expected after 'multOp'");	
			else {
				Node newTerm = new DyadicOpNode(row, column, symbol, leftFactor, rightFactor);
				leftFactor = newTerm;
				symbol = scanner.getCurrentSymbol();
			}
		}
		return leftFactor;
	}
	
	/*
	 * intExpr = [ MINUS_SYMBOL ] term { addOp term }.
	 */
	Node isIntExpr(){
		boolean minusOccured = false;
		Node leftTerm;
		Node rightTerm;
		Node minusPrefix = null;
		
		int column, row;
		
		int columnMinus = scanner.getColumn();
		int rowMinus = scanner.getLine();
		if(scanner.getCurrentSymbol() == Symbols.MINUS_SYMBOL ){
			minusOccured = true;
			scanner.getNextSymbol();
		}
		
		if(NULLNODE.equals(leftTerm = isIntTerm())){
			if(!minusOccured)
				return NULLNODE;
			
			return getErrorNode("intTerm expected after '-'");	
		}
		
		if(minusOccured){
			minusPrefix = new MondanicOpNode(rowMinus, columnMinus, Symbols.MINUS_SYMBOL, leftTerm);
			leftTerm = minusPrefix;
		}
		
		while(Symbols.MINUS_SYMBOL  == scanner.getCurrentSymbol()
				|| Symbols.PLUS_SYMBOL  == scanner.getCurrentSymbol()){
			
			column = scanner.getColumn();
			row = scanner.getLine();
			
			scanner.getNextSymbol();
			if(NULLNODE.equals(rightTerm = isIntTerm()))
				return getErrorNode("intTerm expected after 'addOp'");	
			else {
				Node newTerm = new DyadicOpNode(row, column, scanner.getCurrentSymbol(), leftTerm, rightTerm);
				leftTerm = newTerm;				
			}
		}
		return leftTerm;
	}
	
	/*
	 * inlineIfStatement = INLINE_IF_SYMBOL boolExpr INLINE_THEN_SYMBOL intExpr INLINE_ELSE_SYMBOL intExpr INLINE_FI_SYMBOL.
	 */
	Node isInlineIf(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		Node boolExpr;
		Node thenExpr;
		Node elseExpr;

		if(scanner.getCurrentSymbol() == Symbols.INLINE_IF_SYMBOL){
			scanner.getNextSymbol();
			if(!NULLNODE.equals(boolExpr = isBoolExpr())){
				if(scanner.getCurrentSymbol() == Symbols.INLINE_THEN_SYMBOL){
					scanner.getNextSymbol();
					if(!NULLNODE.equals(thenExpr = isIntExpr())){
						scanner.getNextSymbol();
						if(scanner.getCurrentSymbol() == Symbols.INLINE_ELSE_SYMBOL){
							scanner.getNextSymbol();
							if(!NULLNODE.equals(elseExpr = isIntExpr())){
								scanner.getNextSymbol();
								if(scanner.getCurrentSymbol() == Symbols.INLINE_FI_SYMBOL)
									return new InlineIfNode(line, column, boolExpr, thenExpr, elseExpr);
								else
									return getErrorNode("FII expected");
							} else
								return getErrorNode("intExpr expected");
						} else
							return getErrorNode("':' expected");
					} else
						return getErrorNode("intExpr expected");
				} else 
					return getErrorNode("'?' expected");
			} else
				return getErrorNode("boolExpr expected");
		} else 
			return NULLNODE;
	}

	/*
	 * boolExpr = intExpr relationOp intExpr { boolOp intExpr relationOp intExpr }.
	 */
	Node isBoolExpr(){
		Node leftInt;
		Node rightInt;
		Node boolExpr1;
		Node boolExpr2;
		Node boolExpr3;
		
		int currentSymbol;
		int currentSymbolForBoolOp;
		int line;
		int column;
				
		if(!NULLNODE.equals(leftInt = isIntExpr())){
			currentSymbol = scanner.getCurrentSymbol();
			line = scanner.getLine();
			column = scanner.getColumn();
			
			if(isRelationalOp(currentSymbol)){
				scanner.getNextSymbol();
				if(!NULLNODE.equals(rightInt = isIntExpr()))
					boolExpr1 = new DyadicOpNode(line, column, currentSymbol, leftInt, rightInt);
				else
					return getErrorNode("'intExpr' expected after 'relationOp'");
			} else
				return getErrorNode("'relationOp' expected after 'intExpr'");
		} else
			return NULLNODE;
		
		currentSymbolForBoolOp = scanner.getCurrentSymbol();
		while(Symbols.AND_SYMBOL == currentSymbolForBoolOp
				|| Symbols.OR_SYMBOL == currentSymbolForBoolOp) {
			line = scanner.getLine();
			column = scanner.getColumn();
			scanner.getNextSymbol();
			if(!NULLNODE.equals(leftInt = isIntExpr())){
				currentSymbol = scanner.getCurrentSymbol();
				if(isRelationalOp(currentSymbol)){
					scanner.getNextSymbol();
					if(!NULLNODE.equals(rightInt = isIntExpr())){
						boolExpr2 = new DyadicOpNode(line, column, currentSymbol, leftInt, rightInt);
						boolExpr3 = new DyadicOpNode(line, column, currentSymbolForBoolOp, boolExpr1, boolExpr2);
						boolExpr1 = boolExpr3;
						currentSymbolForBoolOp = scanner.getCurrentSymbol();
					} else
						return getErrorNode("'intExpr' expected after 'relationOp'");
				} else
					return getErrorNode("'relationOp' expected after 'intExpr'");
			} else
				return getErrorNode("'intExpr' expected after 'boolOp'");
		}
		
		return boolExpr1;
	}

	/*
	 * write = WRITE_SYMBOL identifier.
	 */
	Node isWrite(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		Node expr;
		
		if(scanner.getCurrentSymbol() == Symbols.WRITE_SYMBOL ){
			scanner.getNextSymbol();
			
			if(!NULLNODE.equals(expr = isIntExpr())
					|| !NULLNODE.equals(expr = isBoolExpr()))
				return new WriteNode(line, column, expr);
			else
				return getErrorNode("intExpr or bool Expr expected after WRITE");
		}
		return NULLNODE;
	}
	
	/*
	 * read = READ_SYMBOL identifier.
	 */
	Node isRead(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		
		Node ident;
		
		if(scanner.getCurrentSymbol() == Symbols.READ_SYMBOL ){
			scanner.getNextSymbol();
			
			if(!NULLNODE.equals(ident = isIdentifier()))
				return new ReadNode(line, column, ident);
			else 
				return getErrorNode("Identifier expected after read");
		}
		return NULLNODE;
	}

	/*
	 * assignment = identifier ASSIGN_SYMBOL intExpr.
	 */
	Node isAssignment(){
		
		Node identNode;
		Node exprNode;
		
		if(! NULLNODE.equals(identNode = isIdentifier())){	
			int line = scanner.getLine();
			int column = scanner.getColumn();
			if(Symbols.ASSIGN_SYMBOL  == scanner.getCurrentSymbol()){
				scanner.getNextSymbol();
				if(! NULLNODE.equals(exprNode = isIntExpr()))
					return new AssignmentNode(line,column, identNode, exprNode);
				else
					return getErrorNode("IntExpr expected on right hand side of ':='");
			} else 
				return getErrorNode("':=' expected");	
		}
		return NULLNODE;
	}

	/*
	 * statement = [ declaration | assignment | read | write ] DELIMITER_SYMBOL.
	 */
	Node isStatement(){
		Node statement;
		
		if(!NULLNODE.equals(statement = isDeclaration())
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

	/*
	* statementSequence = statement { statement }.
	*/
	Node isStatementSequence(){
		int line = scanner.getLine();
		int column = scanner.getColumn();		
		boolean statementFound = false;
		Node statement = NULLNODE;
		Node statementSequence = NULLNODE;
		
		while(!NULLNODE.equals(statement = isStatement())){
			if(!statementFound){
				statementFound = true;
				statementSequence = new SequenceNode(null, line, column);
			}
			statementSequence.addChild(statement);
			line = scanner.getLine();
			column = scanner.getColumn();		
		}
		return statementSequence;
	}

	Node isProgram(){
		int line = scanner.getLine();
		int column = scanner.getColumn();
		scanner.getNextSymbol();
		
		Node statementSeq;
		
		if(NULLNODE.equals(statementSeq = isStatementSequence())){
			if(isEOFSymbol()){
				return new ProgrammNode(line, column, statementSeq);
			} else
				return getErrorNode("EOF symbol at program end missed");
		} else
			return NULLNODE;
	}


	public void parseProgramm() {
		listing.write(" ");
		
		rootNode = isProgram(); 
		
		listing.write("\n");
		listing.write("SYMBOLTABLE-DUMP\n---------------------\n");
		listing.write(SymbolTable.getInstance().toString());
		listing.write("SYNTAXTREE-DUMP\n---------------------\n");
		listing.write(rootNode.toString());
		listing.write("NODEHISTORY-DUMP\n---------------------\n");
		listing.write(dumpHistory());
		listing.write("SIMPLE-SYNTAXTREE-DUMP\n---------------------\n");
		listing.write("\n"+rootNode.dump(0));
		
	}
}