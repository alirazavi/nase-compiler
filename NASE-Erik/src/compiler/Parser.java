package compiler;

import symboltable.Symbols;
import syntaxtree.nodes.ErrorNode;
import syntaxtree.nodes.Node;
import syntaxtree.nodes.NullNode;
import syntaxtree.nodes.ProgrammNode;
import syntaxtree.nodes.StatementSequenceNode;
import files.FileManager;
import files.Infile;

public class Parser {

	private Scanner scanner;	
	private Node rootNode;
	private final NullNode NULLNODE = new NullNode();
	
	
	public Parser(Scanner s) {
		scanner = s;
	}

	private boolean isEOFSymbol(){
		return Infile.EOF == FileManager.getInstance().getInfile().getCurrentChar();
	}
	
	private void reportGeneralSyntaxError(int line, int column, String error){
		FileManager.getInstance().getListing().write(String.format("\nSYNTAX ERROR near line %d, column %d: %s", line, column, error));	
	}
	
	private Node isDeclaration(){
		
		return NULLNODE;
	}
	
	private Node isStatement(){
		Node statement;	
		int line;
		int column;
		
		if(! NULLNODE.equals((statement = isDeclaration()))){
			if(Symbols.DELIMITER_SYMBOL.ordinal() == scanner.getCurrentSymbol()){
				scanner.getNextSymbol();
				return statement;
			} else {
				line = FileManager.getInstance().getInfile().getLine();
				column = FileManager.getInstance().getInfile().getColumn();
				
				reportGeneralSyntaxError(line, column, "';' expected");
				statement = new ErrorNode(line, column);
				scanner.skipToDelimiter();
				return statement;
			}
		}
		
		
		return NULLNODE;
	}
	
	private Node isStatementSequence(){
		Node statementNode;
		Node statementSequenceNode = NULLNODE;
		Node firstStatementSequenceNode;
		
		int column = FileManager.getInstance().getInfile().getColumn();
		int row = FileManager.getInstance().getInfile().getLine();		
		
		if(!NULLNODE.equals((statementNode = isStatement()))){
			statementSequenceNode = new StatementSequenceNode(NULLNODE, row, column);
			statementSequenceNode.addChild(statementNode);
		}
		firstStatementSequenceNode = statementSequenceNode;
		
		while(!NULLNODE.equals(statementNode = isStatement())){		
			column = FileManager.getInstance().getInfile().getColumn();
			row = FileManager.getInstance().getInfile().getLine();
			statementSequenceNode = new StatementSequenceNode(statementSequenceNode, row, column);
			statementSequenceNode.addChild(statementNode);
		}
			
		return firstStatementSequenceNode;
	}
	
	private Node isProgramm() {
		Node statementSequenceNode;
		Node programmNode = NULLNODE;

		int column = FileManager.getInstance().getInfile().getColumn();
		int row = FileManager.getInstance().getInfile().getLine();
		
		scanner.getNextSymbol();
		
		statementSequenceNode = isStatementSequence();
		if(!statementSequenceNode.equals(NULLNODE)){
			if(isEOFSymbol()){
				programmNode = new ProgrammNode(null, row, column);
				return programmNode;
			} else {
				reportGeneralSyntaxError(row, column, "EOF symbol at program end missed");
			}
			return NULLNODE;	
		}
		return NULLNODE;		
	}

	public void parseProgramm() {
		String buffer;

		FileManager.getInstance().getListing().write(" ");
		
		rootNode = isProgramm(); 
		
		FileManager.getInstance().getListing().write(rootNode.toString());

	}

}
