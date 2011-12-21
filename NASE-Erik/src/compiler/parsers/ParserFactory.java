package compiler.parsers;

import java.util.Arrays;
import java.util.List;

import compiler.scanner.ScannerWrapper;

public class ParserFactory {

	public static ParserWrapper getParser(String[] args, ScannerWrapper scanner){
		List<String> arguments = Arrays.asList(args);
		if(arguments.contains("-blx"))
			return new ParserBLX(scanner);
		else if(arguments.contains("-blxif"))
			return new ParserBLXIF(scanner);
		else
			return new Parser(scanner);
	}
	
}
