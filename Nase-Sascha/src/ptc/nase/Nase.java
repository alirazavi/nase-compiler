package ptc.nase;

import java.io.*;
import java.util.ArrayList;

import ptc.nase.exceptions.GeneralSyntaxError;
import ptc.nase.frontend.IScanner;
import ptc.nase.frontend.Infile;
import ptc.nase.frontend.JFlexNaseScanner;
import ptc.nase.frontend.Parser;
import ptc.nase.frontend.Scanner;
import ptc.nase.syntaxtree.nodes.NODE_TYPE;
import ptc.nase.syntaxtree.nodes.SyntaxtreeNode;

@SuppressWarnings("unused")
public class Nase 
{

	public static String IN_FILE_EXTENSION = ".nase";
	public static String LIST_FILE_EXTENSION = ".lst2";
	
	public static char IF_EOF_CHAR = 0x03;
	public static String ECHO_FILE_EXTENSION = ".lst1";
	public static char IF_TAB_CHAR = '\t';
	public static char IF_BLANK_CHAR = ' ';
	public static char IF_NEWLINE_CHAR = '\n';
	public static char IF_RETURN_CHAR = '\r';
	
	public static char BA_I_TABSIZE = 4;
	
	public static final char ST_BLANK_CHAR = ' ';
	
	
	
	public static void main(String[] args)
	{
		try 
		{
			IScanner scanner;
			//scanner = new Scanner("parsetest");

			scanner = new JFlexNaseScanner("parsetest");
			
			Parser parser = new Parser(scanner);			
			
			parser.parseProgram();
			
		} 
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeneralSyntaxError e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main_scannertest(String[] args) throws FileNotFoundException
	{
		Scanner scanner = new Scanner("test0");
		
		for (int i = 0; i < 30; i++)
			try 
			{
				//System.out.println(scanner.getNextToken());
				scanner.getNextSymbol();
				System.out.println("Gelesenes symbol: " + scanner.getCurrentSymbol());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		
		System.out.println("\n\n");
		scanner.dumpSymbolTable();
		
		System.out.println("Ende gelände...");
	}
	
	
	public static void main_2(String[] args)
	{
		try {
				
			//FileInputStream fileInStream = new FileInputStream ("name.txt");
			//InputStreamReader inputStreamReader = new InputStreamReader(fileInStream, "Unicode" );
			//InputStreamReader inputStreamReader = new InputStreamReader(fileInStream);
			
			
			Infile infile = new Infile("test0");
			
			int i = 0;
			
			do
			{
				infile.readNextChar();
				
				if (i <= 20)
					System.out.println(i + " " + infile.currentChar());
				
				if (i == 8)
					i = 8;
				
				if (i == 14)
					infile.unreadChar();
				
				i++;
				
			}
			while ( infile.currentChar() != Nase.IF_EOF_CHAR );
			
			System.out.println("ende gelände...");
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	

	public static void main_orig(String[] args)
	{
		
		if (args.length != 1)
		{
			Console.display("\nUsage nase <filename>\n");
			Console.display("Use no fileextension. " + IN_FILE_EXTENSION + " assumed!\n");
		}
		
		//Console.display("Hello world!");
		//Console.fatalError("Datei nicht gefunden!");
		
		Infile infile = new Infile(args[0]);
		
		infile.echoWriteLineNr(10);
		
		infile.echoWriteLine("Testzeile");
		
		infile.echoWriteLineNr(11);
		
		infile.echoWriteChar('x');
	}

}
