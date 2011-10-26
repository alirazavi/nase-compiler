package compiler;

import syntaxtree.Node;
import files.FileManager;

public class Parser {

	private Scanner scanner;	
	private Node rootNode;

	public Parser(Scanner s) {
		scanner = s;
	}

	private Node isProgramm() {
		Node statementSequenceNode;
		Node programmNode;

		int column = FileManager.getInstance().getInfile().getColumn();
		int row = FileManager.getInstance().getInfile().getLine();
		
		
		while(scanner.getNextSymbol()){
			;
		}
		
		
		
		/*scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();
		scanner.getNextSymbol();*/
		
		
		

		//if(Syntaxtree.NULL_NODE != ())
		
		return new Node(null);

	}

	public void parseProgramm() {
		String buffer;

		FileManager.getInstance().getListing().write(" ");
		
		rootNode = isProgramm(); 

	}

}
