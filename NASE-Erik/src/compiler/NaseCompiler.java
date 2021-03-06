/**
 * 
 */

package compiler;

import java.io.File;

import compiler.parsers.ParserFactory;
import compiler.parsers.ParserWrapper;
import compiler.scanner.ScannerFactory;
import compiler.scanner.ScannerWrapper;

import files.FileManager;

/**
 * @author student
 * 
 */
public class NaseCompiler {

	final static String[] FILE_EXTENSIONS = { ".lst", ".lst1", ".lst2", ".mms",
			".mml", ".mmo" };
	public final static String TOKEN_LIST_SEPERATOR = " ";
	
	
	private static ScannerWrapper scanner;
	private static ParserWrapper parser;

	private static void unlinkOldFiles(String filename) {
		System.out.println("[INFO]\tDeleting old files");
		for (String extension : FILE_EXTENSIONS) {
			File f = new File(filename + extension);
			if (f.exists()) {
				f.delete();
				System.out.println("[INFO]\tFile " + f.getName() + " deleted");
			}
		}

	}

	private static boolean phase1(String filename){
		parser.parseProgramm();
		return true;
	}
	
	private static boolean phase2(){
		return true;
	}
	
	private static boolean phase3(String filename){
		return true;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{		
		if (args.length < 1){
			System.err.println("[ERROR]\tUsage: java -jar NaseCompiler.jar <filename> [-<scannertype>]");
			System.err.println("[ERROR]\tUse no fileextension. \".nase\" assumed!");
			System.err.println("[ERROR)\tIf no scannertype is given own scanner is used. use -jflex for generated.");
			return;
		}
		
		if(args.length > 2){
			System.err.println("[WARNING]\tonly first argument is read");
		}
		
		if(!(new File(args[0]+".nase").exists())){
			System.err.println("[ERROR]\tFile " + args[0] + ".nase not found");
			return;
		}
		
		unlinkOldFiles(args[0]);
				
		FileManager.getInstance();
		FileManager.getInstance().init(args[0]);
		
		/* analyze options  -jflex for jflex generated scanner*/
		scanner = ScannerFactory.getScanner(args);
		parser = ParserFactory.getParser(args, scanner);
				
			//syntree = new Syntaxtree();
		if(phase1(args[0])){
			if(phase2()){
				if(phase3(args[0]))
					FileManager.getInstance().getListing().write("\nCompilation successfull!");
			}
		}		
		
		FileManager.getInstance().closeOutFiles();
		FileManager.getInstance().mergeOutFiles();
	}
}
