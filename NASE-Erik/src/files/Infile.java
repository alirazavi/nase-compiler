package files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class Infile {
	
	public static final char BLANK = ' ';
	public static final char TAB = '\t';
	public static final char NEWLINE = '\n';
	public static final char RETURN = '\r';
	
	private PushbackInputStream infile;
	
	private String infileName;
	
	private int column = 1;
	private int line = 1;
	
	private char currentChar;
	
	public Infile(String filename){

		try {
			this.infileName = filename + ".nase";
			infile = new PushbackInputStream(new FileInputStream(this.infileName));
					
		} catch (IOException e) {
			System.err.println("Error opening files");
			e.printStackTrace();
		}		
	}
	
	public void close(){
		
		// closing inFile
		try {
			infile.close();
		} catch (IOException e) {
			System.err.println("Error closing inFile");
			e.printStackTrace();
		}
		
	}
		
	public void readNextChar(){
		try {
			int character = infile.read();
			column++;
			
			if(character == -1){
				currentChar = FileManager.EOF;
			} else {
				currentChar = (char) character;
				FileManager.getInstance().getEchofile().echoWriteChar(currentChar);
			}	
			
			switch(currentChar){
				case TAB:
					currentChar = BLANK;
					column += 4;
					break;
				case NEWLINE:
					currentChar = BLANK;
					column = 1;
					++line;
					FileManager.getInstance().getEchofile().echoWriteLineNr(line);
					break;
				case RETURN:
					currentChar = BLANK;
					column = 1;
					break;
				case '$':
					character = infile.read();
					if(character != -1){
						currentChar = (char) character;
						while(currentChar != NEWLINE && currentChar != RETURN && character != -1){
							currentChar = (char) character;						
							if(currentChar == TAB)
								column +=4;
							else
								column++;
							FileManager.getInstance().getEchofile().echoWriteChar(currentChar);
							character = infile.read();							
						}
												
						if(character == -1){
							currentChar = FileManager.EOF;
							return;
						}
						
						currentChar = (char) character;
						
						FileManager.getInstance().getEchofile().echoWriteChar(currentChar);
												
						switch (currentChar) {
							case NEWLINE:
								currentChar = BLANK;
								column = 1;
								line++;
								FileManager.getInstance().getEchofile().echoWriteLineNr(line);
								break;
							case RETURN:
								currentChar = BLANK;
								column = 1;
								break;
						}
					}
					break;						
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void inFileUnreadChar(){
		if(currentChar != FileManager.EOF){
			try {
				infile.unread(currentChar);
				column--;
			} catch (IOException e) {
				System.err.println("Too many unread");
			}
		}
	}
	
	public int getColumn() {
		return column;
	}

	public int getLine() {
		return line;
	}

	public char getCurrentChar() {
		return currentChar;
	}
	
	

}
