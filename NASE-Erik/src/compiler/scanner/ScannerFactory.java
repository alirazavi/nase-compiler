package compiler.scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ScannerFactory {

	
	
	public static ScannerWrapper getScanner(){
		return new Scanner();
	}

	public static ScannerWrapper getScanner(String type, String filename) throws FileNotFoundException {
		if(type.equalsIgnoreCase("jflex"))
			return new ScannerJFlex(new FileReader(filename+".nase"));
		return null;
	}
	
}
