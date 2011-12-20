package compiler.scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class ScannerFactory {

	public static ScannerWrapper getScanner(String[] args) throws FileNotFoundException {		
		List<String> arguments = Arrays.asList(args);
		if(arguments.contains("-jflex"))
			return new ScannerJFlex(new FileReader(args[0]+".nase"));
		else
			return new Scanner();
	}
	
}
